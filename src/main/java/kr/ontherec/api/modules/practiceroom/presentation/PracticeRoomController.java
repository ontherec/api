package kr.ontherec.api.modules.practiceroom.presentation;


import jakarta.validation.Valid;
import kr.ontherec.api.modules.host.application.HostService;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.practiceroom.application.PracticeRoomCommandService;
import kr.ontherec.api.modules.practiceroom.application.PracticeRoomMapper;
import kr.ontherec.api.modules.practiceroom.application.PracticeRoomQueryService;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomRegisterRequestDto;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomResponseDto;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomUpdateRequestDto;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import kr.ontherec.api.modules.practiceroom.exception.PracticeRoomException;
import kr.ontherec.api.modules.practiceroom.exception.PracticeRoomExceptionCode;
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
@RequestMapping("/v1/practice-rooms")
@RequiredArgsConstructor
public class PracticeRoomController {
    private final PracticeRoomQueryService practiceRoomQueryService;
    private final PracticeRoomCommandService practiceRoomCommandService;
    private final PracticeRoomMapper practiceRoomMapper = PracticeRoomMapper.INSTANCE;
    private final HostService hostService;

    @GetMapping
    ResponseEntity<List<PracticeRoomResponseDto>> search(
            @RequestParam Map<String, String> params,
            @PageableDefault(size = 12, sort = "createdAt", direction = DESC) Pageable pageable,
            Authentication authentication
    ) {
        String username = authentication == null ? null : authentication.getName();
        List<PracticeRoomResponseDto> response = practiceRoomQueryService.search(params, pageable, username)
                .stream()
                .map(practiceRoom -> practiceRoomMapper.EntityToResponseDto(practiceRoom, username))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<PracticeRoomResponseDto> get(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String username = authentication == null ? null : authentication.getName();
        PracticeRoom practiceRoom = practiceRoomQueryService.get(id);
        PracticeRoomResponseDto response = practiceRoomMapper.EntityToResponseDto(practiceRoom, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<Long> register(
            Authentication authentication,
            @Valid @RequestBody PracticeRoomRegisterRequestDto dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        PracticeRoom newPracticeRoom = practiceRoomMapper.registerRequestDtoToEntity(dto);
        PracticeRoom practiceRoom = practiceRoomCommandService.register(host, newPracticeRoom);
        return ResponseEntity.created(URI.create("/v1/practice-rooms/" + practiceRoom.getId())).body(practiceRoom.getId());
    }

    @PutMapping("/{id}/images")
    ResponseEntity<Void> updateImages(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Images dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateImages(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/introduction")
    ResponseEntity<Void> updateIntroduction(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Introduction dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);
        practiceRoomCommandService.updateIntroduction(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/area")
    ResponseEntity<Void> updateArea(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Area dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateArea(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/business")
    ResponseEntity<Void> updateBusiness(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Business dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateBusiness(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/parking")
    ResponseEntity<Void> updateParking(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Parking dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateParking(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/facilities")
    ResponseEntity<Void> updateFacilities(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.Facilities dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateFacilities(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/fnb-policies")
    ResponseEntity<Void> updateFnbPolicies(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PracticeRoomUpdateRequestDto.FnbPolicies dto
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.updateFnbPolicies(id, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    ResponseEntity<Void> like(
            Authentication authentication,
            @PathVariable Long id
    ) {
        practiceRoomCommandService.like(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unlike")
    ResponseEntity<Void> unlike(
            Authentication authentication,
            @PathVariable Long id
    ) {
        practiceRoomCommandService.unlike(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> remove(
            Authentication authentication,
            @PathVariable Long id
    ) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!practiceRoomQueryService.isHost(id, host))
            throw new PracticeRoomException(PracticeRoomExceptionCode.FORBIDDEN);

        practiceRoomCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
