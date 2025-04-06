package kr.ontherec.api.modules.stage.presentation;


import jakarta.validation.Valid;
import kr.ontherec.api.modules.host.application.HostService;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.application.StageCommandService;
import kr.ontherec.api.modules.stage.application.StageMapper;
import kr.ontherec.api.modules.stage.application.StageQueryService;
import kr.ontherec.api.modules.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.StageResponseDto;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import kr.ontherec.api.modules.stage.exception.StageExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/v1/stages")
@RequiredArgsConstructor
public class StageController {
    private final StageQueryService stageQueryService;
    private final StageCommandService stageCommandService;
    private final StageMapper stageMapper = StageMapper.INSTANCE;
    private final HostService hostService;

    @GetMapping
    ResponseEntity<List<StageResponseDto>> search(
            @RequestParam(required = false) String query,
            @RequestParam Map<String, String> params,
            @PageableDefault(size = 12, sort = "createdAt", direction = DESC) Pageable pageable,
            Authentication authentication
    ) {
        String username = authentication == null ? null : authentication.getName();
        List<StageResponseDto> response = stageQueryService.search(query, params, pageable, username)
                .stream()
                .map(stage -> stageMapper.EntityToResponseDto(stage, username))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<StageResponseDto> get(@PathVariable Long id, Authentication authentication) {
        String username = authentication == null ? null : authentication.getName();
        Stage stage = stageQueryService.get(id);
        StageResponseDto response = stageMapper.EntityToResponseDto(stage, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<Long> register(Authentication authentication,
                                  @Valid @RequestBody StageRegisterRequestDto dto) {
        Host host = hostService.getByUsername(authentication.getName());
        Stage newStage = stageMapper.registerRequestDtoToEntity(dto);
        Stage stage = stageCommandService.register(host, newStage);
        return ResponseEntity.created(URI.create("/v1/stages/" + stage.getId())).body(stage.getId());
    }

    @PutMapping("/{id}/images")
    ResponseEntity<Void> updateImages(Authentication authentication,
                                    @PathVariable Long id,
                                    @Valid @RequestBody StageUpdateRequestDto.Images dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateImages(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/introduction")
    ResponseEntity<Void> updateIntroduction(Authentication authentication,
                                            @PathVariable Long id,
                                            @Valid @RequestBody StageUpdateRequestDto.Introduction dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);
        stageCommandService.updateIntroduction(id, dto);
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

    @PutMapping("/{id}/parking")
    ResponseEntity<Void> updateParking(Authentication authentication,
                                          @PathVariable Long id,
                                          @Valid @RequestBody StageUpdateRequestDto.Parking dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!stageQueryService.isHost(id, host))
            throw new StageException(StageExceptionCode.FORBIDDEN);

        stageCommandService.updateParking(id, dto);
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

    @PostMapping("/{id}/like")
    ResponseEntity<Void> like(Authentication authentication, @PathVariable Long id) {
        stageCommandService.like(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unlike")
    ResponseEntity<Void> unlike(Authentication authentication, @PathVariable Long id) {
        stageCommandService.unlike(id, authentication.getName());
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
