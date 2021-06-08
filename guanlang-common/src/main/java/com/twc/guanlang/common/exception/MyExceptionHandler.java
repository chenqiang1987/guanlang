package com.twc.guanlang.common.exception;


import com.twc.guanlang.common.api.ApiResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 拦截controller系统异常
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResultBean jsonHandler(HttpServletRequest request, Exception e) {

        ApiResultBean apiResultBean = new ApiResultBean();
        if (e instanceof CustomException) {

            CustomException customException = (CustomException) e;
            apiResultBean.setCode(customException.getCode());
            apiResultBean.setMsg(customException.getMsg()==null?customException.getMessage():customException.getMsg());
        } else {

            if (e.getMessage().lastIndexOf("Required request body is missing") != -1) {
                apiResultBean.setCode(ApiResultBean.CODE1.PARAM_NULL.getCode());
                apiResultBean.setMsg("请检查参数体!");

            } else {
                apiResultBean.setCode(ApiResultBean.CODE1.SYS_ERROR.getCode());
                apiResultBean.setMsg(ApiResultBean.CODE1.SYS_ERROR.getMsg());
            }

            /**
             * 打印错误日志
             */
           // e.printStackTrace();
            log.error(e.getMessage());
        }
        return apiResultBean;
    }


}
