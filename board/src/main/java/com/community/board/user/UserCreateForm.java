package com.community.board.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @Email(message = "이메일 형식을 확인해 주세요.")
    @NotEmpty(message = "사용자 e-mail은 필수항목입니다.")
    private String username;

    @NotEmpty(message = "인증번호를 입력해 주세요.")
    private String memailconfirm;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;
}
