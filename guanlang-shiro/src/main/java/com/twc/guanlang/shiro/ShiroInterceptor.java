package com.twc.guanlang.shiro;//package com.twc.guanlang.shiro;
//
//
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class ShiroInterceptor    implements HandlerInterceptor {
//
//
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //1.先判断请求url是否有 权限注解  有则代表需要做权限校验 无则直接放行
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        PermissionService permissionService = handlerMethod.getMethodAnnotation(PermissionService.class);
//        if (permissionService == null) {
//            return true;
//        }
//
//        //2.走到这里 说明需要对管理员进行操作权限的校验
//        //a.查询当前用户
//        try {
//            UserInfo userInfo = operateuRedisUtil.getCurrentUser();
//
//            //b.添加用户认证信息(使用的是shiro的验证功能)
//            Subject subject = SecurityUtils.getSubject();
//            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userInfo.getUserName(), userInfo.getUserPwd());
//
//            //登陆
//            subject.login(usernamePasswordToken);
//
////        System.out.println("是否认证:"+subject.isAuthenticated());
//
//            //验证角色
////        subject.checkRole("admin");
//
//            //验证权限
//            String url = request.getRequestURI();
//
//            subject.checkPermission(url);
//        } catch (Exception e) {
//            logger.error("权限校验失败!");
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json;charset=UTF-8");
//            R r = new R(R.CODE_NO_PERMISSION, "用户权限校验失败!");
//            response.getWriter().write(JsonUtil.toJson(r));
//            response.getWriter().flush();
//            return false;
//        }
//
//        return true;
//    }
//
//}
