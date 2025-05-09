package kr.ontherec.api.modules.host.presentation;

import jakarta.validation.Valid;
import kr.ontherec.api.modules.host.application.HostMapper;
import kr.ontherec.api.modules.host.application.HostService;
import kr.ontherec.api.modules.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.modules.host.dto.HostResponseDto;
import kr.ontherec.api.modules.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.modules.host.entity.Host;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/hosts")
@RequiredArgsConstructor
public class HostController {
    private final HostService hostService;
    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @PostMapping
    ResponseEntity<Long> register(
            Authentication authentication,
            @Valid @RequestBody HostRegisterRequestDto dto
    ) {
        Host newHost = hostMapper.registerRequestDtoToEntity(authentication.getName(), dto);
        Host savedHost = hostService.register(newHost);
        return ResponseEntity.created(URI.create("/v1/hosts/me")).body(savedHost.getId());
    }

    @GetMapping("/{id}")
    ResponseEntity<HostResponseDto> get(@PathVariable Long id) {
        Host host = hostService.get(id);
        HostResponseDto dto = hostMapper.entityToResponseDto(host);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/me")
    ResponseEntity<Void> update(
            Authentication authentication,
            @Valid @RequestBody HostUpdateRequestDto dto
    ) {
        Host foundHost = hostService.getByUsername(authentication.getName());
        hostService.update(foundHost, dto);
        return ResponseEntity.ok().build();
    }
}
