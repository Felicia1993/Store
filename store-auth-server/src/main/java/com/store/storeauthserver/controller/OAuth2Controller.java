package com.store.storeauthserver.controller;

import com.alibaba.fastjson.JSON;
import com.store.common.utils.HttpUtils;
import com.store.storeauthserver.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.HashMap;

/**
 * 处理社交登录请求
 */
public class OAuth2Controller {
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code) throws IOException {
        //1.根据code获取accesstoken
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", "");//id待申请
        map.put("client_secret", "");//secret待申请
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);


        HttpResponse response = HttpUtils.doPost("api.weibo.com", "/oauth2.0/access_token", "post", null, null, map);
        //2.处理
        if (response.getStatusLine().getStatusCode() == 200){
                //获取到了accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //知道当前是哪个社交用户
            /**
             * 1)当前用户是第一次进网站，自动注册进来（为当前用户生成一个会员信息账号，以后这个社交账号就指定这个会员）
             * 登录或者注册这个社交用户
             */
        } else {
            return "redirect:http://gulimall.com";
        }
        //2.登录成功就跳回首页
        return "redirect:http://gulimall.com";
    }
}
