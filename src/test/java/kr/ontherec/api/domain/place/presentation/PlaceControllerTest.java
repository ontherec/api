package kr.ontherec.api.domain.place.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.AddressRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceResponseDto;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static kr.ontherec.api.domain.place.domain.HolidayType.설날;
import static kr.ontherec.api.domain.place.domain.HolidayType.추석;
import static kr.ontherec.api.domain.place.exception.PlaceExceptionCode.*;
import static kr.ontherec.api.global.config.SecurityConfig.API_KEY_HEADER;
import static kr.ontherec.api.global.model.Regex.BUSINESS_REGISTRATION_NUMBER;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.snippet.Attributes.key;

@IntegrationTest
class PlaceControllerTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;

    @Value("${spring.security.api-key}")
    private String API_KEY;

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1/places";
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

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        placeFactory.create(host, "place", "0000000000", tags);

        given(this.spec)
                .filter(document(
                        "place search",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place search")
                                .description("플레이스 검색")
                                .responseSchema(Schema.schema(PlaceResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("플레이스 배열"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("플레이스 식별자"),
                                        fieldWithPath("[].host")
                                                .type(OBJECT)
                                                .description("호스트"),
                                        fieldWithPath("[].host.id")
                                                .type(NUMBER)
                                                .description("호스트 식별자"),
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
                                                .description("평균 응답 시간 (ISO 8601 Duration)")
                                                .optional(),
                                        fieldWithPath("[].brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
                                        fieldWithPath("[].address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("[].address.id")
                                                .type(NUMBER)
                                                .description("주소 식별자"),
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
                                        // introduction
                                        fieldWithPath("[].introduction")
                                                .type(OBJECT)
                                                .description("플레이스 소개"),
                                        fieldWithPath("[].introduction.title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("[].introduction.content")
                                                .type(STRING)
                                                .description("소개 글")
                                                .optional(),
                                        fieldWithPath("[].introduction.tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("태그 목록")
                                                .optional(),
                                        fieldWithPath("[].introduction.links[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("링크 목록")
                                                .optional(),
                                        // business
                                        fieldWithPath("[].business")
                                                .type(OBJECT)
                                                .description("플레이스 영업 정보"),
                                        fieldWithPath("[].business.bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("[].business.bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("[].business.holidays[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value("enum"))
                                                .description("공휴일 목록"),
                                        // parking
                                        fieldWithPath("[].parking")
                                                .type(OBJECT)
                                                .description("플레이스 주차 정보"),
                                        fieldWithPath("[].parking.capacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("[].parking.location")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("[].parking.free")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        // time
                                        fieldWithPath("[].createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)"),
                                        fieldWithPath("[].modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)"))
                                .build())))
        .when()
                .get()
        .then()
                .statusCode(OK.value());

    }

    @DisplayName("플레이스 등록 성공")
    @Test
    void register() {

        hostFactory.create("test");

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
                addressDto,
                new PlaceRegisterRequestDto.Introduction(
                        "place",
                        "place",
                        Set.of("tag"),
                        Set.of("https://ontherec.kr")
                ),
                new PlaceRegisterRequestDto.Business(
                        Duration.ofDays(30),
                        Duration.ofDays(1),
                        Set.of(설날)
                ),
                new PlaceRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                )
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
                .filter(document(
                        "place register",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place register")
                                .description("플레이스 등록")
                                .requestSchema(Schema.schema(PlaceRegisterRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
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
                                        // introduction
                                        fieldWithPath("introduction")
                                                .type(OBJECT)
                                                .description("플레이스 소개 정보"),
                                        fieldWithPath("introduction.title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("introduction.content")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("introduction.tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("태그 목록")
                                                .optional(),
                                        fieldWithPath("introduction.links[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("링크 목록")
                                                .optional(),
                                        // business
                                        fieldWithPath("business")
                                                .type(OBJECT)
                                                .description("플레이스 영업 정보"),
                                        fieldWithPath("business.bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("business.bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("business.holidays[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value("enum"))
                                                .description("공휴일 목록")
                                                .optional(),
                                        // parking
                                        fieldWithPath("parking")
                                                .type(OBJECT)
                                                .description("플레이스 주차 정보"),
                                        fieldWithPath("parking.capacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("parking.location")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("parking.free")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional())
                                .build())))
        .when()
                .post()
        .then()
                .statusCode(CREATED.value())
                .header("Location", startsWith("/v1/places"))
                .body(notNullValue());

    }

    @Test
    @DisplayName("플레이스 등록 실패 - 중복된 사업자등록번호")
    void registerWithDuplicatedBrn() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        placeFactory.create(host, "place", "0000000000", tags);

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
                addressDto,
                new PlaceRegisterRequestDto.Introduction(
                        "place",
                        "place",
                        Set.of("tag"),
                        Set.of("https://ontherec.kr")
                ),
                new PlaceRegisterRequestDto.Business(
                        Duration.ofDays(30),
                        Duration.ofDays(1),
                        Set.of(설날)
                ),
                new PlaceRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                )
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
        .when()
                .post()
        .then()
                .statusCode(EXIST_BRN.getStatus().value())
                .body("message", equalTo(EXIST_BRN.getMessage()));
    }

    @Test
    @DisplayName("플레이스 등록 실패 - 유효하지 않은 예약 기간")
    void registerWithInvalidBookingPeriod() {

        hostFactory.create("test");

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
                addressDto,
                new PlaceRegisterRequestDto.Introduction(
                        "place",
                        "place",
                        Set.of("tag"),
                        Set.of("https://ontherec.kr")
                ),
                new PlaceRegisterRequestDto.Business(
                        Duration.ofDays(30),
                        Duration.ofDays(30),
                        Set.of(설날)
                ),
                new PlaceRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                )
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(placeDto)
        .when()
                .post()
        .then()
                .statusCode(NOT_VALID_BOOKING_PERIOD.getStatus().value())
                .body("message", equalTo(NOT_VALID_BOOKING_PERIOD.getMessage()));
    }

    @DisplayName("플레이스 조회 성공")
    @Test
    void get() {
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        given(this.spec)
                .filter(document(
                        "place get",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place get")
                                .description("플레이스 조회")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("조회할 플레이스 식별자"))
                                .responseSchema(Schema.schema(PlaceResponseDto.class.getSimpleName()))
                                .responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("플레이스 식별자"),
                                        fieldWithPath("host")
                                                .type(OBJECT)
                                                .description("호스트"),
                                        fieldWithPath("host.id")
                                                .type(NUMBER)
                                                .description("호스트 식별자"),
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
                                                .description("평균 응답 시간 (ISO 8601 Duration)")
                                                .optional(),
                                        fieldWithPath("brn")
                                                .type(STRING)
                                                .description("사업자 등록번호 (" + BUSINESS_REGISTRATION_NUMBER + ")"),
                                        // address
                                        fieldWithPath("address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("address.id")
                                                .type(NUMBER)
                                                .description("주소 식별자"),
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
                                        // introduction
                                        fieldWithPath("introduction")
                                                .type(OBJECT)
                                                .description("플레이스 소개 정보"),
                                        fieldWithPath("introduction.title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("introduction.content")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("introduction.tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("태그 목록")
                                                .optional(),
                                        fieldWithPath("introduction.links[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("링크 목록")
                                                .optional(),
                                        // business
                                        fieldWithPath("business")
                                                .type(OBJECT)
                                                .description("플레이스 영업 정보"),
                                        fieldWithPath("business.bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("business.bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("business.holidays[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value("enum"))
                                                .description("공휴일 목록")
                                                .optional(),
                                        // parking
                                        fieldWithPath("parking")
                                                .type(OBJECT)
                                                .description("플레이스 주차 정보"),
                                        fieldWithPath("parking.capacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("parking.location")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("parking.free")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        // time
                                        fieldWithPath("createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)"),
                                        fieldWithPath("modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)"))
                                .build())))
        .when()
                .get("/{id}", place.getId())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(place.getId().intValue()));

    }

    @DisplayName("플레이스 소개 수정 성공")
    @Test
    void updateIntroduction() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        PlaceUpdateRequestDto.Introduction dto = new PlaceUpdateRequestDto.Introduction(
                "newPlace",
                "newPlace",
                Set.of("newTag"),
                Set.of("https://ontherec.live")
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "place update introduction",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place update introduction")
                                .description("플레이스 소개 수정")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("소개를 수정할 플레이스 식별자"))
                                .requestSchema(Schema.schema(PlaceUpdateRequestDto.Introduction.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("태그 목록")
                                                .optional(),
                                        fieldWithPath("links[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("링크 목록")
                                                .optional())
                                .build())))
        .when()
                .put("/{id}/introduction", place.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("플레이스 소개 실패 - 권한 없음")
    @Test
    void updateIntroductionWithoutAuthority() {

        hostFactory.create("test");
        Host host = hostFactory.create("host");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        PlaceUpdateRequestDto.Introduction dto = new PlaceUpdateRequestDto.Introduction(
                "newPlace",
                "newPlace",
                Set.of("newTag"),
                Set.of("https://ontherec.live")
        );

        given()
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
        .when()
                .put("/{id}/introduction", place.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }

    @DisplayName("플레이스 영업 정보 수정 성공")
    @Test
    void updateBusiness() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        PlaceUpdateRequestDto.Business dto = new PlaceUpdateRequestDto.Business(
                Duration.ofDays(90),
                Duration.ofDays(7),
                Set.of(추석)
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "place update business",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place update business")
                                .description("플레이스 영업 정보 수정")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("영업 정보를 수정할 플레이스 식별자"))
                                .requestSchema(Schema.schema(PlaceUpdateRequestDto.Business.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("bookingFrom")
                                                .type(STRING)
                                                .description("예약 시작 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("bookingUntil")
                                                .type(STRING)
                                                .description("예약 마감 기간 (ISO 8601 Duration)"),
                                        fieldWithPath("holidays[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value("enum"))
                                                .description("공휴일 목록")
                                                .optional())
                                .build())))
        .when()
                .put("/{id}/business", place.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("플레이스 주차 정보 수정 성공")
    @Test
    void updateParking() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        PlaceUpdateRequestDto.Parking dto = new PlaceUpdateRequestDto.Parking(
                30,
                "건물 건너편 주차장",
                false
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "place update parking",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place update parking")
                                .description("플레이스 주차 정보 수정")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("주차 정보를 수정할 플레이스 식별자"))
                                .requestSchema(Schema.schema(PlaceUpdateRequestDto.Parking.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("capacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("location")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("free")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional())
                                .build())))
        .when()
                .put("/{id}/parking", place.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("플레이스 삭제 성공")
    @Test
    void remove() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "place delete",
                        resource(ResourceSnippetParameters.builder()
                                .tag("place")
                                .summary("place delete")
                                .description("플레이스 삭제")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("삭제할 플레이스 식별자"))
                                .build())))
        .when()
                .delete("/{id}", place.getId())
        .then()
                .statusCode(OK.value());
    }
}