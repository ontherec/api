package kr.ontherec.api.domain.host.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.domain.host.application.HostMapper;
import kr.ontherec.api.domain.host.application.HostService;
import kr.ontherec.api.domain.host.domain.Bank;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.domain.host.exception.HostExceptionCode;
import kr.ontherec.api.infra.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.time.LocalTime;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@IntegrationTest
class HostControllerTest {

    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @Autowired
    private HostService hostService;

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
                .header("X-API-KEY", API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
                .filter(RestAssuredRestDocumentationWrapper.document(
                        "register",
                        resource(ResourceSnippetParameters.builder()
                                .tag("host")
                                .summary("register")
                                .description("호스트 등록")
                                .requestSchema(Schema.schema(HostRegisterRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("bank")
                                                .type(SimpleType.STRING)
                                                .description("은행"),
                                        fieldWithPath("account")
                                                .type(SimpleType.STRING)
                                                .description("계좌번호 (- 제외)")
                                                .optional())
                                .build())))
        .when()
                .post("/hosts")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", equalTo("/v1/hosts/me"))
                .body(notNullValue());
    }

    @Test
    @DisplayName("호스트 등록 실패 - 이미 등록된 사용자")
    void registerExistUsername() {

        // given
        HostRegisterRequestDto dto = new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        );

        Host newHost = hostMapper.registerRequestDtoToEntity("test", dto);
        hostService.register(newHost);

        given()
                .header("X-API-KEY", API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/hosts")
        .then()
                .statusCode(HostExceptionCode.EXIST_USERNAME.getStatus().value());
    }

    @Test
    @DisplayName("호스트 수정 성공")
    void update() {

        // given
        Host newHost = hostMapper.registerRequestDtoToEntity("test", new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        ));
        hostService.register(newHost);

        HostUpdateRequestDto dto = new HostUpdateRequestDto(
                Bank.하나,
                "00000000000000",
                LocalTime.MIDNIGHT,
                LocalTime.NOON
        );

        given(this.spec)
                .header("X-API-KEY", API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
                .filter(RestAssuredRestDocumentationWrapper.document(
                        "update",
                        resource(ResourceSnippetParameters.builder()
                                .tag("host")
                                .summary("update")
                                .description("호스트 정보 수정")
                                .requestSchema(Schema.schema(HostUpdateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("bank")
                                                .type(SimpleType.STRING)
                                                .description("은행"),
                                        fieldWithPath("account")
                                                .type(SimpleType.STRING)
                                                .description("계좌번호 (- 제외)")
                                                .optional(),
                                        fieldWithPath("contactFrom")
                                                .type(SimpleType.STRING)
                                                .description("연락 가능 시작 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("contactUntil")
                                                .type(SimpleType.STRING)
                                                .description("연락 가능 종료 시간 (HH:mm:ss.SSS)")
                                                .optional())
                                .build())))
        .when()
                .patch("/hosts/me")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("호스트 수정 실패 - 유효하지 않은 연락 가능 시간")
    void updateWithInvalidContactTime() {

        // given
        Host newHost = hostMapper.registerRequestDtoToEntity("test", new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        ));
        hostService.register(newHost);

        HostUpdateRequestDto dto = new HostUpdateRequestDto(
                Bank.하나,
                "00000000000000",
                LocalTime.NOON,
                LocalTime.MIDNIGHT
        );

        given()
                .header("X-API-KEY", API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .patch("/hosts/me")
        .then()
                .statusCode(HostExceptionCode.NOT_VALID_CONTACT_TIME.getStatus().value());
    }
}