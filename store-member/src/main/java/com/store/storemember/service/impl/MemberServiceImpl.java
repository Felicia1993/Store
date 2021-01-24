package com.store.storemember.service.impl;

import com.store.common.vo.SocialUser;
import com.store.storemember.dao.MemberLevelDao;
import com.store.storemember.entity.MemberLevelEntity;
import com.store.storemember.exception.PhoneExistException;
import com.store.storemember.exception.UsernameExistException;
import com.store.storemember.vo.MemberRegistgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
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
    @Autowired
    MemberLevelDao memberLevelDao;
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
    @Override
    public void regist(MemberRegistgVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();
        //设置默认等级
        MemberLevelEntity defaultLevel = memberLevelDao.getDefaultLevel();
        entity.setLevelId(defaultLevel.getId());

        //检查用户名和手机号的唯一性,为了让controller感知异常，向上抛异常
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());
        //设置
        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        //密码加密存储
        memberDao.insert(entity);
    }

    @Override
    public void checkUsernameUnique(String userName)  {
        MemberDao memberDao = this.baseMapper;
        Integer username = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (username > 0) {
            throw new UsernameExistException();
        }
    }
    @Override
    public void checkPhoneUnique(String phone) {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(mobile > 0) {
            throw new PhoneExistException();
        }

    }
}