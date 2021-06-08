package com.twc.guanlang.common.api;

import lombok.Data;

import java.util.Map;

/**
 * api响应信息
 */
@Data
public class ApiResultBean {


    public ApiResultBean() {
    }

    /**
     * 系统异常信息
     *
     * @param code
     */
    public ApiResultBean(CODE1 code) {
        this.code = code.code;
        this.msg = code.msg;
    }


    /**
     * 自定义异常信息
     *
     * @param code
     * @param msg
     */
    public ApiResultBean(CODE1 code, String msg) {
        this.code = code.code;
        this.msg = msg;
    }

    public ApiResultBean(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 返回数据体
     *
     * @param code
     * @param data
     */
    public ApiResultBean(int code, Map data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 自定义异常信息
     *
     * @param code
     * @param msg
     */
    public ApiResultBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    /**
     * 返回数据体
     */
    private Object data;

    /**
     * 枚举 错误码
     */
    public enum CODE1 {

        SUCCESS_CODE(200, "success"),

        /**
         * 系统类错误 400+
         */
        SYS_ERROR(400, "系统错误"),

        UNLOGIN(401, "请重新登陆系统"),

        USER_DISABLE(402, "该用户被禁用"),

        /**
         * SQL类错误 500+
         */
        INSERT_ERROR(500, "数据添加失败"),
        USERNAME_UNIQUE_ERROR(501, "用户名已经存在"),
        PASSWORD_ERROR(502, "密码错误"),
        USERNAME_ERROR(503, "用户名错误"),
        NULL_DATA(504, "未查询到数据"),
        ROLE_NAME_UNIQUE_ERROR(505, "角色编号已经存在"),
        ROLE_NICK_NAME_UNIQUE_ERROR(506, "角色名称已经存在"),

        /**
         * 参数类错误 600+
         */
        PARAM_NULL(601, "不能为空"),
        PARAM_LENGTH(602, "长度区间为start-end"),
        TWICE_PASSWORD_NOT_SWITCH(603, "两次密码输入不一致"),
        OLD_PASSWORD_ERROR(603, "原密码输入错误"),
        PARAM_ERROR(604, "参数错误"),

        /**
         * 权限类错误 700+
         */
        PERMIT_DENIED(701, "权限不足");

        /**
         * 错误码
         */
        private int code;
        /**
         * 错误信息
         */
        private String msg;
        CODE1(int s, String i) {
            this.code = s;
            this.msg = i;
        }

        public int getCode() {
            return this.code;
        }
        public String getMsg() {
            return this.msg;
        }
    }

}
