package kr.ontherec.api.domain.place.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.domain.host.application.HostService;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.application.PlaceService;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.AddressRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceResponseDto;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.HostGenerator;
import kr.ontherec.api.infra.fixture.PlaceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static javax.management.openmbean.SimpleType.STRING;
import static kr.ontherec.api.domain.place.exception.PlaceExceptionCode.*;
import static kr.ontherec.api.global.config.SecurityConfig.API_KEY_HEADER;
import static kr.ontherec.api.global.model.Regex.BUSINESS_REGISTRATION_NUMBER;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@IntegrationTest
class PlaceControllerTest {

    @Autowired
    private PlaceService placeService;

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

    @DisplayName("플레이스 검색 성공")
    @Test
    void search() {

        // given
        Host newHost = HostGenerator.generate("test");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "search",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("search")
                                .description("플레이스 검색")
                                .responseSchema(Schema.schema(PlaceResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("플레이스 배열"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("플레이스 고유번호"),
                                        fieldWithPath("[].host")
                                                .type(OBJECT)
                                                .description("호스트"),
                                        fieldWithPath("[].host.id")
                                                .type(NUMBER)
                                                .description("호스트 고유번호"),
                                        fieldWithPath("[].host.username")
                                                .type(STRING)
                                                .description("호스트 ID"),
                                        fieldWithPath("[].host.contactFrom")
                                                .type(STRING)
                                                .description("문의 가능 시작 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("[].host.contactUntil")
                                                .type(STRING)
                                                .description("문의 가능 종료 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("[].host.averageResponseTime")
                                                .type(STRING)
                                                .description("평균 응답 시간")
                                                .optional(),
                                        fieldWithPath("[].brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
                                        fieldWithPath("[].name")
                                                .type(STRING)
                                                .description("이름"),
                                        fieldWithPath("[].address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("[].address.id")
                                                .type(NUMBER)
                                                .description("주소 고유번호"),
                                        fieldWithPath("[].address.zipcode")
                                                .type(STRING)
                                                .description("우편번호"),
                                        fieldWithPath("[].address.state")
                                                .type(STRING)
                                                .description("도/시"),
                                        fieldWithPath("[].address.city")
                                                .type(STRING)
                                                .description("시/군/구"),
                                        fieldWithPath("[].address.streetAddress")
                                                .type(STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("[].address.detail")
                                                .type(STRING)
                                                .description("상세 주소")
                                                .optional(),
                                        fieldWithPath("[].address.latitude")
                                                .type(NUMBER)
                                                .description("위도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("[].address.longitude")
                                                .type(NUMBER)
                                                .description("경도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("[].introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("[].keywords")
                                                .type(ARRAY)
                                                .description("키워드 목록")
                                                .optional(),
                                        fieldWithPath("[].links")
                                                .type(ARRAY)
                                                .description("링크 목록")
                                                .optional(),
                                        fieldWithPath("[].bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간")
                                                .optional(),
                                        fieldWithPath("[].bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간")
                                                .optional(),
                                        fieldWithPath("[].holidays")
                                                .type(ARRAY)
                                                .description("공휴일")
                                                .optional(),
                                        fieldWithPath("[].createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)")
                                                .optional(),
                                        fieldWithPath("[].modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)")
                                                .optional())
                                .build())))
        .when()
                .get("/places")
        .then()
                .statusCode(OK.value());

    }

    @DisplayName("플레이스 등록 성공")
    @Test
    void register() {

        // given
        Host newHost = HostGenerator.generate("test");
        hostService.register(newHost);

        AddressRegisterRequestDto addressDto = new AddressRegisterRequestDto(
                "00000",
                "경기도",
                "수원시 장안구",
                "율전로",
                null,
                new BigDecimal("000.0000000000"),
                new BigDecimal("000.0000000000")
        );
        PlaceRegisterRequestDto placeDto = new PlaceRegisterRequestDto(
                "0000000000",
                "place",
                addressDto,
                "introduction",
                Set.of("keyword"),
                Set.of("https://ontherec.kr"),
                Duration.ofDays(30),
                Duration.ofDays(1),
                null
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
                .filter(document(
                        "register",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("register")
                                .description("플레이스 등록")
                                .requestSchema(Schema.schema(PlaceRegisterRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
                                        fieldWithPath("name")
                                                .type(STRING)
                                                .description("이름"),
                                        fieldWithPath("address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("address.zipcode")
                                                .type(STRING)
                                                .description("우편번호"),
                                        fieldWithPath("address.state")
                                                .type(STRING)
                                                .description("도/시"),
                                        fieldWithPath("address.city")
                                                .type(STRING)
                                                .description("시/군/구"),
                                        fieldWithPath("address.streetAddress")
                                                .type(STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("address.detail")
                                                .type(STRING)
                                                .description("상세 주소")
                                                .optional(),
                                        fieldWithPath("address.latitude")
                                                .type(NUMBER)
                                                .description("위도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("address.longitude")
                                                .type(NUMBER)
                                                .description("경도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("keywords")
                                                .type(ARRAY)
                                                .description("키워드 목록")
                                                .optional(),
                                        fieldWithPath("links")
                                                .type(ARRAY)
                                                .description("링크 목록")
                                                .optional(),
                                        fieldWithPath("bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간")
                                                .optional(),
                                        fieldWithPath("bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간")
                                                .optional(),
                                        fieldWithPath("holidays")
                                                .type(ARRAY)
                                                .description("공휴일")
                                                .optional())
                                .build())))
        .when()
                .post("/places")
        .then()
                .statusCode(CREATED.value())
                .header("Location", equalTo("/v1/places/1"))
                .body(equalTo("1"));

    }

    @Test
    @DisplayName("플레이스 등록 실패 - 중복된 사업자등록번호")
    void registerWithDuplicatedBrn() {

        // given
        Host newHost = HostGenerator.generate("test");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        AddressRegisterRequestDto addressDto = new AddressRegisterRequestDto(
                "00000",
                "경기도",
                "수원시 장안구",
                "율전로",
                null,
                new BigDecimal("000.0000000000"),
                new BigDecimal("000.0000000000")
        );
        PlaceRegisterRequestDto placeDto = new PlaceRegisterRequestDto(
                "0000000000",
                "place",
                addressDto,
                null,
                null,
                null,
                Duration.ofDays(30),
                Duration.ofDays(1),
                null
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
        .when()
                .post("/places")
        .then()
                .statusCode(EXIST_BRN.getStatus().value())
                .body("message", equalTo(EXIST_BRN.getMessage()));
    }

    @Test
    @DisplayName("플레이스 등록 실패 - 유효하지 않은 예약 기간")
    void registerWithInvalidBookingPeriod() {

        // given
        Host newHost = HostGenerator.generate("test");
        hostService.register(newHost);

        AddressRegisterRequestDto addressDto = new AddressRegisterRequestDto(
                "00000",
                "경기도",
                "수원시 장안구",
                "율전로",
                null,
                new BigDecimal("000.0000000000"),
                new BigDecimal("000.0000000000")
        );
        PlaceRegisterRequestDto placeDto = new PlaceRegisterRequestDto(
                "0000000000",
                "place",
                addressDto,
                null,
                null,
                null,
                Duration.ofDays(30),
                Duration.ofDays(30),
                null
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
        .when()
                .post("/places")
        .then()
                .statusCode(NOT_VALID_BOOKING_PERIOD.getStatus().value())
                .body("message", equalTo(NOT_VALID_BOOKING_PERIOD.getMessage()));

    }

    @DisplayName("플레이스 조회 성공")
    @Test
    void get() {

        // given
        Host newHost = HostGenerator.generate("test");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "get",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("get")
                                .description("플레이스 조회")
                                .responseSchema(Schema.schema(PlaceResponseDto.class.getSimpleName()))
                                .responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("플레이스 고유번호"),
                                        fieldWithPath("host")
                                                .type(OBJECT)
                                                .description("호스트"),
                                        fieldWithPath("host.id")
                                                .type(NUMBER)
                                                .description("호스트 고유번호"),
                                        fieldWithPath("host.username")
                                                .type(STRING)
                                                .description("호스트 ID"),
                                        fieldWithPath("host.contactFrom")
                                                .type(STRING)
                                                .description("문의 가능 시작 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("host.contactUntil")
                                                .type(STRING)
                                                .description("문의 가능 종료 시간 (HH:mm:ss.SSS)")
                                                .optional(),
                                        fieldWithPath("host.averageResponseTime")
                                                .type(STRING)
                                                .description("평균 응답 시간")
                                                .optional(),
                                        fieldWithPath("brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
                                        fieldWithPath("name")
                                                .type(STRING)
                                                .description("이름"),
                                        fieldWithPath("address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("address.id")
                                                .type(NUMBER)
                                                .description("주소 고유번호"),
                                        fieldWithPath("address.zipcode")
                                                .type(STRING)
                                                .description("우편번호"),
                                        fieldWithPath("address.state")
                                                .type(STRING)
                                                .description("도/시"),
                                        fieldWithPath("address.city")
                                                .type(STRING)
                                                .description("시/군/구"),
                                        fieldWithPath("address.streetAddress")
                                                .type(STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("address.detail")
                                                .type(STRING)
                                                .description("상세 주소")
                                                .optional(),
                                        fieldWithPath("address.latitude")
                                                .type(NUMBER)
                                                .description("위도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("address.longitude")
                                                .type(NUMBER)
                                                .description("경도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("keywords")
                                                .type(ARRAY)
                                                .description("키워드 목록")
                                                .optional(),
                                        fieldWithPath("links")
                                                .type(ARRAY)
                                                .description("링크 목록")
                                                .optional(),
                                        fieldWithPath("bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간")
                                                .optional(),
                                        fieldWithPath("bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간")
                                                .optional(),
                                        fieldWithPath("holidays")
                                                .type(ARRAY)
                                                .description("공휴일")
                                                .optional(),
                                        fieldWithPath("createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)")
                                                .optional(),
                                        fieldWithPath("modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)")
                                                .optional())
                                .build())))
        .when()
                .get("/places/1")
        .then()
                .statusCode(OK.value());

    }

    @DisplayName("플레이스 조회 실패 - 등록되지 않은 플레이스")
    @Test
    void getWithUnregisteredId() {

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .get("/places/1")
        .then()
                .statusCode(NOT_FOUND.getStatus().value())
                .body("message", equalTo(NOT_FOUND.getMessage()));
    }

    @DisplayName("플레이스 수정 성공")
    @Test
    void update() {

        // given
        Host newHost = HostGenerator.generate("test");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        PlaceUpdateRequestDto dto = new PlaceUpdateRequestDto(
                "place",
                null,
                null,
                null,
                Duration.ofDays(30),
                Duration.ofDays(1),
                null
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("update")
                                .description("플레이스 수정")
                                .requestSchema(Schema.schema(PlaceUpdateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("name")
                                                .type(STRING)
                                                .description("이름"),
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("keywords")
                                                .type(ARRAY)
                                                .description("키워드 목록")
                                                .optional(),
                                        fieldWithPath("links")
                                                .type(ARRAY)
                                                .description("링크 목록")
                                                .optional(),
                                        fieldWithPath("bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간")
                                                .optional(),
                                        fieldWithPath("bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간")
                                                .optional(),
                                        fieldWithPath("holidays")
                                                .type(ARRAY)
                                                .description("공휴일")
                                                .optional())
                                .build())))
        .when()
                .patch("/places/1")
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("플레이스 수정 실패 - 권한 없음")
    @Test
    void updateWithoutAuthority() {
        Host me = HostGenerator.generate("test");
        hostService.register(me);

        Host newHost = HostGenerator.generate("host");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        PlaceUpdateRequestDto dto = new PlaceUpdateRequestDto(
                "place",
                null,
                null,
                null,
                Duration.ofDays(30),
                Duration.ofDays(1),
                null
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
        .when()
                .patch("/places/1")
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));

    }

    @DisplayName("플레이스 삭제 성공")
    @Test
    void remove() {

        // given
        Host newHost = HostGenerator.generate("test");
        Host host = hostService.register(newHost);
        Place newPlace = PlaceGenerator.generate("place", "0000000000");
        placeService.register(host, newPlace, null);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .delete("/places/1")
        .then()
                .statusCode(OK.value());
    }
}