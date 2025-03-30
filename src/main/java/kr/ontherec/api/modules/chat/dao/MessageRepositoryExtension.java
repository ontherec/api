package kr.ontherec.api.modules.chat.dao;

import kr.ontherec.api.modules.chat.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MessageRepositoryExtension {
    List<Message> findPage(Long chatId, Long lastMessageId, int size);
}