package com.store.common.exception;
/**
 * 错误码列表：
 * 10：通用
 *  001参数格式校验
 *  002 短信验证码频率太高
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14：物流
 * 15：用户
 */
public enum  BizCodeEnum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(100001,"参数格式校验失败"),
    LOGINACCT_PASSWORD_INVAILD_EXCEPTION(100003,"用户密码输入错误"),
    SMS_CODE_EXCEPTION(10002,"验证码获取频率太高，稍后再试"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    USER_EXIST_EXCEPTION(15001, "用户存在异常"),
    PHONE_EXIST_EXCEPTION(15002, "手机号存在异常"),
    NO_STOCK_EXCEPTION(21000,"商品库存不足"),
    LOGINACCT_PASSWORD_INVALID_EXCEPTION(150002, "手机号存在");

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
