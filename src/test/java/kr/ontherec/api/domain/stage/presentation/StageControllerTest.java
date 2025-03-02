package kr.ontherec.api.domain.stage.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.domain.StageType;
import kr.ontherec.api.domain.stage.dto.*;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
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
import java.util.Arrays;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static kr.ontherec.api.domain.stage.domain.StageType.RECTANGLE;
import static kr.ontherec.api.domain.stage.domain.StageType.T;
import static kr.ontherec.api.domain.stage.exception.StageExceptionCode.FORBIDDEN;
import static kr.ontherec.api.global.config.SecurityConfig.API_KEY_HEADER;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.snippet.Attributes.key;

@IntegrationTest
class StageControllerTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;
    @Autowired private StageFactory stageFactory;

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

    @DisplayName("공연장 검색 성공")
    @Test
    void search() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        stageFactory.create(place, "stage", tags);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "search",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("search")
                                .description("공연장 검색")
                                .responseSchema(Schema.schema(StageResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("공연장 배열"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("공연장 식별자"),
                                        // place
                                        fieldWithPath("[].place")
                                                .type(OBJECT)
                                                .description("공연장을 운영하는 플레이스"),
                                        fieldWithPath("[].place.id")
                                                .type(NUMBER)
                                                .description("플레이스 식별자"),
                                        fieldWithPath("[].place.title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("[].place.address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("[].place.address.id")
                                                .type(NUMBER)
                                                .description("주소 식별자"),
                                        fieldWithPath("[].place.address.zipcode")
                                                .type(STRING)
                                                .description("우편번호"),
                                        fieldWithPath("[].place.address.state")
                                                .type(STRING)
                                                .description("도/시"),
                                        fieldWithPath("[].place.address.city")
                                                .type(STRING)
                                                .description("시/군/구"),
                                        fieldWithPath("[].place.address.streetAddress")
                                                .type(STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("[].place.address.detail")
                                                .type(STRING)
                                                .description("상세 주소")
                                                .optional(),
                                        fieldWithPath("[].place.address.latitude")
                                                .type(NUMBER)
                                                .description("위도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("[].place.address.longitude")
                                                .type(NUMBER)
                                                .description("경도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("[].place.tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("플레이스 태그 목록")
                                                .optional(),
                                        // location
                                        fieldWithPath("[].title")
                                                .type(STRING)
                                                .description("공연장 이름"),
                                        fieldWithPath("[].floor")
                                                .type(NUMBER)
                                                .description("층수"),
                                        fieldWithPath("[].hasElevator")
                                                .type(BOOLEAN)
                                                .description("엘리베이터 존재 여부"),
                                        // introduction
                                        fieldWithPath("[].introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("[].guide")
                                                .type(STRING)
                                                .description("이용 안내")
                                                .optional(),
                                        fieldWithPath("[].tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("공연장 태그 목록")
                                                .optional(),
                                        // count
                                        fieldWithPath("[].viewCount")
                                                .type(NUMBER)
                                                .description("조회수"),
                                        fieldWithPath("[].likeCount")
                                                .type(STRING)
                                                .description("좋아요 수"),
                                        // area
                                        fieldWithPath("[].minCapacity")
                                                .type(NUMBER)
                                                .description("최소 수용인원 (좌석 기준)"),
                                        fieldWithPath("[].maxCapacity")
                                                .type(NUMBER)
                                                .description("최대 수용인원 (스탠딩 기준)"),
                                        fieldWithPath("[].stageType")
                                                .type(STRING)
                                                .description("무대 타입 - " + Arrays.toString(StageType.values())),
                                        fieldWithPath("[].stageWidth")
                                                .type(NUMBER)
                                                .description("무대 가로 길이"),
                                        fieldWithPath("[].stageHeight")
                                                .type(NUMBER)
                                                .description("무대 세로 길이"),
                                        // business
                                        fieldWithPath("[].refundPolicies[]")
                                                .type(ARRAY)
                                                .description("환불 정책 목록")
                                                .optional(),
                                        fieldWithPath("[].refundPolicies[].id")
                                                .type(NUMBER)
                                                .description("환불 정책 식별자"),
                                        fieldWithPath("[].refundPolicies[].dayBefore")
                                                .type(STRING)
                                                .description("환불 기한"),
                                        fieldWithPath("[].refundPolicies[].percent")
                                                .type(NUMBER)
                                                .description("환불 비율"),
                                        // engineering
                                        fieldWithPath("[].stageManagingAvailable")
                                                .type(BOOLEAN)
                                                .description("스테이지 매니징 제공 여부"),
                                        fieldWithPath("[].stageManagingFee")
                                                .type(NUMBER)
                                                .description("스테이지 매니징 비용")
                                                .optional(),
                                        fieldWithPath("[].soundEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("사운드 엔지니어링 제공 여부"),
                                        fieldWithPath("[].soundEngineeringFee")
                                                .type(NUMBER)
                                                .description("사운드 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("[].lightEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("조명 엔지니어링 제공 여부"),
                                        fieldWithPath("[].lightEngineeringFee")
                                                .type(NUMBER)
                                                .description("조명 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("[].photographingAvailable")
                                                .type(BOOLEAN)
                                                .description("촬영 제공 여부"),
                                        fieldWithPath("[].photographingFee")
                                                .type(NUMBER)
                                                .description("촬영 비용")
                                                .optional(),
                                        // documents
                                        fieldWithPath("[].applicationForm")
                                                .type(STRING)
                                                .description("대관 신청서 양식 URL")
                                                .optional(),
                                        fieldWithPath("[].cueSheetTemplate")
                                                .type(STRING)
                                                .description("큐시트 양식 URL"),
                                        fieldWithPath("[].cueSheetDue")
                                                .type(STRING)
                                                .description("큐시트 제출 마감 기한"),
                                        // facilities
                                        fieldWithPath("[].parkingCapacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("[].parkingLocation")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("[].freeParking")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        fieldWithPath("[].hasRestroom")
                                                .type(BOOLEAN)
                                                .description("화장실 존재 여부"),
                                        fieldWithPath("[].hasWifi")
                                                .type(BOOLEAN)
                                                .description("와이파이 제공 여부"),
                                        fieldWithPath("[].hasCameraStanding")
                                                .type(BOOLEAN)
                                                .description("카메라 스탠드 제공 여부"),
                                        fieldWithPath("[].hasWaitingRoom")
                                                .type(BOOLEAN)
                                                .description("대기실 존재 여부"),
                                        fieldWithPath("[].hasProjector")
                                                .type(BOOLEAN)
                                                .description("프로젝터 존재 여부"),
                                        fieldWithPath("[].hasLocker")
                                                .type(BOOLEAN)
                                                .description("물품보관함 존재 여부"),
                                        // fnb policies
                                        fieldWithPath("[].allowsWater")
                                                .type(BOOLEAN)
                                                .description("물 반입 허용 여부"),
                                        fieldWithPath("[].allowsDrink")
                                                .type(BOOLEAN)
                                                .description("음료 반입 허용 여부"),
                                        fieldWithPath("[].allowsFood")
                                                .type(BOOLEAN)
                                                .description("음식 반입 허용 여부"),
                                        fieldWithPath("[].allowsFoodDelivery")
                                                .type(BOOLEAN)
                                                .description("음식 배달 허용 여부"),
                                        fieldWithPath("[].allowsAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 반입 여부"),
                                        fieldWithPath("[].sellDrink")
                                                .type(BOOLEAN)
                                                .description("음료 판매 여부"),
                                        fieldWithPath("[].sellAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 판매 여부"))
                                .build())))
        .when()
                .get("/stages")
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 조회 성공")
    @Test
    void get() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "get",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("get")
                                .description("공연장 조회")
                                .responseSchema(Schema.schema(StageResponseDto.class.getSimpleName()))
                                .responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("공연장 식별자"),
                                        // place
                                        fieldWithPath("place")
                                                .type(OBJECT)
                                                .description("공연장을 운영하는 플레이스"),
                                        fieldWithPath("place.id")
                                                .type(NUMBER)
                                                .description("플레이스 식별자"),
                                        fieldWithPath("place.title")
                                                .type(STRING)
                                                .description("플레이스 이름"),
                                        fieldWithPath("place.address")
                                                .type(OBJECT)
                                                .description("주소"),
                                        fieldWithPath("place.address.id")
                                                .type(NUMBER)
                                                .description("주소 식별자"),
                                        fieldWithPath("place.address.zipcode")
                                                .type(STRING)
                                                .description("우편번호"),
                                        fieldWithPath("place.address.state")
                                                .type(STRING)
                                                .description("도/시"),
                                        fieldWithPath("place.address.city")
                                                .type(STRING)
                                                .description("시/군/구"),
                                        fieldWithPath("place.address.streetAddress")
                                                .type(STRING)
                                                .description("도로명 주소"),
                                        fieldWithPath("place.address.detail")
                                                .type(STRING)
                                                .description("상세 주소")
                                                .optional(),
                                        fieldWithPath("place.address.latitude")
                                                .type(NUMBER)
                                                .description("위도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("place.address.longitude")
                                                .type(NUMBER)
                                                .description("경도 (정수 3자리 이하, 소수 13자리 이하)"),
                                        fieldWithPath("place.tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("플레이스 태그 목록")
                                                .optional(),
                                        // location
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("공연장 이름"),
                                        fieldWithPath("floor")
                                                .type(NUMBER)
                                                .description("층수"),
                                        fieldWithPath("hasElevator")
                                                .type(BOOLEAN)
                                                .description("엘리베이터 존재 여부"),
                                        // introduction
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("guide")
                                                .type(STRING)
                                                .description("이용 안내")
                                                .optional(),
                                        fieldWithPath("tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("공연장 태그 목록")
                                                .optional(),
                                        // count
                                        fieldWithPath("viewCount")
                                                .type(NUMBER)
                                                .description("조회수"),
                                        fieldWithPath("likeCount")
                                                .type(STRING)
                                                .description("좋아요 수"),
                                        // area
                                        fieldWithPath("minCapacity")
                                                .type(NUMBER)
                                                .description("최소 수용인원 (좌석 기준)"),
                                        fieldWithPath("maxCapacity")
                                                .type(NUMBER)
                                                .description("최대 수용인원 (스탠딩 기준)"),
                                        fieldWithPath("stageType")
                                                .type(STRING)
                                                .description("무대 타입 - " + Arrays.toString(StageType.values())),
                                        fieldWithPath("stageWidth")
                                                .type(NUMBER)
                                                .description("무대 가로 길이"),
                                        fieldWithPath("stageHeight")
                                                .type(NUMBER)
                                                .description("무대 세로 길이"),
                                        // business
                                        fieldWithPath("refundPolicies[]")
                                                .type(ARRAY)
                                                .description("환불 정책 목록")
                                                .optional(),
                                        fieldWithPath("refundPolicies[].id")
                                                .type(NUMBER)
                                                .description("환불 정책 식별자"),
                                        fieldWithPath("refundPolicies[].dayBefore")
                                                .type(STRING)
                                                .description("환불 기한"),
                                        fieldWithPath("refundPolicies[].percent")
                                                .type(NUMBER)
                                                .description("환불 비율"),
                                        // engineering
                                        fieldWithPath("stageManagingAvailable")
                                                .type(BOOLEAN)
                                                .description("스테이지 매니징 제공 여부"),
                                        fieldWithPath("stageManagingFee")
                                                .type(NUMBER)
                                                .description("스테이지 매니징 비용")
                                                .optional(),
                                        fieldWithPath("soundEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("사운드 엔지니어링 제공 여부"),
                                        fieldWithPath("soundEngineeringFee")
                                                .type(NUMBER)
                                                .description("사운드 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("lightEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("조명 엔지니어링 제공 여부"),
                                        fieldWithPath("lightEngineeringFee")
                                                .type(NUMBER)
                                                .description("조명 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("photographingAvailable")
                                                .type(BOOLEAN)
                                                .description("촬영 제공 여부"),
                                        fieldWithPath("photographingFee")
                                                .type(NUMBER)
                                                .description("촬영 비용")
                                                .optional(),
                                        // documents
                                        fieldWithPath("applicationForm")
                                                .type(STRING)
                                                .description("대관 신청서 양식 URL")
                                                .optional(),
                                        fieldWithPath("cueSheetTemplate")
                                                .type(STRING)
                                                .description("큐시트 양식 URL"),
                                        fieldWithPath("cueSheetDue")
                                                .type(STRING)
                                                .description("큐시트 제출 마감 기한"),
                                        // facilities
                                        fieldWithPath("parkingCapacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("parkingLocation")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("freeParking")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        fieldWithPath("hasRestroom")
                                                .type(BOOLEAN)
                                                .description("화장실 존재 여부"),
                                        fieldWithPath("hasWifi")
                                                .type(BOOLEAN)
                                                .description("와이파이 제공 여부"),
                                        fieldWithPath("hasCameraStanding")
                                                .type(BOOLEAN)
                                                .description("카메라 스탠드 제공 여부"),
                                        fieldWithPath("hasWaitingRoom")
                                                .type(BOOLEAN)
                                                .description("대기실 존재 여부"),
                                        fieldWithPath("hasProjector")
                                                .type(BOOLEAN)
                                                .description("프로젝터 존재 여부"),
                                        fieldWithPath("hasLocker")
                                                .type(BOOLEAN)
                                                .description("물품보관함 존재 여부"),
                                        // fnb policies
                                        fieldWithPath("allowsWater")
                                                .type(BOOLEAN)
                                                .description("물 반입 허용 여부"),
                                        fieldWithPath("allowsDrink")
                                                .type(BOOLEAN)
                                                .description("음료 반입 허용 여부"),
                                        fieldWithPath("allowsFood")
                                                .type(BOOLEAN)
                                                .description("음식 반입 허용 여부"),
                                        fieldWithPath("allowsFoodDelivery")
                                                .type(BOOLEAN)
                                                .description("음식 배달 허용 여부"),
                                        fieldWithPath("allowsAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 반입 여부"),
                                        fieldWithPath("sellDrink")
                                                .type(BOOLEAN)
                                                .description("음료 판매 여부"),
                                        fieldWithPath("sellAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 판매 여부"))
                                .build())))
        .when()
                .get("/stages/{id}", stage.getId())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(stage.getId().intValue()));
    }

    @DisplayName("공연장 등록 성공")
    @Test
    void register() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                place.getId(),
                // location
                "stage",
                -1,
                false,
                // introduction
                "stage",
                "stage",
                Set.of("tag"),
                // area
                60,
                120,
                RECTANGLE,
                BigDecimal.valueOf(10.5),
                BigDecimal.valueOf(5),
                // business
                Set.of(new RefundPolicyRegisterRequestDto(
                        Duration.ofDays(30),
                        BigDecimal.valueOf(33.3)
                )),
                // engineering
                false,
                null,
                true,
                100000L,
                false,
                null,
                true,
                100000L,
                // documents
                null,
                "https://docs.google.com/document",
                Duration.ofDays(3),
                // facilities
                2,
                "건물 뒤편",
                true,
                true,
                true,
                true,
                false,
                true,
                false,
                // fnb policies
                true,
                false,
                false,
                false,
                false,
                true,
                false
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "register",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("register")
                                .description("공연장 등록")
                                .requestSchema(Schema.schema(StageRegisterRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("placeId")
                                                .type(NUMBER)
                                                .description("공연장을 등록할 플레이스 식별자"),
                                        // location
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("공연장 이름"),
                                        fieldWithPath("floor")
                                                .type(NUMBER)
                                                .description("층수"),
                                        fieldWithPath("hasElevator")
                                                .type(BOOLEAN)
                                                .description("엘리베이터 존재 여부"),
                                        // introduction
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("guide")
                                                .type(STRING)
                                                .description("이용 안내")
                                                .optional(),
                                        fieldWithPath("tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("공연장 태그 목록")
                                                .optional(),
                                        // area
                                        fieldWithPath("minCapacity")
                                                .type(NUMBER)
                                                .description("최소 수용인원 (좌석 기준)"),
                                        fieldWithPath("maxCapacity")
                                                .type(NUMBER)
                                                .description("최대 수용인원 (스탠딩 기준)"),
                                        fieldWithPath("stageType")
                                                .type(STRING)
                                                .description("무대 타입 - " + Arrays.toString(StageType.values())),
                                        fieldWithPath("stageWidth")
                                                .type(NUMBER)
                                                .description("무대 가로 길이"),
                                        fieldWithPath("stageHeight")
                                                .type(NUMBER)
                                                .description("무대 세로 길이"),
                                        // business
                                        fieldWithPath("refundPolicies[]")
                                                .type(ARRAY)
                                                .description("환불 정책 목록")
                                                .optional(),
                                        fieldWithPath("refundPolicies[].dayBefore")
                                                .type(STRING)
                                                .description("환불 기한"),
                                        fieldWithPath("refundPolicies[].percent")
                                                .type(NUMBER)
                                                .description("환불 비율"),
                                        // engineering
                                        fieldWithPath("stageManagingAvailable")
                                                .type(BOOLEAN)
                                                .description("스테이지 매니징 제공 여부"),
                                        fieldWithPath("stageManagingFee")
                                                .type(NUMBER)
                                                .description("스테이지 매니징 비용")
                                                .optional(),
                                        fieldWithPath("soundEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("사운드 엔지니어링 제공 여부"),
                                        fieldWithPath("soundEngineeringFee")
                                                .type(NUMBER)
                                                .description("사운드 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("lightEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("조명 엔지니어링 제공 여부"),
                                        fieldWithPath("lightEngineeringFee")
                                                .type(NUMBER)
                                                .description("조명 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("photographingAvailable")
                                                .type(BOOLEAN)
                                                .description("촬영 제공 여부"),
                                        fieldWithPath("photographingFee")
                                                .type(NUMBER)
                                                .description("촬영 비용")
                                                .optional(),
                                        // documents
                                        fieldWithPath("applicationForm")
                                                .type(STRING)
                                                .description("대관 신청서 양식 URL")
                                                .optional(),
                                        fieldWithPath("cueSheetTemplate")
                                                .type(STRING)
                                                .description("큐시트 양식 URL"),
                                        fieldWithPath("cueSheetDue")
                                                .type(STRING)
                                                .description("큐시트 제출 마감 기한"),
                                        // facilities
                                        fieldWithPath("parkingCapacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("parkingLocation")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("freeParking")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        fieldWithPath("hasRestroom")
                                                .type(BOOLEAN)
                                                .description("화장실 존재 여부"),
                                        fieldWithPath("hasWifi")
                                                .type(BOOLEAN)
                                                .description("와이파이 제공 여부"),
                                        fieldWithPath("hasCameraStanding")
                                                .type(BOOLEAN)
                                                .description("카메라 스탠드 제공 여부"),
                                        fieldWithPath("hasWaitingRoom")
                                                .type(BOOLEAN)
                                                .description("대기실 존재 여부"),
                                        fieldWithPath("hasProjector")
                                                .type(BOOLEAN)
                                                .description("프로젝터 존재 여부"),
                                        fieldWithPath("hasLocker")
                                                .type(BOOLEAN)
                                                .description("물품보관함 존재 여부"),
                                        // fnb policies
                                        fieldWithPath("allowsWater")
                                                .type(BOOLEAN)
                                                .description("물 반입 허용 여부"),
                                        fieldWithPath("allowsDrink")
                                                .type(BOOLEAN)
                                                .description("음료 반입 허용 여부"),
                                        fieldWithPath("allowsFood")
                                                .type(BOOLEAN)
                                                .description("음식 반입 허용 여부"),
                                        fieldWithPath("allowsFoodDelivery")
                                                .type(BOOLEAN)
                                                .description("음식 배달 허용 여부"),
                                        fieldWithPath("allowsAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 반입 여부"),
                                        fieldWithPath("sellDrink")
                                                .type(BOOLEAN)
                                                .description("음료 판매 여부"),
                                        fieldWithPath("sellAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 판매 여부"))
                                .build())))
        .when()
                .post("/stages")
        .then()
                .statusCode(CREATED.value())
                .body(notNullValue());
    }

    @DisplayName("공연장 위치 정보 수정 성공")
    @Test
    void updateLocation() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Location dto = new StageUpdateRequestDto.Location(
                "newStage",
                3,
                true
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update location",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update location")
                                .description("공연장 위치 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Location.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("공연장 이름"),
                                        fieldWithPath("floor")
                                                .type(NUMBER)
                                                .description("층수"),
                                        fieldWithPath("hasElevator")
                                                .type(BOOLEAN)
                                                .description("엘리베이터 존재 여부")
                                )
                                .build())))
        .when()
                .put("/stages/{id}/location", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 위치 정보 수정 실패 - 권한 없음")
    @Test
    void updateLocationWithoutAuthority() {

        hostFactory.create("test");
        Host host = hostFactory.create("host");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Location dto = new StageUpdateRequestDto.Location(
                "newStage",
                3,
                true
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
        .when()
                .put("/stages/{id}/location", stage.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value());
    }

    @DisplayName("공연장 소개 수정 성공")
    @Test
    void updateIntroduction() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Introduction dto = new StageUpdateRequestDto.Introduction(
                "newStage",
                "newStage",
                Set.of("newTag")
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update introduction",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update introduction")
                                .description("공연장 소개 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Introduction.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("introduction")
                                                .type(STRING)
                                                .description("소개")
                                                .optional(),
                                        fieldWithPath("guide")
                                                .type(STRING)
                                                .description("이용 안내")
                                                .optional(),
                                        fieldWithPath("tags[]")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("공연장 태그 목록")
                                                .optional()
                                )
                                .build())))
        .when()
                .put("/stages/{id}/introduction", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 면적 정보 수정 성공")
    @Test
    void updateArea() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Area dto = new StageUpdateRequestDto.Area(
                100,
                200,
                T,
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(6)
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update area",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update area")
                                .description("공연장 면적 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Area.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("minCapacity")
                                                .type(NUMBER)
                                                .description("최소 수용인원 (좌석 기준)"),
                                        fieldWithPath("maxCapacity")
                                                .type(NUMBER)
                                                .description("최대 수용인원 (스탠딩 기준)"),
                                        fieldWithPath("stageType")
                                                .type(STRING)
                                                .description("무대 타입 - " + Arrays.toString(StageType.values())),
                                        fieldWithPath("stageWidth")
                                                .type(NUMBER)
                                                .description("무대 가로 길이"),
                                        fieldWithPath("stageHeight")
                                                .type(NUMBER)
                                                .description("무대 세로 길이"))
                                .build())))
        .when()
                .put("/stages/{id}/area", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 영업 정보 수정 성공")
    @Test
    void updateBusiness() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Business dto = new StageUpdateRequestDto.Business(
                Set.of(new RefundPolicyUpdateRequestDto(
                        stage.getRefundPolicies().stream().toList().get(0).getId(),
                        Duration.ofDays(15),
                        BigDecimal.valueOf(30)
                ))
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update business",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update business")
                                .description("공연장 영업 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Business.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("refundPolicies[]")
                                                .type(ARRAY)
                                                .description("환불 정책 목록")
                                                .optional(),
                                        fieldWithPath("refundPolicies[].id")
                                                .type(NUMBER)
                                                .description("환불 정책 식별자"),
                                        fieldWithPath("refundPolicies[].dayBefore")
                                                .type(STRING)
                                                .description("환불 기한"),
                                        fieldWithPath("refundPolicies[].percent")
                                                .type(NUMBER)
                                                .description("환불 비율"))
                                .build())))
        .when()
                .put("/stages/{id}/business", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 엔지니어링 정보 수정 성공")
    @Test
    void updateEngineering() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Engineering dto = new StageUpdateRequestDto.Engineering(
                true,
                50000L,
                true,
                100000L,
                true,
                100000L,
                true,
                100000L
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update engineering",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update engineering")
                                .description("공연장 엔지니어링 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Engineering.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("stageManagingAvailable")
                                                .type(BOOLEAN)
                                                .description("스테이지 매니징 제공 여부"),
                                        fieldWithPath("stageManagingFee")
                                                .type(NUMBER)
                                                .description("스테이지 매니징 비용")
                                                .optional(),
                                        fieldWithPath("soundEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("사운드 엔지니어링 제공 여부"),
                                        fieldWithPath("soundEngineeringFee")
                                                .type(NUMBER)
                                                .description("사운드 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("lightEngineeringAvailable")
                                                .type(BOOLEAN)
                                                .description("조명 엔지니어링 제공 여부"),
                                        fieldWithPath("lightEngineeringFee")
                                                .type(NUMBER)
                                                .description("조명 엔지니어링 비용")
                                                .optional(),
                                        fieldWithPath("photographingAvailable")
                                                .type(BOOLEAN)
                                                .description("촬영 제공 여부"),
                                        fieldWithPath("photographingFee")
                                                .type(NUMBER)
                                                .description("촬영 비용")
                                                .optional())
                                .build())))
        .when()
                .put("/stages/{id}/engineering", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 문서 정보 수정 성공")
    @Test
    void updateDocuments() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Documents dto = new StageUpdateRequestDto.Documents(
                "https://docs.google.com/document/u/0",
                "https://docs.google.com/document/u/0",
                Duration.ofDays(7)
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update documents",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update documents")
                                .description("공연장 문서 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Documents.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("applicationForm")
                                                .type(STRING)
                                                .description("대관 신청서 양식 URL")
                                                .optional(),
                                        fieldWithPath("cueSheetTemplate")
                                                .type(STRING)
                                                .description("큐시트 양식 URL"),
                                        fieldWithPath("cueSheetDue")
                                                .type(STRING)
                                                .description("큐시트 제출 마감 기한"))
                                .build())))
        .when()
                .put("/stages/{id}/documents", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 편의시설 정보 수정 성공")
    @Test
    void updateFacilities() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.Facilities dto = new StageUpdateRequestDto.Facilities(
                30,
                "건물 건너편 주차장",
                false,
                true,
                true,
                true,
                true,
                true,
                true
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update facilities",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update facilities")
                                .description("공연장 편의시설 정보 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Facilities.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("parkingCapacity")
                                                .type(NUMBER)
                                                .description("주차 대수"),
                                        fieldWithPath("parkingLocation")
                                                .type(STRING)
                                                .description("주차장 위치 정보")
                                                .optional(),
                                        fieldWithPath("freeParking")
                                                .type(BOOLEAN)
                                                .description("주차 무료 여부")
                                                .optional(),
                                        fieldWithPath("hasRestroom")
                                                .type(BOOLEAN)
                                                .description("화장실 존재 여부"),
                                        fieldWithPath("hasWifi")
                                                .type(BOOLEAN)
                                                .description("와이파이 제공 여부"),
                                        fieldWithPath("hasCameraStanding")
                                                .type(BOOLEAN)
                                                .description("카메라 스탠드 제공 여부"),
                                        fieldWithPath("hasWaitingRoom")
                                                .type(BOOLEAN)
                                                .description("대기실 존재 여부"),
                                        fieldWithPath("hasProjector")
                                                .type(BOOLEAN)
                                                .description("프로젝터 존재 여부"),
                                        fieldWithPath("hasLocker")
                                                .type(BOOLEAN)
                                                .description("물품보관함 존재 여부"))
                                .build())))
        .when()
                .put("/stages/{id}/facilities", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 식음료 정책 수정 성공")
    @Test
    void updateFnbPolicies() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);
        StageUpdateRequestDto.FnbPolicies dto = new StageUpdateRequestDto.FnbPolicies(
                false,
                false,
                false,
                true,
                false,
                true,
                true
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "update fnb policies",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("update fnb policies")
                                .description("공연장 식음료 정책 수정")
                                .requestSchema(Schema.schema(StageUpdateRequestDto.Location.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("allowsWater")
                                                .type(BOOLEAN)
                                                .description("물 반입 허용 여부"),
                                        fieldWithPath("allowsDrink")
                                                .type(BOOLEAN)
                                                .description("음료 반입 허용 여부"),
                                        fieldWithPath("allowsFood")
                                                .type(BOOLEAN)
                                                .description("음식 반입 허용 여부"),
                                        fieldWithPath("allowsFoodDelivery")
                                                .type(BOOLEAN)
                                                .description("음식 배달 허용 여부"),
                                        fieldWithPath("allowsAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 반입 여부"),
                                        fieldWithPath("sellDrink")
                                                .type(BOOLEAN)
                                                .description("음료 판매 여부"),
                                        fieldWithPath("sellAlcohol")
                                                .type(BOOLEAN)
                                                .description("주류 판매 여부"))
                                .build())))
        .when()
                .put("/stages/{id}/fnb-policies", stage.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("공연장 삭제 성공")
    @Test
    void remove() {

        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "delete",
                        resource(ResourceSnippetParameters.builder()
                                .tag("stage")
                                .summary("delete")
                                .description("공연장 삭제")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("삭제할 공연장 식별자"))
                                .build())))
        .when()
                .delete("/stages/{id}", stage.getId())
        .then()
                .statusCode(OK.value());
    }
}