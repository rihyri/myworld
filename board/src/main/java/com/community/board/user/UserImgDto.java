package com.community.board.user;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class UserImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserImgDto of(UserImg userImg) {
        return modelMapper.map(userImg, UserImgDto.class);
    }
}
