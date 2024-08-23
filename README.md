![image](https://github.com/user-attachments/assets/b9c9a198-1084-4cc6-94d0-e5e11dd7fbd4)
<br>
· 프로젝트 URL : http://13.124.111.120:8081/
<br><br>
˙ 작업기간 :  2024.08.01. - 2024.08.15 (2주)
<br><br>
˙ 작업인원 :  1명 (이혜리)
<br><br>
˙ 작업툴 :  
          java, Spring Boot(async-await), JPA, Maria DB, intelliJ,
          Ajax, thyemleaf, API통신, AWS,
          html, css, java script, jqery, figma, Boot Strap
<br><br>
˙ 개발 환경
 IDE : intelliJ
 java version : 17
 build system : gradle - groovy
 spring framework ver: 3.3.2 
<br>
<br>
Spring Boot에 대하여 더 알아가기 위하여 제작한 풀스택 프로젝트입니다.


① 회원가입 
- Spring Security를 이용한 회원가입 기능 구현, SMTP 이메일 인증(네이버)으로 이메일 인증 확인이 되어야만 가입 가능. 
![image](https://github.com/user-attachments/assets/bb78477c-449c-4aad-91e0-293331ed6661)

<br><br><br>

② 로그인/로그아웃 
- Spring Security를 이용한 로그인/로그아웃 구현. 로그인한 회원만 사이트에 접근 가능하며 권한에 따라 접근 가능한 페이지 존재.
<br>

③ 임시 비밀번호 발급 
-  SMTP 이메일 인증(네이버)으로 유저가 입력한 이메일로 임시 변경된 비밀번호를 보내준다.
![image](https://github.com/user-attachments/assets/f0873e20-936d-43f0-8be3-9e938682de4c)

<br>

④ 게시판 목록 
- 일반 게시판 / 공지사항 게시판 두 가지 카테고리 존재. 현재 카테고리에 맞는 게시글을 보여준다. 페이지네이션으로 한 페이지에 10개의 게시글이 보여진다.
<br>

⑤ 게시글 
- 현재 작성한 게시글 작성자, 본문, 추천 내용 구현. 수정·삭제 기능은 현재 로그인 유저 = 게시글 작성 유저가 같은 경우일 때만 보임.
- 작성자 외 타인은 게시글을 추천 가능. 아래는 댓글 목록, 댓글 쓰기 기능 구현. 댓글 추가 후 수정·삭제 기능은 댓글 작성자만 사용 가능.
  가장 추천수가 높은 기능은 상단에 Best 댓글로 한번 더 보여진다. 댓글 추천 기능 또한 본인 외 타인이 가능. 
  댓글의 닉네임을 누를경우 본인일 때는 마이 페이지로 넘어가고 타인의 경우 쪽지 쓰기 기능으로 넘어가며 receiver 닉네임이 클릭한 유저의 닉네임으로 추가된다.
![image](https://github.com/user-attachments/assets/32907bb3-4262-43d0-a92a-0a00a82f9fff)
![image](https://github.com/user-attachments/assets/3ea3d1a7-7d5e-4946-836a-60145d6aa98c)


<br>

⑥ 마이페이지 
- 첫 번째로 로그인한 유저의 닉네임을 변경할 수 있다. 빈 칸이나 중복 닉네임의 경우 경고 메시지 생성. 두 번째로 비밀번호 변경 기능. 비밀번호 입력-비밀번호 입력 확인이 같아야만 변경이 가능.
- 세 번째로 프로필 이미지 변경 가능. '삭제'버튼을 누르거나 이미지가 아무것도 없는 경우는 프로필 이미지가 default로 설정. 그 외 이미지 파일을 입력 시 프로필 사진이 변경된다.
- 이미지 외 다른 파일을 입력하였을 경우 alert창이 뜨며 프로필 사진 변경 불가능.
<br>

![image](https://github.com/user-attachments/assets/8fa2ac86-084c-4e73-b5ff-ed404e9a3033)


⑦ 쪽지함 
- Spring MVC를 이용하여 데이터를 주고받는 쪽지함 기능 구현. 수신자자와 발신자가 같은 경우 자동으로 '내게 쓰기' 쪽지함으로 이동. 
  수신 쪽지함에서 읽지 않은 쪽지 갯수를 확인 가능하며 확인한 쪽지는 연회색으로 변경된다. 수신자와 발신자가 다른 경우 쪽지를 두 쪽에서 모두 지워야만 DB에서 사라지고 내게 쓰기에서는 삭제 버튼을 누르면 바로 DB에서 사라진다.
![image](https://github.com/user-attachments/assets/40872c25-1415-43d6-bf56-8662eef6496c)
![image](https://github.com/user-attachments/assets/b28ad028-1fe7-4e52-8c7d-48137f3cca86)
![image](https://github.com/user-attachments/assets/99c2fae1-9f48-4d8f-a323-71563cd871e4)

  <br>
  <br><br>
코드 블로그:
https://velog.io/@rihyri/SpringBoot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-1.-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-o92zx8of
<br>
