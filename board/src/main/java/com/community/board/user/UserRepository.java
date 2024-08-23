package com.community.board.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<SiteUser> findByUsername(String username);

    Optional<SiteUser> findByNickname(String nickname);


}
