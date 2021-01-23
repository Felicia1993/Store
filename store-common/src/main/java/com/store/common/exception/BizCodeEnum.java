package com.store.common.exception;

public enum  BizCodeEnum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(100001,"参数格式校验失败"),
    PRODUCT_UP_EXCEPTION(100002,"产品上架失败"),
    LOGINACCT_PASSWORD_INVAILD_EXCEPTION(100003,"用户密码输入错误");
    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
