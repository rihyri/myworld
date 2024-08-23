package com.community.board.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class UserImgService {

    @Value("${userImgLocation}")
    private String userImgLocation;

    private final UserImgRepository userImgRepository;
    private final FileService fileService;
    private final UserRepository userRepository;


    public void updateUserImg(Long userImgId, MultipartFile userImgFile) throws Exception {
        if(!userImgFile.isEmpty()) {
            UserImg savedUserImg = userImgRepository.findById(userImgId).orElseThrow(EntityNotFoundException::new);

            if(StringUtils.hasText(savedUserImg.getImgName())) {
                fileService.deleteFile(userImgLocation + "/" + savedUserImg.getImgName());
            }

            String oriImgName = userImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(userImgLocation, oriImgName, userImgFile.getBytes());
            String imgUrl = "/images/profile/" + imgName;
            savedUserImg.updateUserImg(oriImgName, imgName, imgUrl);
        }
    }


    public void resetToDefaultImage(Principal principal) throws Exception {

        String username = principal.getName();
        SiteUser user = userRepository.findByUsername(username).orElseThrow();
        UserImg savedUserImg = user.getUserImg();

        String defaultImgPath = "/img/profile.png";

        savedUserImg.setImgName("default.png");
        savedUserImg.setOriImgName("default.png");
        savedUserImg.setImgUrl(defaultImgPath);

        userImgRepository.save(savedUserImg);
    }

    public UserImg getCurrentUserImg(Long userId) {
        UserImg image = userImgRepository.findByUserId(userId);
        if (image != null) {
            return image;
        }
        return null;
    }

}
