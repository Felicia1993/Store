package com.store.storemember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storemember.entity.MemberEntity;
import com.store.storemember.exception.PhoneExistException;
import com.store.storemember.exception.UsernameExistException;
import com.store.storemember.vo.MemberLoginVo;
import com.store.storemember.vo.MemberRegistgVo;
import com.store.storemember.vo.SocialUser;

import java.util.Map;

/**
 * »áÔ±
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:08:59
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MemberEntity oauthlogin(SocialUser vo);

    MemberEntity login_old(MemberLoginVo vo);

    void regist(MemberRegistgVo vo);

    void checkUsernameUnique(String userName) throws UsernameExistException;

    void checkPhoneUnique(String phone) throws PhoneExistException;
}

