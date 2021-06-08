package com.twc.guanlang.common.log;


import com.twc.guanlang.common.json.TwcJsonUtil;
import com.twc.guanlang.common.time.TwcTimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * 切面类 @Aspect
 * 注入ioc容器 @Component
 */
@Slf4j
//@Aspect
//@Component
public class LogUtil {

    public static void controllerLog(Class c, String method, Object arg) {

        log.info(TwcTimeUtil.reverseTime2String(new Date()) + "#" + c.getName() + "#" + method + "#" + TwcJsonUtil.reverseObj2JsonObj(arg).toString());
    }



//    /**
//     * 定义连接点
//     */
//    @Pointcut("execution(* com.twc.guanlang.controller.*.*(..)))")
//    public void controller() {
//    }
//
//
//    @Around("controller()")
//    public void logParam(ProceedingJoinPoint point) throws Throwable {
//
//        log.info(point.getTarget().getClass().getName()+"#"+point.getArgs());
//        point.proceed();
//    }

}
