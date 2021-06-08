package com.twc.guanlang.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.twc.guanlang.common.MybatisExampleUtil;
import com.twc.guanlang.common.PasswordHelper;
import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.str.TwcStringUtil;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.entity.user.User2Role;
import com.twc.guanlang.mapper.entity.Repair2UserMapper;
import com.twc.guanlang.mapper.entity.User2RoleMapper;
import com.twc.guanlang.mapper.entity.UserMapper;
import com.twc.guanlang.param.user.*;

import com.twc.guanlang.common.annotation.TwcAnnotationParser;
import com.twc.guanlang.vo.ExpertVO;
import com.twc.guanlang.vo.PageData;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService implements BaseService {


    @Resource
    private UserMapper userMapper;

    public SystemUser findUserByName(String userName) {


        Example example = new Example(SystemUser.class);
        JSONObject jsonobject = null;


        Map map = new HashMap<>();

        Iterator iterator = jsonobject.keys();
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            String value = jsonobject.get(key) + "";
        }
        map.put("userName", userName);

        return userMapper.selectOneByExample(MybatisExampleUtil.setExampleForObject(SystemUser.class, map));
    }


    @Autowired
    private PasswordHelper passwordHelper;


    @Autowired
    private User2RoleMapper user2RoleMapper;

    @Transactional
    public boolean registerUser(RegisterParam registerParam) throws Exception {


        SystemUser user = new SystemUser();
        TwcAnnotationParser.parseParamCheck(registerParam);
        UserSelectOneParam userSelectOnePara = new UserSelectOneParam();
        userSelectOnePara.setUserName(registerParam.getUserName());
        List list = selectOne(userSelectOnePara);
        if (list != null && list.size() > 0)
            throw new CustomException(ApiResultBean.CODE1.USERNAME_UNIQUE_ERROR);
        if (!registerParam.getPassword().equals(registerParam.getConfirmPassword()))
            throw new CustomException(ApiResultBean.CODE1.TWICE_PASSWORD_NOT_SWITCH);

        user.setUserName(registerParam.getUserName());
        user.setPassword(registerParam.getPassword());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setTelPhone(registerParam.getTelPhone());
        user.setEnable(BaseEntity.ENABLE.ENABLE);
        passwordHelper.encryptPassword(user);
        int rs = userMapper.insertUseGeneratedKeys(user);

        for (int roleId : registerParam.getRoleIds()) {

            User2Role user2Role = new User2Role();
            user2Role.setUserId(user.getId());
            user2Role.setRoleId(Long.parseLong(roleId+""));
            user2Role.setCreateTime(new Date());
            user2Role.setUpdateTime(new Date());
            user2Role.setEnable(BaseEntity.ENABLE.ENABLE);
            user2RoleMapper.insert(user2Role);
        }

        return rs == 1 ? true : false;
    }

    public void deleteOne(SystemUser systemUser) {

//        systemUser.setEnable(BaseEntity.ENABLE.DISABLE);
//        super.deleteOne(userMapper, systemUser);
    }


    @Transactional
    public void deleteUser(String userId) {

        SystemUser systemUser = userMapper.selectByPrimaryKey(userId);
        systemUser.setEnable(BaseEntity.ENABLE.DISABLE);
        userMapper.updateByPrimaryKey(systemUser);
    }



    public Object deleteOne(JSONObject o) {

        String id = o.get("userId") + "";
        SystemUser systemUser = userMapper.selectByPrimaryKey(id);
        systemUser.setEnable(BaseEntity.ENABLE.DISABLE);
        userMapper.updateByPrimaryKey(systemUser);
        return null;
    }

    public Object modifyOne(JSONObject o) {
        return null;
    }

    public Object addOne(JSONObject o) {
        return null;
    }

    public List selectOne(UserSelectOneParam userSelectOneParam) throws CustomException {


        String telPhone = userSelectOneParam.getTelPhone();
        String id = userSelectOneParam.getId();
        String userName = userSelectOneParam.getUserName();
        Example example = new Example(SystemUser.class);

        if (TwcStringUtil.isNotEmpty(telPhone)) {
            example.createCriteria().andEqualTo("telPhone", telPhone.trim());
        } else if (TwcStringUtil.isNotEmpty(id))
            example.createCriteria().andEqualTo("id", id.trim());
        else if (TwcStringUtil.isNotEmpty(userName))
            example.createCriteria().andEqualTo("userName", userName.trim());
        else
            throw new CustomException(ApiResultBean.CODE1.PARAM_NULL.getCode(), "参数缺失");

        return userMapper.selectByExample(example);
    }

    public PageData selectPage(UserPageParam userPageParam) {
        Page<SystemUser> page = PageHelper.startPage(userPageParam.getPageNum(), userPageParam.getPageSize(), "update_time desc");
        Example example = new Example(SystemUser.class);
        Example.Criteria criteria = example.createCriteria();
        if (TwcStringUtil.isNotEmpty(userPageParam.getTelPhone())) {

            criteria.andLike("telPhone", userPageParam.getTelPhone());
        }
        if (TwcStringUtil.isNotEmpty(userPageParam.getUserName())) {

            criteria.andLike("userName", userPageParam.getUserName());
        }
        if (TwcStringUtil.isNotEmpty(userPageParam.getNickName())) {

            criteria.andLike("nickName", userPageParam.getNickName());
        }

//        if (TwcStringUtil.isNotEmpty(userPageParam.getRoleId())) {
//
//            criteria.andLike("roleId", userPageParam.getRoleId());
//        }

        List list = userMapper.selectUserPage(userPageParam);
        return new PageData(page);
    }

    public Object addOrModifyOne(UseraddOrModifyOneParam useraddOrModifyOneParam) throws CustomException {

        TwcAnnotationParser.parseParamCheck(useraddOrModifyOneParam);
        SystemUser user = null;
        int rs = -1;
        boolean isAdd = true;
        if (TwcStringUtil.isEmpty(useraddOrModifyOneParam.getId())) {

            user = new SystemUser();
            UserSelectOneParam userSelectOneParam = new UserSelectOneParam();
            userSelectOneParam.setUserName(useraddOrModifyOneParam.getUserName());
            List list = selectOne(userSelectOneParam);
            if (list != null && list.size() > 0)
                throw new CustomException(ApiResultBean.CODE1.USERNAME_UNIQUE_ERROR);
            if (!useraddOrModifyOneParam.getPassword().equals(useraddOrModifyOneParam.getConfirmPassword()))
                throw new CustomException(ApiResultBean.CODE1.TWICE_PASSWORD_NOT_SWITCH);
            user.setUserName(useraddOrModifyOneParam.getUserName());
            user.setNickName(useraddOrModifyOneParam.getNickName());
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setPassword(useraddOrModifyOneParam.getPassword());
            user.setTelPhone(useraddOrModifyOneParam.getTelPhone());
            user.setEnable(BaseEntity.ENABLE.ENABLE);
            passwordHelper.encryptPassword(user);
        } else {
            isAdd = false;
            UserSelectOneParam userSelectOneParam = new UserSelectOneParam();
            userSelectOneParam.setId(useraddOrModifyOneParam.getId());
            List list = selectOne(userSelectOneParam);
            if (list == null || list.size() == 0)
                throw new CustomException(ApiResultBean.CODE1.NULL_DATA.getCode(), "用户不存在");
            user = (SystemUser) list.get(0);

        }


        if (isAdd) {
            rs = userMapper.insertUseGeneratedKeys(user);
        } else {
            //修改了用户名
            if (!user.getUserName().equalsIgnoreCase(useraddOrModifyOneParam.getUserName())) {

                UserSelectOneParam userSelectOneParam = new UserSelectOneParam();
                userSelectOneParam.setId(user.getUserName() + "");
                List list = selectOne(userSelectOneParam);
                if (list != null && list.size() > 0)
                    throw new CustomException(ApiResultBean.CODE1.USERNAME_UNIQUE_ERROR.getCode(), useraddOrModifyOneParam.getUserName() + "已经存在");
            }

            user.setUserName(useraddOrModifyOneParam.getUserName());
            user.setTelPhone(useraddOrModifyOneParam.getTelPhone());
            user.setNickName(useraddOrModifyOneParam.getNickName());
            user.setUpdateTime(new Date());
            rs = userMapper.updateByPrimaryKey(user);

            //清空原先角色关系
            Example example = new Example(User2Role.class);
            example.createCriteria().andEqualTo("userId", user.getId());
            user2RoleMapper.deleteByExample(example);
        }

        for (int roleId : useraddOrModifyOneParam.getRoleIds()) {

            User2Role user2Role = new User2Role();
            user2Role.setUserId(user.getId());
            user2Role.setRoleId(Long.parseLong(roleId+""));
            user2Role.setCreateTime(new Date());
            user2Role.setUpdateTime(new Date());
            user2Role.setEnable(BaseEntity.ENABLE.ENABLE);
            user2RoleMapper.insert(user2Role);
        }

        return rs;
    }

    @Transactional
    public void resetPassword(ResetPasswordParam resetPasswordParam) {

        String userId = resetPasswordParam.getUserId();
        SystemUser systemUser = userMapper.selectByPrimaryKey(userId);
        systemUser.setPassword(SystemUser.DEFALUST_PASSWORD);
        passwordHelper.encryptPassword(systemUser);
        userMapper.updateByPrimaryKey(systemUser);
    }


    @Transactional
    public void updatePassword(UpdatePasswordParam updatePasswordParam, SystemUser systemUser) throws CustomException {


        if (!updatePasswordParam.getConfirmPassword().trim().equalsIgnoreCase(updatePasswordParam.getNewPassword().trim()))
            throw new CustomException(ApiResultBean.CODE1.TWICE_PASSWORD_NOT_SWITCH);
        String userEncrypt = systemUser.getPassword();

        String checkEncrypt = PasswordHelper.encryptString(updatePasswordParam.getOldPassword(), systemUser.getCredentialsSalt());
        if (!userEncrypt.equalsIgnoreCase(checkEncrypt))
            throw new CustomException(ApiResultBean.CODE1.OLD_PASSWORD_ERROR);

        String newEncrypt = PasswordHelper.encryptString(updatePasswordParam.getNewPassword(), systemUser.getCredentialsSalt());
        systemUser.setPassword(newEncrypt);
        systemUser.setUpdateTime(new Date());
        userMapper.updateByPrimaryKey(systemUser);


        //刷新shiro
        UsernamePasswordToken token = new UsernamePasswordToken(systemUser.getUserName(), updatePasswordParam.getNewPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);

        } catch (IncorrectCredentialsException ice) {
            throw new CustomException(ApiResultBean.CODE1.PASSWORD_ERROR);
        } catch (UnknownAccountException uae) {
            throw new CustomException(ApiResultBean.CODE1.USERNAME_ERROR);
        }
    }


    public List<ExpertVO> allExperts(String userName) {

        Example example = new Example(SystemUser.class);
        JSONObject jsonobject = null;
        example.createCriteria().andEqualTo("name", "expert");
        List<SystemUser> systemUsers = userMapper.selectByExample(example);
        for (SystemUser systemUser : systemUsers) {

            ExpertVO expertVO = new ExpertVO();
            expertVO.setNickName(systemUser.getNickName());
            expertVO.setUserName(systemUser.getUserName());
            expertVO.setTelPhone(systemUser.getTelPhone());


        }

        return null;
    }

    /**
     * 用户是否在线判断逻辑
     *
     * @param userName
     * @return
     */

    public boolean userIsOnline(String userName) {


        return false;
    }


    @Autowired
    private Repair2UserMapper repair2UserMapper;

    @Transactional
    public void updateFaultForUser( Long userId,  String faults)  {

        String[] ids=faults.split(",");
        Example example=new Example(Repair2User.class);
        example.createCriteria().andEqualTo("userId");
        repair2UserMapper.deleteByExample(example);

        if(!StringUtil.isEmpty(faults)){

            for(String id:ids){

                Repair2User repair2User=new Repair2User();
                repair2User.setRepairCode(id);
                repair2User.setUserId(userId);
                repair2UserMapper.insert(repair2User);
            }
        }

    }
}
