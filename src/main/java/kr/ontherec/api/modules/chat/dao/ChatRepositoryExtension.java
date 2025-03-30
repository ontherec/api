package kr.ontherec.api.modules.chat.dao;

import kr.ontherec.api.modules.chat.entity.Chat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ChatRepositoryExtension {
    List<Chat> findAllByUsername(String username);
}