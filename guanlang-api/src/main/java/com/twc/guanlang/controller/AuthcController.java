package com.twc.guanlang.controller;

import com.twc.guanlang.entity.user.SystemUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 需要指定权限可以访问的地址
 * authc：所有已登陆用户可访问
 * <p>
 * roles：有指定角色的用户可访问，通过[ ]指定具体角色，这里的角色名称与数据库中配置一致
 * <p>
 * perms：有指定权限的用户可访问，通过[ ]指定具体权限，这里的权限名称与数据库中配置一致
 * <p>
 * anon：所有用户可访问，通常作为指定页面的静态资源时使用
 * @author chenqiang
 */
@RestController
@RequestMapping("authc")
public class AuthcController {

    @GetMapping("index")
    public Object index() {
        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getSession().getAttribute("user");
        return user == null ? "null user" : user.toString();
    }

    @GetMapping("admin")
    public Object admin() {
        return "Welcome Admin";
    }

    // delete
    @GetMapping("removable")
    public Object removable() {
        return "removable";
    }

    // insert & update
    @GetMapping("renewable")
    public Object renewable() {
        return "renewable";
    }
}
