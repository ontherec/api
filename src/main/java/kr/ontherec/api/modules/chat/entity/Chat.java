package kr.ontherec.api.modules.chat.entity;

import jakarta.persistence.*;
import kr.ontherec.api.modules.chat.exception.ChatException;
import kr.ontherec.api.modules.chat.exception.ChatExceptionCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Chat {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Set<Participant> participants;

    public boolean isParticipant(String username) {
        return participants.stream()
            .anyMatch(participant -> participant.getUsername().equals(username));
    }

    public Participant getParticipant(String username) {
        return participants.stream()
            .filter(participant -> participant.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new ChatException(ChatExceptionCode.NOT_FOUND_PARTICIPANT));
    }

    public void removeParticipant(String username) {
        participants.remove(getParticipant(username));
    }
}
