package kr.ontherec.api.domain.chat.dao;

import kr.ontherec.api.domain.chat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChat_Id(Long chatId, Pageable pageable);
}