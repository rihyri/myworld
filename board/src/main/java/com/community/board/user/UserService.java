package com.community.board.user;

import com.community.board.mail.CommonUtil;
import com.community.board.DataNotFoundException;
import com.community.board.mail.EmailException;
import com.community.board.mail.TempPasswordMail;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;
    private final TempPasswordMail tempPasswordMail;


    public SiteUser create(String username, String nickname, String password) {

        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        if(userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("이미 사용중인 닉네임입니다.");
        }

        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return user;
    }

    public SiteUser findByNickname(String nickname) {
        Optional<SiteUser> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("사용자가 존재하지 않습니다.");
        }
    }

    public SiteUser getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("site user not found");
        }
    }

    public void modifyNickname(Long userId, String nickname) throws Exception {

        if(userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("이미 사용중인 닉네임입니다.");
        }

        SiteUser user = userRepository.findById(userId).orElseThrow(() -> new Exception("User Not Found"));

        user.setNickname(nickname);
        userRepository.save(user);
    }

    public void modifyPassword(String username) throws EmailException {
        String tempPassword = commonUtil.createTempPassword();
        SiteUser user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("해당 이메일의 유저가 없습니다."));
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        boolean usePort587 = true;
        tempPasswordMail.sendSimpleMessage(username, tempPassword, usePort587);
    }


    public void alterPassword(SiteUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

}
