package com.store.storeauthserver.controller;

import com.alibaba.fastjson.TypeReference;
import com.store.common.utils.R;
import com.store.storeauthserver.constant.AuthServerConstant;
import com.store.storeauthserver.feign.MemberFeignService;
import com.store.storeauthserver.vo.SocialUser;
import com.store.storeauthserver.vo.UserLoginVo;
import com.store.storeauthserver.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    RedisTemplate redisTemplate;
    @Autowired
    MemberFeignService memberFeignService;
    /**
     * 发送一个轻轻直接跳转到一个页面
     * Springmvc viewcontroller：将轻轻和页面映射过滤
     * @return
     */
    /*@GetMapping("/login.html")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/regin.html")
    public String regPage() {
        return "regin";
    }*/

    /**
     * todo:重定向携带数据利用session
     * 模拟重定向携带数据
     *
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes) {
        /**
         * .map(fieldError -> {
         *                 String field = fieldError.getField();
         *                 String defaultMessage = fieldError.getDefaultMessage();
         *                 errors.put(field, defaultMessage);
         *                 return
         *             });
         */
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().stream().collect(Collectors.toMap(fieldError -> {
                return fieldError.getField();
            }, fieldError -> {
                return fieldError.getDefaultMessage();
            }));
            //   model.addAttribute("errors");
            redirectAttributes.addFlashAttribute("errors", errors);
            //如果校验出错转发到注册页
            return "forward:/auth.store.com/reg.html";
        }
        //真正注册，调用远程服务
        //1.校验验证码
        String code = vo.getCode();
        String s = (String) redisTemplate.opsForValue().get(AuthServerConstant.StatusEnum.SMS_CODE_CACHE_PREFIX);
        if (!StringUtils.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //验证码通过，真正注册，调用远程服务进行注册
                //删除验证码
                redisTemplate.delete(AuthServerConstant.StatusEnum.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                R regist = memberFeignService.regist(vo);
                if (regist.getCode() == 0) {
                    //成功
                    HashMap<String, String> errors = new HashMap<>();
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:/login.html";
                } else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("mgs", (String) regist.getData("msg", new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute(errors);
                    return "redirect:/reg.html";
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                //校验出错，转发到注册页
                return "redirect:/reg.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            //校验出错，转发到注册页
            return "redirect:/reg.html";
        }
    }
    @PostMapping("/login")
    public String login(SocialUser vo, RedirectAttributes redirectAttributes) {
        //远程登录
        R login = memberFeignService.oauthlogin(vo);
        if (login.getCode() ==0) {
            return "redirect:/http://store.com";
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg", (String) login.getData("msg", new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors", errors);

            return "redirect:/reg.html";
        }

    }
}
