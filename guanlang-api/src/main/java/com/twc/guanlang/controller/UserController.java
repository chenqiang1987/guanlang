package com.twc.guanlang.controller;

import com.twc.guanlang.common.api.ApiResultBean;

import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.mapper.entity.Repair2UserMapper;
import com.twc.guanlang.param.BaseParam;
import com.twc.guanlang.param.user.*;
import com.twc.guanlang.service.RoleService;
import com.twc.guanlang.service.UserService;
import com.twc.guanlang.common.annotation.TwcAnnotationParser;
import com.twc.guanlang.common.log.LogUtil;
import com.twc.guanlang.vo.PageData;
import com.twc.guanlang.vo.RoleVO;
import com.twc.guanlang.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")

@Api(value = "用户控制器")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "未登陆跳转", hidden = true)
    @GetMapping("unloginHandler")
    public Object unloginHandler() {
        return new ApiResultBean(ApiResultBean.CODE1.UNLOGIN);
    }

    /**
     * 用户名密码登陆
     *
     * @param loginByUserNameParam
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "使用用户名登陆系统", httpMethod = "POST")
    @PostMapping("loginByUserName")
    public Object loginByUserName(@RequestBody LoginByUserNameParam loginByUserNameParam) throws Exception {

        LogUtil.controllerLog(UserController.class, "loginByUserName", loginByUserNameParam);
        TwcAnnotationParser.parseParamCheck(loginByUserNameParam);
        UsernamePasswordToken token = new UsernamePasswordToken(loginByUserNameParam.getUserName(), loginByUserNameParam.getPassword());

        Subject subject = SecurityUtils.getSubject();
        try {
           Session session= subject.getSession();
//            session.setAttribute("user",loginByUserNameParam.getUserName());
            subject.login(token);

        } catch (IncorrectCredentialsException ice) {
            return new ApiResultBean(ApiResultBean.CODE1.PASSWORD_ERROR);
        } catch (UnknownAccountException uae) {
            return new ApiResultBean(ApiResultBean.CODE1.USERNAME_ERROR);
        }

        SystemUser user = null;
        try {
            UserSelectOneParam userSelectOneParam = new UserSelectOneParam();
            userSelectOneParam.setUserName(loginByUserNameParam.getUserName());
            List list = userService.selectOne(userSelectOneParam);
            if (list == null || list.size() == 0) {
                return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA);
            } else {
                user = (SystemUser) list.get(0);
                if (user.getEnable().equalsIgnoreCase(BaseEntity.ENABLE.DISABLE))
                    return new ApiResultBean(ApiResultBean.CODE1.USER_DISABLE);
                subject.getSession().setAttribute("user", user);
                return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
            }
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR);
        }
    }

    /**
     * 用户注册
     *
     * @param registerParam
     * @return
     * @throws Exception
     */
    @PostMapping("register")
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    public Object register(@RequestBody RegisterParam registerParam) throws Exception {

        LogUtil.controllerLog(UserController.class, "register", registerParam);
        if (userService.registerUser(registerParam))
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
        else
            return new ApiResultBean(ApiResultBean.CODE1.INSERT_ERROR);
    }

    /**
     * 注销登陆
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "注销登陆", httpMethod = "POST")
    @PostMapping("logOut")
    public Object logOut() throws Exception {

        LogUtil.controllerLog(UserController.class, "logOut", null);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);

    }

    /**
     * 查询单个人员信息
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询单个人员信息", httpMethod = "POST")
    @PostMapping("getUserMsg")
    public Object getUserMsg(@RequestBody JSONObject jsonObject) throws Exception {

        LogUtil.controllerLog(UserController.class, "getUserMsg", jsonObject);
        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getSession().getAttribute("user");
        if (user == null)
            return new ApiResultBean(ApiResultBean.CODE1.UNLOGIN);

        String userName = jsonObject.get("userName") + "";
        if (StringUtil.isEmpty(userName.trim()))
            return new ApiResultBean(ApiResultBean.CODE1.PARAM_NULL, "请填写用户名!");
        SystemUser systemUser = userService.findUserByName(userName.trim());
        if (systemUser == null)
            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA);


        List<RoleVO> list = roleService.findRolesByUserId(user.getId());

        UserVO userVO = new UserVO();
        userVO.setRoles(list);
        BeanUtils.copyProperties(systemUser, userVO);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), userVO);
    }

    /**
     * 删除单个用户
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除单个用户", httpMethod = "POST")
    @PostMapping("/authc/deleteUser")
    public Object deleteUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId) {

        userService.deleteUser(userId);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 新增一个用户
     * @param request
     * @param response
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "新增一个用户", httpMethod = "POST")
    @ApiParam(name = "jsonObject", value = "新增用户表单", required = true)
    public Object addOne(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject) {


        return null;
    }

    /**
     * 获取单个用户信息
     * @param request
     * @param response
     * @param userSelectOneParam
     * @return
     * @throws CustomException
     */
    @ApiOperation(value = "获取单个用户信息", httpMethod = "POST")
    @PostMapping("/authc/selectOne")
    public Object selectOne(HttpServletRequest request, HttpServletResponse response, @RequestBody UserSelectOneParam userSelectOneParam) throws CustomException {

        List list = (List) userService.selectOne(userSelectOneParam);
        if (list == null || list.size() == 0) {
            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA);
        } else {
            SystemUser systemUser = (SystemUser) list.get(0);
            List<RoleVO> roles = roleService.findRolesByUserId(systemUser.getId());
            UserVO userVO = new UserVO();
            userVO.setRoles(roles);
            BeanUtils.copyProperties(systemUser, userVO);
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), userVO);
        }
    }

    /**
     * 获取用户列表
     * @param request
     * @param response
     * @param userPageParam
     * @return
     */
    @ApiOperation(value = "分页获取用户列表", httpMethod = "POST")
    @PostMapping("/authc/selectPage")
    public Object selectPage(HttpServletRequest request, HttpServletResponse response, @RequestBody UserPageParam userPageParam) {

        PageData pageData = userService.selectPage(userPageParam);
        List<UserVO> userVOS = new ArrayList<>();
        List<SystemUser> list = pageData.getData();
        if (list != null && list.size() > 0) {
            list.forEach((e) -> {
                List<RoleVO> roles = roleService.findRolesByUserId(e.getId());
                UserVO userVO = new UserVO();
                userVO.setRoles(roles);
                BeanUtils.copyProperties(e, userVO);

//                userVO.setId(e.getId());
                userVOS.add(userVO);
            });
        }
        pageData.setData(userVOS);
        return returnPageData(pageData);

    }

    /**
     *新增或者修改单个用户
     * @param request
     * @param response
     * @param useraddOrModifyOneParam
     * @return
     * @throws CustomException
     */
    @ApiOperation(value = "新增或者修改单个用户", httpMethod = "POST")
    @PostMapping("/authc/addOrModifyOne")
    public Object addOrModifyOne(HttpServletRequest request, HttpServletResponse response, @RequestBody UseraddOrModifyOneParam useraddOrModifyOneParam) throws CustomException {

        userService.addOrModifyOne(useraddOrModifyOneParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 重置密码
     *
     * @return
     * @throws CustomException
     */
    @ApiOperation(value = "重置密码", httpMethod = "POST")
    @PostMapping("authc/resetPassword")
    public Object resetPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody ResetPasswordParam resetPasswordParam) throws CustomException {

        SystemUser systemUser = BaseController.getLoginUserByShiro();
        if (!systemUser.getUserName().equalsIgnoreCase("admin")) {
            return new ApiResultBean(ApiResultBean.CODE1.PERMIT_DENIED);
        }
        userService.resetPassword(resetPasswordParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 修改密码
     *
     * @return
     * @throws CustomException
     */
    @ApiOperation(value = "修改用户密码", httpMethod = "POST")
    @PostMapping("authc/updatePassword")
    public Object updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdatePasswordParam updatePasswordParam) throws CustomException {

        userService.updatePassword(updatePasswordParam, BaseController.getLoginUserByShiro());
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    public static void main(String s[]) {

        SystemUser systemUser = new SystemUser();
        JSONObject o = new JSONObject();
        o.element("userName", "value");
        SystemUser systemUser1 = (SystemUser) JSONObject.toBean(o, SystemUser.class);
        System.out.println(systemUser1);

        test(new LoginByUserNameParam());
        UserController userController = new UserController();

    }

    private static void test(BaseParam baseParam) {

    }

    /**
     * 给专家配置故障
     * @param userId
     * @param faults
     * @return
     * @throws CustomException
     */
    @ApiOperation(value = "给专家配置故障", httpMethod = "POST")
    @PostMapping("authc/updateFaultForUser")
    public Object updateFaultForUser(@RequestParam String userId, @RequestParam String faults) throws CustomException {

        userService.updateFaultForUser(Long.parseLong(userId), faults);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }
}