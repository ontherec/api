package kr.ontherec.api.domain.chat.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.domain.chat.domain.QMessage;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MessageRepositoryExtensionImpl extends QuerydslRepositorySupport implements MessageRepositoryExtension {

    public MessageRepositoryExtensionImpl() {
        super(Message.class);
    }

    @Override
    public List<Message> findPage(Long chatId, Long lastMessageId, int size) {
        QMessage message = QMessage.message;

        BooleanExpression condition = lastMessageId == null ?
                message.chat.id.eq(chatId) :
                message.chat.id.eq(chatId)
                .and(message.id.gt(lastMessageId));

        return from(message)
                .where(condition)
                .orderBy(message.id.asc())
                .limit(size)
                .fetch();
    }
}