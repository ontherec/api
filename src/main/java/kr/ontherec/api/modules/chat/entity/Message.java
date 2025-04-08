package kr.ontherec.api.modules.chat.entity;

import jakarta.persistence.*;
import kr.ontherec.api.infra.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder
@AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Chat chat;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private MessageType type;

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false, updatable = false)
    private String content;
}