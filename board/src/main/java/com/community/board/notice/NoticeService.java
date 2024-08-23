package com.community.board.notice;

import com.community.board.DataNotFoundException;
import com.community.board.answer.Answer;
import com.community.board.question.Question;
import com.community.board.user.SiteUser;
import com.community.board.user.UserRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public List<Notice> getList() {
        return this.noticeRepository.findAll();
    }

    @Transactional
    public Notice getNotice(Integer id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("notice Not Found");
        }
    }


    @Transactional
    public void create(String subject, String content, Principal principal) {
        Notice n = new Notice();
        n.setSubject(subject);
        n.setContent(content);
        n.setCreateDate(LocalDateTime.now());

        String username = principal.getName();
        SiteUser user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("User not found"));

        n.setAuthor(user);

        this.noticeRepository.save(n);
    }


    @Transactional
    public Page<Notice> getList(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }


    @Transactional
    public void modify(Notice notice, String subject, String content) {
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setModifyDate(LocalDateTime.now());
        this.noticeRepository.save(notice);
    }


    @Transactional
    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }

}
