package com.community.board.user;

import com.community.board.user.UserImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImgRepository extends JpaRepository<UserImg, Long> {

    UserImg findByUserId(Long userId);
}
