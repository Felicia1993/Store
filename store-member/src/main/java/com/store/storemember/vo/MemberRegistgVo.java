package com.store.storemember.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
@Data
public class MemberRegistgVo {
    private String userName;
    private String password;
    private String phone;
}
