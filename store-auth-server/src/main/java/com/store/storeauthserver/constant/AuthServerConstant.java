package com.store.storeauthserver.constant;

public class AuthServerConstant {
    //
    public enum StatusEnum{
        SMS_CSMS_CODE_CHACHE_PREFIODE_CHACHE_PREFIX(0,"新建");
        private int code;
        private String msg;

        StatusEnum(int code, String msg) {
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


}
