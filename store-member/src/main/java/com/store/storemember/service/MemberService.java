package com.store.storemember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.common.vo.SocialUser;
import com.store.storemember.entity.MemberEntity;

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

    MemberEntity login(SocialUser vo);
}

