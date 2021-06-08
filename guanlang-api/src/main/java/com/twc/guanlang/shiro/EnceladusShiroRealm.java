package com.twc.guanlang.shiro;

import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.param.user.UserSelectOneParam;
import com.twc.guanlang.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EnceladusShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;


    /**
     * 人员的角色和权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();

        SystemUser user = userService.findUserByName(username);

//        for (Role role : userService.findRolesByUserId(user.getId())) {
//            authorizationInfo.addRole(role.getName());
//
//            for (SysPermission permission : role.getPermissions()) {
//                authorizationInfo.addStringPermission(permission.getName());
//            }
//        }
        return authorizationInfo;
    }


    /**
     * 验证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        SystemUser user = null;
        try {

            UserSelectOneParam userSelectOneParam = new UserSelectOneParam();
            userSelectOneParam.setUserName(username);
            List list = userService.selectOne(userSelectOneParam);

            if (list == null || list.size() == 0) {
                return null;
            } else
                user = (SystemUser) list.get(0);
        } catch (CustomException e) {
            e.printStackTrace();
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
        return authenticationInfo;

    }

}
