package kr.ontherec.api.domain.chat.dao;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.QChat;
import kr.ontherec.api.domain.chat.domain.QParticipant;
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
                .where(chat.id.in(subQuery))
                .fetch();
    }
}