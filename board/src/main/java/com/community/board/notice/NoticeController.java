package com.community.board.notice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<Notice> paging = this.noticeService.getList(page);
        model.addAttribute("paging", paging);

        return "notice/notice_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Notice notice = this.noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "notice/notice_detail";
    }

    @GetMapping("/create")
    public String noticeCreate(Model model) {
        model.addAttribute("noticeForm", new NoticeForm());
        return "notice/notice_form";
    }

    @PostMapping("/create")
    public String noticeCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        this.noticeService.create(noticeForm.getSubject(), noticeForm.getContent(), principal);

        return "redirect:/notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String noticeModify(NoticeForm noticeForm, @PathVariable("id") Integer id, Principal principal) {
        Notice notice = this.noticeService.getNotice(id);
        if (!notice.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        noticeForm.setSubject(notice.getSubject());
        noticeForm.setContent(notice.getContent());
        return "notice/notice_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String noticeModify(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, @PathVariable("id")Integer id) {

        if(bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        Notice notice = this.noticeService.getNotice(id);

        if (!notice.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        this.noticeService.modify(notice, noticeForm.getSubject(), noticeForm.getContent());
        return String.format("redirect:/notice/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated")
    @GetMapping("/delete/{id}")
    public String noticeDelete(Principal principal, @PathVariable("id") Integer id) {

        Notice notice = this.noticeService.getNotice(id);

        if (!notice.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        noticeService.delete(notice);
        return "redirect:/notice/list";
    }
}
