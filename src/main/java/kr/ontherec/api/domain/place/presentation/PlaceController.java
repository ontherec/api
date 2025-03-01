package kr.ontherec.api.domain.place.presentation;

import jakarta.validation.Valid;
import kr.ontherec.api.domain.host.application.HostService;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.application.PlaceMapper;
import kr.ontherec.api.domain.place.application.PlaceService;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceResponseDto;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
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
@RequestMapping("/v1/places")
@RequiredArgsConstructor
public class PlaceController {
    private final HostService hostService;
    private final PlaceService placeService;
    private final PlaceMapper placeMapper = PlaceMapper.INSTANCE;
    private final TagService tagService;

    @GetMapping
    ResponseEntity<List<PlaceResponseDto>> search(@RequestParam(value = "q", required = false) String query) {
        List<Place> places = placeService.search(query);
        List<PlaceResponseDto> dtos = places.stream().map(placeMapper::EntityToResponseDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<PlaceResponseDto> get(@PathVariable Long id) {
        Place place = placeService.get(id);
        PlaceResponseDto dto = placeMapper.EntityToResponseDto(place);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    ResponseEntity<Long> register(Authentication authentication, @Valid @RequestBody PlaceRegisterRequestDto dto) {
        Host host = hostService.getByUsername(authentication.getName());
        Place newPlace = placeMapper.registerRequestDtoToEntity(dto);
        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        Place place = placeService.register(host, newPlace, tags);
        return ResponseEntity.created(URI.create("/v1/places/" + place.getId())).body(place.getId());
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody PlaceUpdateRequestDto dto) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        Place newPlace = placeMapper.updateRequestDtoToEntity(dto);
        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        placeService.update(id, newPlace, tags);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> remove(Authentication authentication, @PathVariable Long id) {
        Host host = hostService.getByUsername(authentication.getName());
        if (!placeService.isHost(id, host))
            throw new PlaceException(PlaceExceptionCode.FORBIDDEN);

        placeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
