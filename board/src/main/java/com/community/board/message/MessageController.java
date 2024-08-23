package com.community.board.message;

import com.community.board.user.SiteUser;
import com.community.board.user.UserRepository;
import com.community.board.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final UserService userService;

    @ModelAttribute("unreadMessages")
    public List<MessageDto> populateUnreadMessages(Principal principal) {
        if (principal != null) {
            String currentUsername = principal.getName();
            SiteUser currentUser = userService.getUser(currentUsername);
            return messageService.unreadMessages(currentUser);
        }
        return new ArrayList<>();
    }

    @GetMapping("/messages/insert")
    public String sendMessage(Principal principal, Model model, MessageForm messageForm) {
        String username = principal.getName();
        SiteUser currentUser = userService.getUser(username);
        String nickname = currentUser.getNickname();

        model.addAttribute("nickname", nickname);
        return "message/message_form";
    }

    @PostMapping("/messages/insert")
    public String sendMessage(Model model, Principal principal, @Valid MessageForm messageForm, BindingResult bindingResult) {
        String currentUsername = principal.getName();
        SiteUser currentUser = userService.getUser(currentUsername);

        if (bindingResult.hasErrors()) {
            return "message/message_form";
        }

        if (currentUser == null) {
            model.addAttribute("error", "로그인한 사용자를 찾을 수 없습니다.");
            return "message/message_form";
        }

        String senderNickname = currentUser.getNickname();
        Optional<SiteUser> receiverUser = userRepository.findByNickname(messageForm.getReceiverName());

        if (receiverUser.isEmpty()) {
            bindingResult.rejectValue("receiverName", "not found", "받는 사람을 찾을 수 없습니다. 닉네임을 다시 한번 확인해 주세요.");
            return "message/message_form";
        }

        this.messageService.write(messageForm.getReceiverName(), senderNickname, messageForm.getTitle(), messageForm.getContent());
        return "message/message_success";
    }

    @GetMapping("/messages/received/list")
    public String getReceivedMessage(Model model, Principal principal,
                                     @RequestParam(value = "page", defaultValue = "0") int page) {
        String currentUsername = principal.getName();
        SiteUser currentUser = userService.getUser(currentUsername);

        // 페이지네이션된 결과 가져오기
        Page<Message> paging = this.messageService.getReceiverMessage(currentUser, page);

        // Message 객체를 DTO로 변환
        List<MessageDto> messageDtos = paging.getContent().stream()
                .filter(message -> !message.isDeleteByReceiver()) // 수신자가 삭제하지 않은 메시지만 필터링
                .map(MessageDto::toDto) // Message 객체를 MessageDto로 변환
                .collect(Collectors.toList());

        model.addAttribute("paging", paging);
        model.addAttribute("messages", messageDtos);
        return "message/message_list";
    }

    @GetMapping("/message/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Principal principal) {
        Message message = this.messageService.getMessage(id);
        messageService.markMessageAsViewed(id);

        model.addAttribute("message", message);
        return "message/message_detail";
    }

    @GetMapping("/messages/received/{id}")
    public String deleteReceiveMessage(@PathVariable("id") Integer id) {

        try {
            messageService.deleteMessageByReceiver(id);
            return "redirect:/messages/received/list";
        } catch (IllegalArgumentException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/messages/sent")
    public String getSentMessage(Model model, Principal principal,
                                 @RequestParam(value = "page", defaultValue = "0") int page) {
        String currentUsername = principal.getName();
        SiteUser currentUser = userService.getUser(currentUsername);

        // 페이지네이션된 결과 가져오기
        Page<Message> paging = this.messageService.getSentMessage(currentUser, page);

        // Message 객체를 DTO로 변환
        List<MessageDto> messageDtos = paging.getContent().stream()
                .map(MessageDto::toDto)  // Message 객체를 MessageDto로 변환
                .collect(Collectors.toList());

        model.addAttribute("paging", paging);
        model.addAttribute("messages", messageDtos);
        return "message/messages_sent_list";
    }


    @GetMapping("/message/sent/detail/{id}")
    public String sentDetail(Principal principal, Model model, @PathVariable("id") Integer id) {
        Message message = this.messageService.getMessage(id);

        model.addAttribute("message", message);
        return "message/message_sent_detail";
    }

    @GetMapping("/messages/sent/{id}")
    public String deleteSentMessage(@PathVariable("id") Integer id, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUser(username);

        try {
            messageService.deleteMessageBySender(id, user);
            return "redirect:/messages/sent";
        } catch (IllegalArgumentException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/messages/self")
    public String getSelfMessages(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page) {
        String username = principal.getName();
        SiteUser currentUser = userService.getUser(username);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        Page<MessageDto> paging = messageService.selfMessage(currentUser, pageable);

        model.addAttribute("paging", paging);
        return "message/messages_self_list";
    }

    @GetMapping("/message/self/detail/{id}")
    public String selfDetail(Model model, @PathVariable("id") Integer id, Principal principal) {
        Message message = this.messageService.getMessage(id);
        messageService.markMessageAsViewed(id);

        model.addAttribute("message", message);
        return "message/message_self_detail";
    }

    @GetMapping("/messages/self/{id}")
    public String deleteSelfMessage(@PathVariable("id") Integer id, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUser(username);

        try {
            messageService.deleteMessageBySenderAndReceiver(id, user);
            return "redirect:/messages/self";
        } catch (IllegalArgumentException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/messages/success")
    public String successMessage(Principal principal, Model model) {
        return "message_success";
    }
}
