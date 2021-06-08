package com.twc.guanlang.controller;

import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.param.role.RoleAddOneParam;
import com.twc.guanlang.param.role.RoleModifyOneParam;
import com.twc.guanlang.service.RoleService;
import com.twc.guanlang.common.log.LogUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 需要指定权限可以访问的地址
 * authc：所有已登陆用户可访问
 * <p>
 * roles：有指定角色的用户可访问，通过[ ]指定具体角色，这里的角色名称与数据库中配置一致
 * <p>
 * perms：有指定权限的用户可访问，通过[ ]指定具体权限，这里的权限名称与数据库中配置一致
 * <p>
 * anon：所有用户可访问，通常作为指定页面的静态资源时使用
 *
 * @author chenqiang
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 获取用户的角色列表
     *
     * @return
     */
    @ApiOperation("查询当前登陆用户的角色")
    @PostMapping("/authc/getRolesByUser")
    public Object getRolesByUser() {

        LogUtil.controllerLog(this.getClass(), "getRolesByUser", null);
        Subject subject = SecurityUtils.getSubject();
        BaseEntity user = (SystemUser) subject.getSession().getAttribute("user");
        List list = roleService.findRolesByUserId(user.getId());
        Map map = new HashMap();
        map.put("list", list);
        if (list == null || list.size() == 0) {
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
        } else {
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), map);
        }
    }

    /**
     * 查询系统所有角色
     *
     * @return
     */
    @ApiOperation("查询系统所有角色")
    @PostMapping("/authc/allRoles")
    public Object allRoles() {

        LogUtil.controllerLog(this.getClass(), "allRoles", null);
        List list = roleService.allRoles();
        Map map = new HashMap();
        map.put("list", list);
        if (list == null || list.size() == 0) {
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
        } else {
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), map);
        }
    }

    /**
     * 删除单个角色
     *
     * @param id
     * @return
     */
    @ApiOperation("删除单个角色")
    @PostMapping("/authc/deleteOne")
    public Object deleteOne(@RequestParam String id) {

        roleService.deleteOne(id);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 添加单个角色
     *
     * @param roleAddOneParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("添加单个角色")
    @PostMapping("/authc/addOne")
    public Object addOne(@RequestBody RoleAddOneParam roleAddOneParam) throws CustomException {

        roleService.addOne(roleAddOneParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 修改单个角色
     *
     * @param roleModifyOneParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("修改单个角色")
    @PostMapping("/authc/modifyOne")
    public Object modifyOne(@RequestBody RoleModifyOneParam roleModifyOneParam) throws CustomException {

        roleService.modifyOne(roleModifyOneParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }
}


