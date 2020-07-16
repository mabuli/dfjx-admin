/**
 * Copyright 2018 权限管理源 http://www.xxx.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.dfjx.module.sys.controller;


import io.dfjx.config.SystemConfig;
import io.dfjx.module.sys.service.SysRoleService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.dfjx.common.util.R;
import io.dfjx.module.sys.shiro.ShiroUtils;

/**
 * 登录相关
 * 
 * @author mazong
 * @email mazong@gmail.com
 * @date 2019年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SystemConfig systemConfig;

    private static final String SYSTEM_PROFILE = "dev";

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    // thp改 去除验证码
    // public R login(String username, String password, String captcha) {
    public R login(String username, String password) {
        // String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        // if(!captcha.equalsIgnoreCase(kaptcha)){
        // return R.error("验证码不正确");
        // }

        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok();
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();

        return "redirect:login.html";
	}

    private static final String URL_ADMIN_INDEX = "index.html";//"index";//
    private static final String URL_USER_INDEX = "app.html";
    private static final String ROLE_TYPE_USER = "user";
}
