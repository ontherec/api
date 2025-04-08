package kr.ontherec.api.modules.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record PostCreateRequestDto (
        @NotNull(message = "공연장 사진을 입력해주세요.")
        List<@URL(message = "유효하지 않은 URL 입니다") String> images,
        @NotBlank(message = "공연장 이름을 입력해주세요.")
        String title,
        @NotBlank(message = "공연장 소개를 입력해주세요.")
        @Size(max = 1000, message = "공연장 소개는 최대 1000글자까지 입력 가능합니다.")
        String content
) {}
