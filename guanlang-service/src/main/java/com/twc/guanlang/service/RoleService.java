package com.twc.guanlang.service;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;

import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.user.Role;
import com.twc.guanlang.entity.user.User2Role;
import com.twc.guanlang.mapper.entity.RoleMapper;
import com.twc.guanlang.mapper.entity.User2RoleMapper;
import com.twc.guanlang.param.role.RoleAddOneParam;
import com.twc.guanlang.param.role.RoleModifyOneParam;
import com.twc.guanlang.param.role.RoleSelectOneParam;
import com.twc.guanlang.common.str.TwcStringUtil;
import com.twc.guanlang.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;


    /**
     * 获取所有角色
     *
     * @return
     */
    public List<Role> allRoles() {


        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        List list = roleMapper.selectByExample(example);
        return (list == null || list.size() == 0) ? null : list;
    }

    public List<Role> getPermitByRoleId(int roleId) {

        List list = roleMapper.selectAll();
        return (list == null || list.size() == 0) ? null : list;
    }


    @Autowired
    private User2RoleMapper user2RoleMapper;

    /**
     * 用户的角色列表
     *
     * @param userId
     * @return
     */
    public List<RoleVO> findRolesByUserId(Long userId) {


        Example example = new Example(User2Role.class);
        example.createCriteria().andEqualTo("userId", userId);

        List<User2Role> user2Roles = user2RoleMapper.selectByExample(example);

        if (user2Roles == null || user2Roles.size() == 0) return null;
        StringBuffer roleIds = new StringBuffer();

        user2Roles.forEach((e) -> {
            roleIds.append(e.getRoleId() + ",");
        });
        String roles = roleIds.substring(0, roleIds.toString().length() - 1);
        List<RoleVO> list = roleMapper.selectRolesByRoleIds(roles);

        return list;
    }


    @Transactional
    public void deleteOne(String id) {

        Role role = roleMapper.selectByPrimaryKey(id);
        role.setEnable(BaseEntity.ENABLE.DISABLE);
        role.setUpdateTime(new Date());
        roleMapper.updateByPrimaryKey(role);

    }


    @Transactional
    public void modifyOne(RoleModifyOneParam roleModifyOneParam) throws CustomException {

        Role oldRole = roleMapper.selectByPrimaryKey(roleModifyOneParam.getId());

        if (!oldRole.getNickName().equalsIgnoreCase(roleModifyOneParam.getNickName())) {

            RoleSelectOneParam roleSelectOneParam = new RoleSelectOneParam();
            roleSelectOneParam.setNickName(roleModifyOneParam.getNickName());
            Role exists = selectOne(roleSelectOneParam);
            if (exists != null)
                throw new CustomException(ApiResultBean.CODE1.ROLE_NICK_NAME_UNIQUE_ERROR);
        }
        oldRole.setNickName(roleModifyOneParam.getNickName());
        oldRole.setUpdateTime(new Date());
        roleMapper.updateByPrimaryKey(oldRole);

    }

    @Transactional
    public void addOne(RoleAddOneParam roleAddOneParam) throws CustomException {

        RoleSelectOneParam roleSelectOneParam = new RoleSelectOneParam();
        roleSelectOneParam.setName(roleAddOneParam.getName());
        Role exists = selectOne(roleSelectOneParam);
        if (exists != null)
            throw new CustomException(ApiResultBean.CODE1.ROLE_NAME_UNIQUE_ERROR);

        roleSelectOneParam.setName("");
        roleSelectOneParam.setNickName(roleAddOneParam.getNickName());
        exists = selectOne(roleSelectOneParam);
        if (exists != null)
            throw new CustomException(ApiResultBean.CODE1.ROLE_NICK_NAME_UNIQUE_ERROR);

        Role role = new Role();
        role.setName(roleAddOneParam.getName());
        role.setNickName(roleAddOneParam.getNickName());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setEnable(BaseEntity.ENABLE.ENABLE);
        roleMapper.insert(role);
    }


    public Role selectOne(RoleSelectOneParam roleSelectOneParam) {

        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        if (TwcStringUtil.isNotEmpty(roleSelectOneParam.getRoleId())) {
            criteria.andEqualTo("id", roleSelectOneParam.getRoleId());
        }

        if (TwcStringUtil.isNotEmpty(roleSelectOneParam.getName())) {
            criteria.andEqualTo("name", roleSelectOneParam.getName());
        }

        if (TwcStringUtil.isNotEmpty(roleSelectOneParam.getNickName())) {
            criteria.andEqualTo("nickName", roleSelectOneParam.getNickName());
        }
        return roleMapper.selectOneByExample(example);

    }

}