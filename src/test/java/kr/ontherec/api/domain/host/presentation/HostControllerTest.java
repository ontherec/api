package kr.ontherec.api.domain.host.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.ontherec.api.domain.host.application.HostMapper;
import kr.ontherec.api.domain.host.application.HostService;
import kr.ontherec.api.domain.host.domain.Bank;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.domain.host.exception.HostExceptionCode;
import kr.ontherec.api.infra.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@IntegrationTest
class HostControllerTest {

    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @Autowired
    private HostService hostService;

    @Value("${spring.security.api-key}")
    private String API_KEY;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("호스트 등록 성공")
    void register() {

        // given
        HostRegisterRequestDto dto = new HostRegisterRequestDto(
                Bank.KB국민,
                "00000000000000"
        );

        given()
                .header("X-API-KEY", API_KEY)
                .contentType(ContentType.JSON)
                .body(dto)
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
}