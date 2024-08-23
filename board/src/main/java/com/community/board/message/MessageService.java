package com.community.board.message;

import com.community.board.DataNotFoundException;
import com.community.board.user.SiteUser;
import com.community.board.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;


    public Message getMessage(Integer id) {
        Optional<Message> message = this.messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        } else {
            throw new DataNotFoundException("message not found");
        }
    }

    @Transactional(readOnly = true)
    public List<MessageDto> unreadMessages(SiteUser user) {

        List<Message> messages = messageRepository.findAllByReceiver(user);
        List<MessageDto> messageDtos = new ArrayList<>();

        for (Message message : messages) {
            if (!message.isViewed() && !message.isDeleteBySender()) {
                messageDtos.add(MessageDto.toDto(message));
            }
        }
        return messageDtos;
    }

    @Transactional
    public MessageDto write(String receiverName, String senderName, String title, String content) {
        SiteUser receiver = userService.findByNickname(receiverName);
        SiteUser sender = userService.findByNickname(senderName);

        Message message = new Message();

        message.setReceiver(receiver);
        message.setSender(sender);
        message.setTitle(title);
        message.setContent(content);
        message.setDeleteByReceiver(false);
        message.setDeleteBySender(false);
        message.setCreateDate(LocalDateTime.now());
        message.setViewed(false);
        messageRepository.save(message);

        return MessageDto.toDto(message);
    }

    public Page<Message> getReceiverMessage(SiteUser user, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.messageRepository.findAllByReceiverAndNotSender(user, pageable);
    }

    @Transactional
    public void markMessageAsViewed(int messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message ID: " + messageId));
        message.setViewed(true);
        messageRepository.save(message);
    }

    @Transactional
    public void deleteMessageByReceiver(int id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        message.setDeleteByReceiver(true);

        if (message.isDeleteBySender()) {
            messageRepository.delete(message);
        }
    }

    public Page<Message> getSentMessage(SiteUser user, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.messageRepository.findAllBySenderAndNotReceiver(user, pageable);
    }

    @Transactional
    public void deleteMessageBySender(int id, SiteUser user) {
        Message message = messageRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        });

        message.setDeleteBySender(true);

        if (message.isDeleteByReceiver()) {
            messageRepository.delete(message);
        }
    }

    @Transactional(readOnly = true)
    public Page<MessageDto> selfMessage(SiteUser user, Pageable pageable) {
        Page<Message> messages = messageRepository.findAllBySenderAndReceiver(user, user, pageable);
        return messages.map(MessageDto::toDto);
    }

    @Transactional
    public void deleteMessageBySenderAndReceiver(int id, SiteUser user) {
        Message message = messageRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        });

        if (user == message.getSender() && user == message.getReceiver()) {
                messageRepository.delete(message);
        }
    }
}
