package com.store.storemember.controller;

import java.util.Arrays;
import java.util.Map;

import com.store.common.exception.BizCodeEnum;
import com.store.storemember.exception.PhoneExistException;
import com.store.storemember.exception.UsernameExistException;
import com.store.storemember.feign.CouponFeignService;
import com.store.storemember.vo.MemberLoginVo;
import com.store.storemember.vo.MemberRegistgVo;
import com.store.storemember.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.store.storemember.entity.MemberEntity;
import com.store.storemember.service.MemberService;
import com.store.common.utils.PageUtils;
import com.store.common.utils.R;



/**
 * »áÔ±
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:08:59
 */
@RestController
@RequestMapping("storemember/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;
    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R membercoupons = couponFeignService.membercoupons();

        return R.ok().put("member", memberEntity).put("coupons", membercoupons.get("coupons"));
    }
    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistgVo vo) {
        try {
            memberService.regist(vo);
        } catch (UsernameExistException e) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        } catch (PhoneExistException e) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    public R oauthlogin(@RequestBody SocialUser vo) {
        MemberEntity memberEntity = memberService.oauthlogin(vo);
        if (memberEntity != null) {
            //1.登录成功处理
            return R.ok();
        } else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo) {
        MemberEntity ent = memberService.login_old(vo);
        if(ent != null) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getMsg());
        }
    }

}
