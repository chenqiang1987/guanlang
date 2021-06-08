package com.twc.guanlang.service;


import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.user.Permit;
import com.twc.guanlang.entity.user.Role2Permit;
import com.twc.guanlang.mapper.entity.PermitMapper;
import com.twc.guanlang.mapper.entity.Role2PermitMapper;
import com.twc.guanlang.mapper.entity.User2RoleMapper;
import com.twc.guanlang.param.Constants;
import com.twc.guanlang.param.permit.PermitAddOneParam;
import com.twc.guanlang.common.annotation.TwcAnnotationParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 权限service
 * <p>
 * ::使用了很多递归，递归比较吃性能
 * 一般权限菜单数量不会特别多，性能应该可以
 */
@Service
public class PermitService {


    @Autowired
    private User2RoleMapper user2RoleMapper;

    @Autowired
    private PermitMapper permitMapper;


    /**
     * 递归
     * 根据当前节点获取所有的父节点
     *
     * @param nodeId
     * @param list   用户的权限集合，取所有的叶子节点，叶子节点网上递归必然会查询到非叶子节点，减少递归次数
     */
    private void getAllParentIdByNodeId(int nodeId, List list) {

        Example example = new Example(Permit.class);
        example.createCriteria().andEqualTo("id", nodeId);
        Permit node = permitMapper.selectByExample(example).get(0);
        if (node.getParentId() != 0) {
            list.add(node.getParentId());
            getAllParentIdByNodeId(node.getParentId(), list);
        }
    }


    /**
     * 递归
     * 根据用户的权限集合，遍历每个树节点，判断是否属于当前用户，前端可以根据forUser来做节点的处理
     *
     * @param allTree
     * @param list
     */
//    private void treeNode(Permit allTree, List<Permit> list) {
//
//        if (allTree.getChilds() != null && allTree.getChilds().size() > 0) {
//            for (Permit treeNode : allTree.getChilds()) {
//                for (Permit userNode : list) {
//                    List parentIds = new ArrayList();
//                    getAllParentIdByNodeId(userNode.getId(), parentIds);
//                    if (treeNode.getId() == userNode.getId() || userNode.getParentId() == userNode.getId() || parentIds.contains(treeNode.getId())) {
//                        treeNode.setForUser(1);
//                    }
//                }
//                if (treeNode.getChilds() != null && treeNode.getChilds().size() > 0) {
//                    treeNode(treeNode, list);
//                }
//            }
//        }
//    }

    /**
     * 获取用户的菜单资源
     * <p>
     * 取叶子节点
     * is_leaf=1 这个很重要，有子级的菜单不是叶子节点
     * 新增数据的时候注意设置此此变量
     *
     * @param userId
     * @return
     */
//    public Permit findMenuByUserId(String userId) {
//
//            Permit permit = new Permit();
//            List<Permit> list = permitMapper.selectPermitsByUserId(userId);
//            Permit allTree = getAllPermits();
//            treeNode(allTree, list);
//            return allTree;
//    }


//    public List<Permit> findMenuByUserIdNoTree(String userId) {
//
//        Permit permit = new Permit();
//        List<Permit> list = permitMapper.selectPermitsByUserIdNoTree(userId);
//        Permit allTree = getAllPermits();
//        treeNode(allTree, list);
//        return null;
//    }


    /**
     * 角色的资源
     *
     * @param roleId
     * @return
     */
    public Permit getPermitsByRole(String roleId) {

        Permit permit = new Permit();
        List<Permit> list = permitMapper.selectPermitsByRoleId(roleId);
        Permit allTree = getAllPermits();
       // treeNode(allTree, list);
        return allTree;
    }


    /**
     * 角色的资源
     *
     * @param roleId
     * @return
     */
    public List<Permit> getPermitsByRoleNoTree(String roleId) {

        Permit permit = new Permit();
        List<Permit> list = permitMapper.selectPermitsByRoleIdNoTree(roleId);

        return list;
    }

    @Autowired
    private Role2PermitMapper role2PermitMapper;

    @Transactional
    public void updatePermitForRoleId(String roleId,String permitIds) {

       Example example=new Example(Role2Permit.class);
       example.createCriteria().andEqualTo("roleId",roleId);
       List<Role2Permit> role2Permits=role2PermitMapper.selectByExample(example);
       for(Role2Permit role2Permit:role2Permits){

           role2PermitMapper.delete(role2Permit);
       }

       String[] pids=permitIds.split(",");

       for(String pid:pids){

           Role2Permit role2Permit=new Role2Permit();
           role2Permit.setRoleId(Integer.parseInt(roleId));
           role2Permit.setPermitId(Integer.parseInt(pid));
           role2Permit.setEnable(BaseEntity.ENABLE.ENABLE);
           role2Permit.setUpdateTime(new Date());
           role2Permit.setCreateTime(new Date());
           role2PermitMapper.insert(role2Permit);
       }
    }


    /**
     * 传入根节点查询菜单树  --递归
     *
     * @param rootId
     * @return
     */
    private Permit getTreeByRoot(Permit rootPermit) {

        Example example = new Example(Permit.class);
        example.createCriteria().andEqualTo("parentId", rootPermit.getId()).andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        List<Permit> childs = permitMapper.selectByExample(example);
        if (childs != null && childs.size() > 0) {
            rootPermit.setChilds(childs);
            for (Permit child : childs) {
                getTreeByRoot(child);
            }
        }
        return rootPermit;
    }

    public Permit getAllPermits() {

        Permit permit = new Permit();
        permit.setForUser(1);
        permit.setId(0l);
        permit.setEnable(BaseEntity.ENABLE.ENABLE);
        return getTreeByRoot(permit);
    }



    public List<Permit> getAllNoTree() {

        Example example = new Example(Permit.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        return permitMapper.selectByExample(example);
    }

    @Transactional
    public void deleteOne(String id) {
        Permit permit = permitMapper.selectByPrimaryKey(id);
        permit.setEnable(BaseEntity.ENABLE.DISABLE);
        permitMapper.updateByPrimaryKey(permit);

    }


    @Transactional
    public void addOne(PermitAddOneParam permitAddOneParam) throws CustomException {

        TwcAnnotationParser.parseParamCheck(permitAddOneParam);
        Permit permit = new Permit();
        permit.setName(permitAddOneParam.getName());
        permit.setParentId(permitAddOneParam.getParentId());
        permit.setEnable(BaseEntity.ENABLE.ENABLE);
        permit.setCreateTime(new Date());
        permit.setUpdateTime(new Date());
        permit.setIsLeaf(1);
        permit.setPath(permitAddOneParam.getPath());
        permitMapper.insert(permit);
        if (permitAddOneParam.getParentId() == 0)
            return;
        Permit parent = permitMapper.selectByPrimaryKey(permitAddOneParam.getParentId());
        parent.setIsLeaf(0);
        permitMapper.updateByPrimaryKey(parent);
    }

    public static void main(String a[]) {


    }


}
