package kr.ontherec.api.domain.stage.presentation;


import jakarta.validation.Valid;
import kr.ontherec.api.domain.host.application.HostService;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.application.PlaceQueryService;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.application.StageCommandService;
import kr.ontherec.api.domain.stage.application.StageMapper;
import kr.ontherec.api.domain.stage.application.StageQueryService;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.domain.stage.dto.StageResponseDto;
import kr.ontherec.api.domain.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.domain.stage.exception.StageException;
import kr.ontherec.api.domain.stage.exception.StageExceptionCode;
import kr.ontherec.api.domain.tag.application.TagService;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/stages")
@RequiredArgsConstructor
public class StageController {
    private final StageQueryService stageQueryService;
    private final StageCommandService stageCommandService;
    private final StageMapper stageMapper = StageMapper.INSTANCE;
    private final PlaceQueryService placeQueryService;
    private final HostService hostService;
    private final TagService tagService;

    @GetMapping
    ResponseEntity<List<StageResponseDto>> search(@RequestParam(value = "q", required = false) String query) {
        List<Stage> stages = stageQueryService.search(query);
        List<StageResponseDto> response = stages.stream().map(stageMapper::EntityToResponseDto).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<StageResponseDto> get(@PathVariable Long id) {
        Stage stage = stageQueryService.get(id);
        StageResponseDto response = stageMapper.EntityToResponseDto(stage);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<Long> register(Authentication authentication,
                                  @Valid @RequestBody StageRegisterRequestDto dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeQueryService.isHost(dto.placeId(), host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        Stage newStage = stageMapper.registerRequestDtoToEntity(dto);
        Place place = placeQueryService.get(dto.placeId());
        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        Stage stage = stageCommandService.register(place, newStage, tags);
        return ResponseEntity.created(URI.create("/v1/stages/" + stage.getId())).body(stage.getId());
    }

    @PutMapping("/{id}/location")
    ResponseEntity<Void> updateLocation(Authentication authentication,
                                        @PathVariable Long id,
                                        @Valid @RequestBody StageUpdateRequestDto.Location dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateLocation(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/area")
    ResponseEntity<Void> updateArea(Authentication authentication,
                                           @PathVariable Long id,
                                           @Valid @RequestBody StageUpdateRequestDto.Area dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateArea(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/introduction")
    ResponseEntity<Void> updateIntroduction(Authentication authentication,
                                            @PathVariable Long id,
                                            @Valid @RequestBody StageUpdateRequestDto.Introduction dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        stageCommandService.updateIntroduction(id, dto, tags);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/business")
    ResponseEntity<Void> updateBusiness(Authentication authentication,
                                        @PathVariable Long id,
                                        @Valid @RequestBody StageUpdateRequestDto.Business dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateBusiness(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/engineering")
    ResponseEntity<Void> updateEngineering(Authentication authentication,
                                           @PathVariable Long id,
                                           @Valid @RequestBody StageUpdateRequestDto.Engineering dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateEngineering(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/documents")
    ResponseEntity<Void> updateDocuments(Authentication authentication,
                                         @PathVariable Long id,
                                         @Valid @RequestBody StageUpdateRequestDto.Documents dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateDocuments(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/facilities")
    ResponseEntity<Void> updateFacilities(Authentication authentication,
                                          @PathVariable Long id,
                                          @Valid @RequestBody StageUpdateRequestDto.Facilities dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateFacilities(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/fnb-policies")
    ResponseEntity<Void> updateFnbPolicies(Authentication authentication,
                                           @PathVariable Long id,
                                           @Valid @RequestBody StageUpdateRequestDto.FnbPolicies dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateFnbPolicies(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> remove(Authentication authentication,
                                @PathVariable Long id) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
