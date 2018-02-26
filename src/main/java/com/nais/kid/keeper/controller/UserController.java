package com.nais.kid.keeper.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.nais.domain.User;
import com.nais.kid.keeper.vo.UserVo;
import com.nais.response.GeneralResponse;
import com.nais.service.UserService;
import com.nais.vo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("list")
    public String entry() {
        return "user/list";
    }

    @PostMapping()
    @ResponseBody
    public GeneralResponse<String> addUser(UserVo userVo) {
        if (userVo == null) {
            return GeneralResponse.failure("请填写用户信息");
        }

        LOGGER.info("receive addUser request: {}", JSON.toJSONString(userVo));
        String result = checkUserVo(userVo);
        if (!result.equals("")) {
            return GeneralResponse.failure(result);
        }

        User user = new User();
        try {
            user.setBirthday(dateTimeFormat.parse(userVo.getBirthday()));
        } catch (Exception e) {
            LOGGER.info("", e);
            return GeneralResponse.failure("请填写正确的生日信息");
        }
        user.setName(userVo.getName());
        user.setSex(userVo.getSex());
        user.setEduLevel(userVo.getEduLevel());
        user.setTel(userVo.getTel());
        com.nais.vo.UserVo returnDate = userService.addUser(user, 123L);
        LOGGER.info("add user {}", returnDate);
        if (returnDate != null) {
            return GeneralResponse.success("success");
        }else{
            return GeneralResponse.failure("服务器开小差了");
        }
    }

    @GetMapping()
    @ResponseBody
    public PageVo<com.nais.vo.UserVo> getUserById(String tel, String name, Integer sex, Integer eduLevel,
                                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) {
        return userService.getUserListByPage(tel, name, sex, eduLevel, page, rows);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public GeneralResponse<Void> delUser(Long uid) {
        LOGGER.info("delete user {}", uid);
        if (uid == null || uid < 0) {
            return GeneralResponse.failure("非法用户id");
        }
        try {
            // TODO
        } catch (Exception e) {
            LOGGER.info("删除用户{}发生异常" + e, uid);
        }
        return GeneralResponse.success(null);
    }

    private String checkUserVo(UserVo userVo) {
        if (Strings.isNullOrEmpty(userVo.getName())) {
            return "请填写用户名";
        }
        if (userVo.getSex() == null) {
            return "请选择性别";
        }
        if (Strings.isNullOrEmpty(userVo.getTel())) {
            return "请填写联系电话";
        }
        if (Strings.isNullOrEmpty(userVo.getBirthday())) {
            return "请填写生日信息";
        }
        return "";
    }
}
