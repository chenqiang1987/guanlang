package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.vo.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


import java.util.HashMap;
import java.util.Map;


/**
 * 定义常用的控制器方法
 * @author chenqiang
 */
public abstract class BaseController {

    protected ApiResultBean returnPageData(PageData pageData) {

        if (pageData.getData() == null || pageData.getData().size() == 0) {
            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA);
        }
        Map map = new HashMap();
        map.put("page", pageData);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), map);
    }

    public static SystemUser getLoginUserByShiro() {

        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getSession().getAttribute("user");
        return user;
    }


}
