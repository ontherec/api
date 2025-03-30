package kr.ontherec.api.modules.place.presentation;

import jakarta.validation.Valid;
import kr.ontherec.api.modules.host.application.HostService;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.application.PlaceCommandService;
import kr.ontherec.api.modules.place.application.PlaceMapper;
import kr.ontherec.api.modules.place.application.PlaceQueryService;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.modules.place.dto.PlaceResponseDto;
import kr.ontherec.api.modules.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.modules.place.exception.PlaceException;
import kr.ontherec.api.modules.place.exception.PlaceExceptionCode;
import kr.ontherec.api.modules.tag.application.TagService;
import kr.ontherec.api.modules.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/places")
@RequiredArgsConstructor
public class PlaceController {
    private final HostService hostService;
    private final PlaceQueryService placeQueryService;
    private final PlaceCommandService placeCommandService;
    private final PlaceMapper placeMapper = PlaceMapper.INSTANCE;
    private final TagService tagService;

    @GetMapping
    ResponseEntity<List<PlaceResponseDto>> search(@RequestParam(value = "q", required = false) String query) {
        List<Place> places = placeQueryService.search(query);
        List<PlaceResponseDto> dtos = places.stream().map(placeMapper::EntityToResponseDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<PlaceResponseDto> get(@PathVariable Long id) {
        Place place = placeQueryService.get(id);
        PlaceResponseDto dto = placeMapper.EntityToResponseDto(place);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    ResponseEntity<Long> register(Authentication authentication, @Valid @RequestBody PlaceRegisterRequestDto dto) {
        Host host = hostService.getByUsername(authentication.getName());
        Place newPlace = placeMapper.registerRequestDtoToEntity(dto);
        Set<Tag> tags = dto.introduction().tags() == null ? null : dto.introduction().tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        Place place = placeCommandService.register(host, newPlace, tags);
        return ResponseEntity.created(URI.create("/v1/places/" + place.getId())).body(place.getId());
    }

    @PutMapping("/{id}/introduction")
    ResponseEntity<Void> updateIntroduction(Authentication authentication,
                                        @PathVariable Long id,
                                        @Valid @RequestBody PlaceUpdateRequestDto.Introduction dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeQueryService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        placeCommandService.updateIntroduction(id, dto, tags);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/business")
    ResponseEntity<Void> updateBusiness(Authentication authentication,
                                        @PathVariable Long id,
                                        @Valid @RequestBody PlaceUpdateRequestDto.Business dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeQueryService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        placeCommandService.updateBusiness(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/parking")
    ResponseEntity<Void> updateParking(Authentication authentication,
                                        @PathVariable Long id,
                                        @Valid @RequestBody PlaceUpdateRequestDto.Parking dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeQueryService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        placeCommandService.updateParking(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> remove(Authentication authentication, @PathVariable Long id) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeQueryService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        placeCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
