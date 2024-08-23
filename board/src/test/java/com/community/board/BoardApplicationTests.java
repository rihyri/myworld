package com.community.board;

import com.community.board.notice.Notice;
import com.community.board.notice.NoticeRepository;
import com.community.board.question.QuestionService;
import com.community.board.user.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private UserImgRepository userImgRepository;
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired

	@Transactional
	@Test
	void testJpa() {
		for (int i = 1; i <= 100; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			SiteUser user = new SiteUser();
			user.setUsername("peyz@gmail.com");
			user.setPassword("12345");
			user.setNickname("peyz");
			this.questionService.create(subject, content, user);
		}
	}


	public MultipartFile createMultipartFile() throws Exception {
		String path = "C:/comm/profile/";
		String imageName = "image1.jpg";
		byte[] content = new byte[]{1, 2, 3, 4};

		MultipartFile file = new MockMultipartFile(
				"file",
				imageName,
				"image/jpge",
				content
		);

		return file;
	}

	@Test
	void testNotice() {
		Notice n1 = new Notice();
		n1.setSubject("공지입니다.");
		n1.setContent("열심히 하세요.");
		n1.setCreateDate(LocalDateTime.now());

		this.noticeRepository.save(n1);
	}




}
