package kr.ontherec.api.modules.post.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import kr.ontherec.api.infra.IntegrationTest;
import kr.ontherec.api.infra.fixture.PostFactory;
import kr.ontherec.api.modules.post.dto.PostCreateRequestDto;
import kr.ontherec.api.modules.post.dto.PostResponseDto;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static kr.ontherec.api.infra.config.SecurityConfig.API_KEY_HEADER;
import static kr.ontherec.api.modules.post.exception.PostExceptionCode.FORBIDDEN;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.snippet.Attributes.key;

@IntegrationTest
class PostControllerTest {
    @Autowired private PostFactory postFactory;

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

    @DisplayName("게시글 검색 성공")
    @Test
    void search() {
        postFactory.create("test", "post");
        Map<String, String> params = new HashMap<>();
        params.put("q", "post");

        given(this.spec)
                .params(params)
                .filter(document(
                        "post search",
                        resource(ResourceSnippetParameters.builder()
                                .tag("post")
                                .summary("post search")
                                .description("게시글 검색")
                                .queryParameters(
                                        parameterWithName("q")
                                                .type(STRING)
                                                .description("검색어")
                                                .optional(),
                                        parameterWithName("liked")
                                                .type(BOOLEAN)
                                                .description("좋아요 여부 (로그인 필요)")
                                                .optional())
                                .responseSchema(Schema.schema(PostResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("[]")
                                                .type(ARRAY)
                                                .description("게시글 목록"),
                                        fieldWithPath("[].id")
                                                .type(NUMBER)
                                                .description("게시글 식별자"),
                                        fieldWithPath("[].author")
                                                .type(STRING)
                                                .description("게시글 작성자 ID"),
                                        fieldWithPath("[].images")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("이미지 URL 목록"),
                                        fieldWithPath("[].title")
                                                .type(STRING)
                                                .description("게시글 제목"),
                                        fieldWithPath("[].content")
                                                .type(STRING)
                                                .description("게시글 본문"),
                                        fieldWithPath("[].viewCount")
                                                .type(NUMBER)
                                                .description("조회수"),
                                        fieldWithPath("[].likeCount")
                                                .type(NUMBER)
                                                .description("좋아요 수"),
                                        fieldWithPath("[].liked")
                                                .type(BOOLEAN)
                                                .description("좋아요 여부 (미로그인시 false)"),
                                        fieldWithPath("[].createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)"),
                                        fieldWithPath("[].modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)"))
                                .build())))
        .when()
                .get("/posts")
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("게시글 조회 성공")
    @Test
    void get() {
        Post post = postFactory.create("test", "post");

        given(this.spec)
                .filter(document(
                        "post get",
                        resource(ResourceSnippetParameters.builder()
                                .tag("post")
                                .summary("post get")
                                .description("게시글 조회")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("조회할 게시글 식별자"))
                                .responseSchema(Schema.schema(PostResponseDto.class.getSimpleName() + "[]"))
                                .responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("게시글 식별자"),
                                        fieldWithPath("author")
                                                .type(STRING)
                                                .description("게시글 작성자 ID"),
                                        fieldWithPath("images")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("이미지 URL 목록"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("게시글 제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("게시글 본문"),
                                        fieldWithPath("viewCount")
                                                .type(NUMBER)
                                                .description("조회수"),
                                        fieldWithPath("likeCount")
                                                .type(NUMBER)
                                                .description("좋아요 수"),
                                        fieldWithPath("liked")
                                                .type(BOOLEAN)
                                                .description("좋아요 여부 (미로그인시 false)"),
                                        fieldWithPath("createdAt")
                                                .type(SimpleType.STRING)
                                                .description("생성된 시간 (UTC)"),
                                        fieldWithPath("modifiedAt")
                                                .type(SimpleType.STRING)
                                                .description("수정된 시간 (UTC)"))
                                .build())))
        .when()
                .get("/posts/{id}", post.getId())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(post.getId().intValue()));
    }

    @DisplayName("게시글 생성 성공")
    @Test
    void create() {
        PostCreateRequestDto dto = new PostCreateRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "post",
                "post");

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "post create",
                        resource(ResourceSnippetParameters.builder()
                                .tag("post")
                                .summary("post create")
                                .description("게시글 생성")
                                .requestSchema(Schema.schema(PostCreateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("images")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("이미지 URL 목록"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("게시글 제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("게시글 본문"))
                                .build())))
        .when()
                .post("/posts")
        .then()
                .statusCode(CREATED.value())
                .header("Location", startsWith("/v1/posts"))
                .body(notNullValue());
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void update() {
        Post post = postFactory.create("test", "post");
        PostUpdateRequestDto dto = new PostUpdateRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "post",
                "post"
        );

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
                .filter(document(
                        "post update",
                        resource(ResourceSnippetParameters.builder()
                                .tag("post")
                                .summary("post update")
                                .description("게시글 수정")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("수정할 게시글 식별자"))
                                .requestSchema(Schema.schema(PostUpdateRequestDto.class.getSimpleName()))
                                .requestFields(
                                        fieldWithPath("images")
                                                .type(ARRAY)
                                                .attributes(key("itemsType").value(STRING))
                                                .description("이미지 URL 목록"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("게시글 제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("게시글 본문"))
                                .build())))
        .when()
                .put("/posts/{id}", post.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("게시글 수정 실패 - 권한 없음")
    @Test
    void updateWithoutAuthority() {
        Post post = postFactory.create("user", "post");
        PostUpdateRequestDto dto = new PostUpdateRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "post",
                "post");

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .contentType(JSON)
                .body(dto)
        .when()
                .put("/posts/{id}", post.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void delete() {
        Post post = postFactory.create("test", "post");

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
                .filter(document(
                        "post delete",
                        resource(ResourceSnippetParameters.builder()
                                .tag("post")
                                .summary("post delete")
                                .description("게시글 삭제")
                                .pathParameters(
                                        parameterWithName("id")
                                                .type(NUMBER)
                                                .description("삭제할 게시글 식별자"))
                                .build())))
        .when()
                .delete("/posts/{id}", post.getId())
        .then()
                .statusCode(OK.value());
    }

    @DisplayName("게시글 삭제 실패 - 권한 없음")
    @Test
    void deleteWithoutAuthority() {
        Post post = postFactory.create("user", "post");

        given(this.spec)
                .header(API_KEY_HEADER, API_KEY)
        .when()
                .delete("/posts/{id}", post.getId())
        .then()
                .statusCode(FORBIDDEN.getStatus().value())
                .body("message", equalTo(FORBIDDEN.getMessage()));
    }
}