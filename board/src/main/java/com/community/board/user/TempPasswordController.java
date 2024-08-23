package com.community.board.user;

import com.community.board.mail.CommonUtil;
import com.community.board.mail.TempPasswordMail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class TempPasswordController {

    private final TempPasswordMail tempPasswordMail;
    private final CommonUtil commonUtil;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TempPasswordController.class);

    public TempPasswordController(TempPasswordMail tempPasswordMail, CommonUtil commonUtil, UserService userService) {
        this.tempPasswordMail = tempPasswordMail;
        this.commonUtil = commonUtil;
        this.userService = userService;
    }

    @PostMapping("/user/tempPassword")
    public String sendTempPassword(@RequestParam("username") String email, Model model) {

        try {
            userService.modifyPassword(email);
            model.addAttribute("message", "임시 비밀번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            logger.error("이메일 전송 실패: ", e);
            model.addAttribute("message", "이메일 전송에 실패하였습니다.");
        }

        return "result";
    }
}
