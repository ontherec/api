package kr.ontherec.api.modules.chat.dao;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.QChat;
import kr.ontherec.api.modules.chat.entity.QParticipant;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ChatRepositoryExtensionImpl extends QuerydslRepositorySupport implements ChatRepositoryExtension {

    public ChatRepositoryExtensionImpl() {
        super(Chat.class);
    }

    @Override
    public List<Chat> findAllByUsername(String username) {
        QChat chat = QChat.chat;
        QParticipant participant = QParticipant.participant;

        JPQLQuery<Long> subQuery = JPAExpressions.select(chat.id)
                .from(chat)
                .join(chat.participants, participant)
                .where(participant.username.eq(username));

        return from(chat)
                .join(chat.participants, participant)
                .fetchJoin()
                .where(chat.id.in(subQuery))
                .fetch();
    }
}