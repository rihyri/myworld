package com.community.board.message;

import com.community.board.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.sender != :user AND m.deleteByReceiver = false")
    List<Message> findAllByReceiver(@Param("user")SiteUser user);
    List<Message> findAllBySender(SiteUser user);

    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.sender != :user AND m.deleteByReceiver = false")
    Page<Message> findAllByReceiverAndNotSender(@Param("user")SiteUser user, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.sender = :user AND m.receiver != :user AND m.deleteBySender = false")
    Page<Message> findAllBySenderAndNotReceiver(@Param("user")SiteUser user, Pageable pageable);

    Page<Message> findAllBySenderAndReceiver(SiteUser sender, SiteUser receiver, Pageable pageable);


}
