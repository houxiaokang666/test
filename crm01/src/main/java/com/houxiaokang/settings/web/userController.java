package com.houxiaokang.settings.web;

import com.houxiaokang.common.Util.DatesimplDateFormat;
import com.houxiaokang.common.constant.constants;
import com.houxiaokang.common.domain.Result;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.settings.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class userController {

    @Autowired
    private UserServer userServer;

    @RequestMapping("/settings/qx/user/tologin.do")
    public String tologin() {
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map hashMap = new HashMap();
        hashMap.put("loginact", loginAct);
        hashMap.put("password", loginPwd);
        User user = userServer.queryUserByLoginActAndpsw(hashMap);
        Date date = new Date();
        String newdate = DatesimplDateFormat.DatesimplDateFormatoString(date);
        Result result = null;
        if (user == null) {
            return result = new Result(constants.RESULT_CODE_FAIL, "用户名或密码错误");
        } else if (!(user.getExpireTime().compareTo(newdate) > 0)) {
            return result = new Result(constants.RESULT_CODE_FAIL, "用户已到期");
        } else if (constants.RESULT_CODE_FAIL.equals(user.getLockState())) {
            return result = new Result(constants.RESULT_CODE_FAIL, "账号被锁定");
        } else if (!(user.getAllowIps().contains(request.getRemoteAddr()))) {
            return result = new Result(constants.RESULT_CODE_FAIL, "ip地址受限");
        } else {
            //如果需要记住密码，则往外写cookie
            if ("true".equals(isRemPwd)) {
                Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                c1.setMaxAge(10 * 24 * 60 * 60);
                c1.setPath("/");
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                c2.setMaxAge(10 * 24 * 60 * 60);
                c2.setPath("/");
                response.addCookie(c2);
            } else {
                //把没有过期cookie删除
                Cookie c1 = new Cookie("loginAct", "1");
                c1.setMaxAge(0);
                c1.setPath("/");
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd", "1");
                c2.setMaxAge(0);
                c2.setPath("/");
                response.addCookie(c2);
            }
            session.setAttribute(constants.SESSION_USER, user);
        }
        return result = new Result(constants.RESULT_CODE_SUCCESS);
    }

    @RequestMapping("/settings/qx/user/exit.do")
    public String exit(HttpSession session, HttpServletResponse response) {
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        c1.setPath("/");
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        c2.setPath("/");
        response.addCookie(c2);
        session.invalidate();
        return "redirect:/";
    }
}
