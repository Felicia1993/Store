package com.store.storeauthserver.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MemberRespVo implements Serializable {
    private Long id;
    private Long levelId;
    private String userName;
    private String nickName;
    private String mobile;
    private String email;
    private String header;
    private Integer gender;
    private Date birth;
    private String city;
    private String job;
    private String sign;
}
