package com.store.storemember.service.impl;

import com.store.common.vo.SocialUser;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storemember.dao.MemberDao;
import com.store.storemember.entity.MemberEntity;
import com.store.storemember.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public MemberEntity login(SocialUser socialUser) {
        //登录和注册合并逻辑
        socialUser.getUid();
        MemberEntity memberEntity = new MemberEntity();
        return memberEntity;
    }

}