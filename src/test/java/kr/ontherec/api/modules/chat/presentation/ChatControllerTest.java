package kr.ontherec.api.modules.chat.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.ChatFactory;
import kr.ontherec.api.infra.fixture.MessageFactory;
import kr.ontherec.api.modules.chat.dto.ChatCreateRequestDto;
import kr.ontherec.api.modules.chat.dto.ChatResponseDto;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.NUMBER;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static kr.ontherec.api.infra.config.SecurityConfig.API_KEY_HEADER;
import static kr.ontherec.api.modules.chat.entity.MessageType.TEXT;
import static kr.ontherec.api.modules.chat.exception.ChatExceptionCode.FORBIDDEN;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.snippet.Attributes.key;

@IntegrationTest
class ChatControllerTest {
    @Autowired private ChatFactory chatFactory;
    @Autowired private MessageFactory messageFactory;

    @Value("${spring.security.api-key}")
    private String API_KEY;

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1";
        RestAssured.port = port;

        this.spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint())
        ).build();
    }

    @DisplayName("내 채팅방 목록 조회 성공")
    @Test
    void getMyChats() {
        Chat chat = chatFactory.create("chat", Set.of("test"));
        messageFactory.create(chat, TEXT, "test", "message");

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "chat get my chats",
                        resource(ResourceSnippetParameters.builder()
                                .tag("chat")
                                .summary("chat get my chats")
                                .description("내 채팅방 목록 조회")
                                .responseSchema(Schema.schema(ChatResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("채팅방 목록"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("채팅방 식별자"),
                                        fieldWithPath("[].title")
                                                .type(STRING)
                                                .description("채팅방 제목"),
                                        fieldWithPath("[].participants[]")
                                                .type(ARRAY)
                                                .description("참가자 목록"),
                                        fieldWithPath("[].participants[].id")
                                                .type(NUMBER)
                                                .description("참가자 식별자"),
                                        fieldWithPath("[].participants[].username")
                                                .type(STRING)
                                                .description("참가자 ID"),
                                        fieldWithPath("[].participants[].readAt")
                                                .type(STRING)
                                                .description("마지막으로 읽은 시간 (UTC)"),
                                        fieldWithPath("[].messages[]")
                                                .type(ARRAY)
                                                .description("메시지 목록"),
                                        fieldWithPath("[].messages[].id")
                                                .type(NUMBER)
                                                .description("메시지 식별자"),
                                        fieldWithPath("[].messages[].type")
                                                .type("enum")
                                                .description("메시지 타입 - " + Arrays.toString(MessageType.values())),
                                        fieldWithPath("[].messages[].username")
                                                .type(STRING)
                                                .description("메시지 작성자"),
                                        fieldWithPath("[].messages[].content")
                                                .type(STRING)
                                                .description("메시지 내용"),
                                        fieldWithPath("[].messages[].createdAt")
                                                .type(STRING)
                                                .description("메시지 전송 시간 (UTC)"))
                                .build())))
        .when()
                .get("/chats/me")
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("채팅방 메시지 목록 조회 성공")
    @Test
    void getMessages() {
        Chat chat = chatFactory.create("chat", Set.of("test"));
        for(int i = 0 ; i < 20 ; i++) {
            messageFactory.create(chat, TEXT, "test", "message" + i);
        }

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .queryParam("lastMessageId", 10)
                .queryParam("size", 10)
                .filter(document(
                        "chat get messages",
                        resource(ResourceSnippetParameters.builder()
                                .tag("chat")
                                .summary("chat get messages")
                                .description("메시지 목록 조회")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("메시지 목록을 조회할 채팅방 식별자"))
                                .queryParameters(
                                        parameterWithName("lastMessageId")
                                                .type(NUMBER)
                                                .description("마지막 메시지 식별자"),
                                        parameterWithName("size")
                                                .type(NUMBER)
                                                .description("조회할 메시지 개수"))
                                .responseSchema(Schema.schema(ChatResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("메시지 목록"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("메시지 식별자"),
                                        fieldWithPath("[].type")
                                                .type("enum")
                                                .description("메시지 타입 - " + Arrays.toString(MessageType.values())),
                                        fieldWithPath("[].username")
                                                .type(STRING)
                                                .description("메시지 작성자"),
                                        fieldWithPath("[].content")
                                                .type(STRING)
                                                .description("메시지 내용"),
                                        fieldWithPath("[].createdAt")
                                                .type(STRING)
                                                .description("메시지 전송 시간 (UTC)"))
                                .build())))
        .when()
                .get("/chats/{id}/messages", chat.getId())
        .then()
                .statusCode(OK.value())
                .body("size()", is(10));
    }

    @DisplayName("채팅방 메시지 목록 조회 성공 - 첫 페이지")
    @Test
    void getMessagesWithoutLastMessage() {
        Chat chat = chatFactory.create("chat", Set.of("test"));
        for(int i = 0 ; i < 20 ; i++) {
            messageFactory.create(chat, TEXT, "test", "message" + i);
        }

        given()
                .header(API_KEY_HEADER, API_KEY)
                .queryParam("size", 10)
        .when()
                .get("/chats/{id}/messages", chat.getId())
        .then()
                .statusCode(OK.value())
                .body("size()", is(10));
    }

    @DisplayName("채팅방 메시지 목록 조회 실패 - 권한 없음")
    @Test
    void getMessagesWithoutAuthority() {
        Chat chat = chatFactory.create("chat", Set.of("host"));

        given()
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .get("/chats/{id}/messages", chat.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }

    @DisplayName("채팅방 생성 성공")
    @Test
    void create() {
        ChatCreateRequestDto dto = new ChatCreateRequestDto(
                "chat",
                Set.of("test", "host")
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "chat create",
                        resource(ResourceSnippetParameters.builder()
                                .tag("chat")
                                .summary("chat create")
                                .description("채팅방 생성")
                                .requestSchema(Schema.schema(ChatCreateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("채팅방 제목"),
                                        fieldWithPath("participants[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("참가자 목록"))
                                .build())))
        .when()
                .post("/chats")
        .then()
                .statusCode(CREATED.value())
                .header("Location", startsWith("/v1/chats"))
                .body(notNullValue());
    }

    @DisplayName("채팅방 생성 실패 - 권한 없음")
    @Test
    void createWithoutAuthority() {
        ChatCreateRequestDto dto = new ChatCreateRequestDto(
                "chat",
                Set.of("host")
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
        .when()
                .post("/chats")
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }

    @DisplayName("채팅방 읽기 성공")
    @Test
    void read() {
        Chat chat = chatFactory.create("chat", Set.of("test"));

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "chat read",
                        resource(ResourceSnippetParameters.builder()
                                .tag("chat")
                                .summary("chat read")
                                .description("채팅방 읽음 처리")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("읽음 처리할 채팅방 식별자"))
                                .build())))
        .when()
                .patch("/chats/{id}/read", chat.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("채팅방 읽기 실패 - 권한 없음")
    @Test
    void readWithoutAuthority() {
        Chat chat = chatFactory.create("chat", Set.of("host"));

        given()
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .patch("/chats/{id}/read", chat.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }

    @DisplayName("채팅방 나가기 성공")
    @Test
    void exit() {
        Chat chat = chatFactory.create("chat", Set.of("test"));

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "chat exit",
                        resource(ResourceSnippetParameters.builder()
                                .tag("chat")
                                .summary("chat exit")
                                .description("채팅방 나가기")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("나갈 채팅방 식별자"))
                                .build())))
        .when()
                .delete("/chats/{id}", chat.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("채팅방 나가기 실패 - 참가하지 않은 채팅방")
    @Test
    void exitWithNotParticipatedChat() {
        Chat chat = chatFactory.create("chat", Set.of("host"));

        given()
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .delete("/chats/{id}", chat.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }
}