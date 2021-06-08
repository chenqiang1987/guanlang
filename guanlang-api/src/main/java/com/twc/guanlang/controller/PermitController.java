package com.twc.guanlang.controller;

import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.entity.user.Permit;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.param.Constants;
import com.twc.guanlang.param.permit.PermitAddOneParam;
import com.twc.guanlang.service.PermitService;
import com.twc.guanlang.service.RoleService;
import com.twc.guanlang.common.log.LogUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;


/**
 * 资源菜单控制器
 * <p>
 * authc表示需要登陆才能访问
 * @author chenqiang
 */
@RestController
@RequestMapping("permit")
public class PermitController extends BaseController {

    @Resource
    private RoleService roleService;
    @Resource
    private PermitService permitService;

    /**
     * 所有的资源菜单 支持树形结构和非树形结构
     *
     * @return
     */
    @ApiOperation("系统所有的菜单资源-树形结构")
    @PostMapping("/authc/allPermits")
    @ResponseBody
    public Object allPermits() {
        LogUtil.controllerLog(PermitController.class, "allPermits", null);
        if(Constants.isTree){
            Permit permit = permitService.getAllPermits();
            if (permit == null) {
                return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
            } else {
                return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), permit);
            }
        }else{
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), permitService.getAllNoTree());
        }
    }

    /**
     * 获取角色的菜单资源
     *
     * @return
     */
    @ApiOperation("获取角色的菜单资源-树形结构")
    @PostMapping("/authc/getPermitByRoleId")
    public Object getPermitByRoleId(@RequestParam String roleId) {


        if(Constants.isTree){
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("roleId", roleId);
            LogUtil.controllerLog(this.getClass(), "getPermitByRoleId", jsonObject);

            if (StringUtil.isEmpty(roleId))
                return new ApiResultBean(ApiResultBean.CODE1.PARAM_NULL.getCode(), "角色编号不能为空!");
            else {
                Permit permit = permitService.getPermitsByRole(roleId);
                return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), permit);
            }
        }else{

            List permit = permitService.getPermitsByRoleNoTree(roleId);
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), permit);
        }

    }

    /**
     * 修改角色的菜单资源
     * @param roleId
     * @param permitIds
     * @return
     */
    @ApiOperation("给角色设置菜单")
    @PostMapping("/authc/setPermitForRoleId")
    public Object updatePermitForRoleId(@RequestParam String roleId,@RequestParam String permitIds) {

            permitService.updatePermitForRoleId(roleId,permitIds);
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);

    }

    /**
     * 删除一个菜单资源  逻辑删除
     * @param id
     * @return
     */
    @ApiOperation("删除菜单资源")
    @PostMapping("/authc/deleteOne")
    public Object deleteOne(@RequestParam String id) {
        permitService.deleteOne(id);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 新增一个菜单资源
     * @param permitAddOneParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("添加菜单资源")
    @PostMapping("/authc/addOne")
    public Object addOne(@RequestBody PermitAddOneParam permitAddOneParam) throws CustomException {

        permitService.addOne(permitAddOneParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

}


