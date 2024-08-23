package com.community.board.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_img_id")
    private UserImg userImg;

    public SiteUser() {
        this.userImg = new UserImg();
        this.userImg.setImgName("default.png");
        this.userImg.setOriImgName("default.png");
        this.userImg.setImgUrl("/img/profile.png");

        this.userImg.setUser(this);
    }
}
