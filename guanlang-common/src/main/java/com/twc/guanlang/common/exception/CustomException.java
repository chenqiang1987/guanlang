package com.twc.guanlang.common.exception;


import com.twc.guanlang.common.api.ApiResultBean;
import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class CustomException extends Exception {


    private String msg;

    private int code;

    public CustomException() {

        super();
    }

    public CustomException(String msg) {
        super(msg);
    }

//    public CustomException(int code) {
//
//        this.code = code;
//        this.msg = (String) ApiResultBean.map.get(code);
//
//    }

    public CustomException(ApiResultBean.CODE1 code) {

        this.code = code.getCode();
        this.msg = code.getMsg();

    }

    public CustomException(int code, String msg) {

        this.code = code;
        this.msg = msg;

    }

}
