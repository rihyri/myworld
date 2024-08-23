package com.community.board.user;

import com.community.board.DataNotFoundException;
import com.community.board.mail.EmailException;
import com.community.board.mail.RegisterMail;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RegisterMail registerMail;
    private final UserImgService userImgService;
    private final UserImgRepository userImgRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug(bindingResult.getAllErrors().toString());
            return "user/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "user/signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getNickname(), userCreateForm.getPassword1());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("이미 사용중인 이메일입니다.")) {
                bindingResult.rejectValue("username", "usernameDuplicated", "이메일이 이미 사용 중입니다.");
            } else if (e.getMessage().contains("이미 사용중인 닉네임입니다.")) {
                bindingResult.rejectValue("nickname", "nicknameDuplicated", "닉네임이 이미 사용 중입니다.");
            }
            return "user/signup_form";
        }

        return "user/login_form";
    }

    @PostMapping("/login/mailConfirm")
    @ResponseBody
    String mailConfirm(@RequestParam("username") String username) throws Exception {

        String code = registerMail.sendSimpleMessage(username);
        return code;
    }

    @GetMapping("/modify")
    public String userModifyForm(Model model, Principal principal, UserModifyFormDto userModifyFormDto) {
        String currentUsername = principal.getName();
        SiteUser currentUser = userService.getUser(currentUsername);
        Long userId = currentUser.getId();

        UserImg userImg = userImgService.getCurrentUserImg(userId);
        String url = userImg.getImgUrl();

        model.addAttribute("userId", userId);
        model.addAttribute("nickname", currentUser.getNickname());
        model.addAttribute("userModifyFormDto", userModifyFormDto);
        model.addAttribute("url", url);
        return "user/modify_form";
    }

    @PostMapping("/modify")
    public String userModify(Principal principal, @Valid UserModifyFormDto userModifyFormDto, BindingResult bindingResult, Model model, @RequestParam(value = "userImgFile", required = false) List<MultipartFile> userImgFileList) throws Exception {

        String currentUsername = principal.getName();
        SiteUser currentUser = userService.getUser(currentUsername);
        Long userId = currentUser.getId();
        String userNickname = currentUser.getNickname();
        UserImg userImg = currentUser.getUserImg();

        String imageUrl = userImg.getImgUrl();
        model.addAttribute("url", imageUrl);

        userImgService.updateUserImg(userImg.getId(), userImgFileList.get(0));

        if (bindingResult.hasErrors()) {
            return "user/modify_form";
        } else {
            try {
                if (!userNickname.equals(userModifyFormDto.getNickname())) {
                    userService.modifyNickname(userId, userModifyFormDto.getNickname());
                    userImgService.updateUserImg(userImg.getId(), userImgFileList.get(0));
                }
            } catch (Exception e) {
                if (e.getMessage().contains("이미 사용중인 닉네임입니다.")) {
                    bindingResult.rejectValue("nickname", "nicknameDuplicated", "이미 사용중인 닉네임입니다.");
                }
                return "user/modify_form";
            }
        }

        return "redirect:/user/modify";
    }

    @GetMapping("/modify/delete")
    public String profileDelete(Principal principal, Model model) {
        try {
            userImgService.resetToDefaultImage(principal);

        } catch (Exception e) {
            logger.error("프로필 사진을 기본 이미지로 업데이트 중 에러 발생", e);
            model.addAttribute("errorMessage", "프로필 사진을 기본 이미지로 업데이트 중 에러 발생: " + e.getMessage());
            return "user/modify_form";
        }
        return "redirect:/user/modify";
    }


    @GetMapping("/page")
    public String userPage(@RequestParam (name = "userNickname", required = false) String userNickname, Model model, Principal principal) {

        SiteUser user = userService.findByNickname(userNickname);

        if (user.getUsername().equals(principal.getName())) {
            return "redirect:/user/modify";

        } else {

            String url = user.getUserImg().getImgUrl();

            model.addAttribute("user", user);
            model.addAttribute("url", url);

            return "user/user_page";
        }
    }


    @GetMapping("/tempPassword")
    public String sendTempPassword() {
        return "user/temp_password_form";
    }


    @GetMapping("/alter/password")
    public String alterPassword(PasswordAlterForm passwordAlterForm) {
        return "user/alter_password";
    }


    @PostMapping("/alter/password")
    public String alterPassword(@Valid PasswordAlterForm passwordAlterForm, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        SiteUser user = userService.getUser(username);

        if (bindingResult.hasErrors()) {
            logger.debug(bindingResult.getAllErrors().toString());
            return "user/alter_password";
        }


        if (!passwordAlterForm.getPassword1().equals(passwordAlterForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "user/alter_password";
        }

        userService.alterPassword(user, passwordAlterForm.getPassword1());

        return "redirect:/user/modify";
    }
}
