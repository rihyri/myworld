package com.community.board.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserModifyFormDto {

    private Long user_id;
    private Long id;

    @NotEmpty(message = "닉네임을 입력해 주세요.")
    private String nickname;

    private List<UserImgDto> userImgDtoList;
    private Long userImgId;
    private static ModelMapper modelMapper = new ModelMapper();

    public SiteUser modify() {
        return modelMapper.map(this, SiteUser.class);
    }

    public static UserModifyFormDto of(SiteUser user) {
        return modelMapper.map(user, UserModifyFormDto.class);
    }

    }

