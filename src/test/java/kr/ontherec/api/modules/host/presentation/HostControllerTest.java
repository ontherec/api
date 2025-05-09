package kr.ontherec.api.modules.host.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import com.epages.restdocs.apispec.Schema;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.modules.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.modules.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.modules.host.entity.Bank;
import kr.ontherec.api.modules.host.entity.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.time.LocalTime;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.NUMBER;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static io.restassured.RestAssured.given;
import static kr.ontherec.api.infra.config.SecurityConfig.API_KEY_HEADER;
import static kr.ontherec.api.infra.entity.Regex.BANK_ACCOUNT;
import static kr.ontherec.api.modules.host.exception.HostExceptionCode.EXIST_USERNAME;
import static kr.ontherec.api.modules.host.exception.HostExceptionCode.NOT_VALID_CONTACT_TIME;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@IntegrationTest
class HostControllerTest {
    @Autowired private HostFactory hostFactory;

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

    @Test
    @DisplayName("호스트 등록 성공")
    void register() {

        // given
        HostRegisterRequestDto dto = new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
                .filter(RestAssuredRestDocumentationWrapper.document(
                        "host register",
                        resource(ResourceSnippetParameters.builder()
                                .tag("host")
                                .summary("host register")
                                .description("호스트 등록")
                                .requestSchema(Schema.schema(HostRegisterRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("bank")
                                                .type(STRING)
                                                .description("은행"),
                                        fieldWithPath("account")
                                                .type(STRING)
                                                .description("계좌번호 (" + BANK_ACCOUNT + ")")
                                                .optional())
                                .build())))
        .when()
                .post("/hosts")
        .then()
                .statusCode(CREATED.value())
                .header("Location", startsWith("/v1/hosts"))
                .body(notNullValue());
    }

    @Test
    @DisplayName("호스트 등록 실패 - 이미 등록된 사용자")
    void registerExistUsername() {

        // given
        hostFactory.create("test");

        HostRegisterRequestDto dto = new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/hosts")
        .then()
                .statusCode(EXIST_USERNAME.getStatus().value())
                .body("message", equalTo(EXIST_USERNAME.getMessage()));
    }

    @Test
    @DisplayName("호스트 조회 성공")
    void get() {

        // given
        Host host = hostFactory.create("test");

        given(this.spec)
                .contentType(ContentType.JSON)
                .filter(RestAssuredRestDocumentationWrapper.document(
                        "host get",
                        resource(ResourceSnippetParameters.builder()
                                .tag("host")
                                .summary("host get")
                                .description("호스트 조회")
                                .responseSchema(Schema.schema(Host.class.getSimpleName()))
                                .responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("식별자"),
                                        fieldWithPath("username")
                                                .type(STRING)
                                                .description("ID"),
                                        fieldWithPath("contactFrom")
                                                .type(STRING)
                                                .description("문의 가능 시작 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("contactUntil")
                                                .type(STRING)
                                                .description("문의 가능 종료 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("averageResponseTime")
                                                .type(NUMBER)
                                                .description("평균 응답 시간 (ISO 8601 Duration)")
                                                .optional(),
                                        fieldWithPath("createdAt")
                                                .type(STRING)
                                                .description("생성된 시간 (UTC)")
                                                .optional(),
                                        fieldWithPath("modifiedAt")
                                                .type(STRING)
                                                .description("수정된 시간 (UTC)")
                                                .optional())
                                .build())))
        .when()
                .get("/hosts/{id}", host.getId())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(host.getId().intValue()));
    }

    @Test
    @DisplayName("호스트 수정 성공")
    void update() {

        // given
        hostFactory.create("test");

        HostUpdateRequestDto dto = new HostUpdateRequestDto(
                Bank.하나,
                "00000000000000",
                LocalTime.MIDNIGHT,
                LocalTime.NOON
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
                .filter(RestAssuredRestDocumentationWrapper.document(
                        "host update",
                        resource(ResourceSnippetParameters.builder()
                                .tag("host")
                                .summary("host update")
                                .description("호스트 정보 수정")
                                .requestSchema(Schema.schema(HostUpdateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("bank")
                                                .type(STRING)
                                                .description("은행"),
                                        fieldWithPath("account")
                                                .type(STRING)
                                                .description("계좌번호 (" + BANK_ACCOUNT + ")")
                                                .optional(),
                                        fieldWithPath("contactFrom")
                                                .type(STRING)
                                                .description("문의 가능 시작 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("contactUntil")
                                                .type(STRING)
                                                .description("문의 가능 종료 시간 (HH:mm:ss.SSS)")
                                                .optional())
                                .build())))
        .when()
                .patch("/hosts/me")
        .then()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("호스트 수정 실패 - 유효하지 않은 문의 가능 시간")
    void updateWithInvalidContactTime() {

        // given
        hostFactory.create("test");

        HostUpdateRequestDto dto = new HostUpdateRequestDto(
                Bank.하나,
                "00000000000000",
                LocalTime.NOON,
                LocalTime.MIDNIGHT
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .patch("/hosts/me")
        .then()
                .statusCode(NOT_VALID_CONTACT_TIME.getStatus().value())
                .body("message", equalTo(NOT_VALID_CONTACT_TIME.getMessage()));
    }
}