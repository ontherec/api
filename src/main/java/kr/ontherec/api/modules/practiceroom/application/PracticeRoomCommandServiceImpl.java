package kr.ontherec.api.modules.practiceroom.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.practiceroom.dao.PracticeRoomRepository;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomUpdateRequestDto;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import kr.ontherec.api.modules.practiceroom.exception.PracticeRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.practiceroom.exception.PracticeRoomExceptionCode.EXIST_BRN;

@Service @RequiredArgsConstructor
@Transactional
public class PracticeRoomCommandServiceImpl implements PracticeRoomCommandService {
    private final PracticeRoomRepository practiceRoomRepository;
    private final PracticeRoomMapper practiceRoomMapper = PracticeRoomMapper.INSTANCE;

    @Override
    public PracticeRoom register(Host host, PracticeRoom newPracticeRoom) {
        if(practiceRoomRepository.existsByBrn(newPracticeRoom.getBrn()))
            throw new PracticeRoomException(EXIST_BRN);

        newPracticeRoom.setHost(host);
        return practiceRoomRepository.save(newPracticeRoom);
    }

    @Override
    public void updateImages(Long id, PracticeRoomUpdateRequestDto.Images dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateImages(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateIntroduction(Long id, PracticeRoomUpdateRequestDto.Introduction dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateIntroduction(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateArea(Long id, PracticeRoomUpdateRequestDto.Area dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateArea(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateBusiness(Long id, PracticeRoomUpdateRequestDto.Business dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateBusiness(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateParking(Long id, PracticeRoomUpdateRequestDto.Parking dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateParking(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateFacilities(Long id, PracticeRoomUpdateRequestDto.Facilities dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateFacilities(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void updateFnbPolicies(Long id, PracticeRoomUpdateRequestDto.FnbPolicies dto) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        practiceRoomMapper.updateFnbPolicies(dto, foundPracticeRoom);
        practiceRoomRepository.save(foundPracticeRoom);
    }

    @Override
    public void like(Long id, String username) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        foundPracticeRoom.getLikedUsernames().add(username);
    }

    @Override
    public void unlike(Long id, String username) {
        PracticeRoom foundPracticeRoom = practiceRoomRepository.findByIdOrThrow(id);
        foundPracticeRoom.getLikedUsernames().remove(username);
    }

    @Override
    public void delete(Long id) {
        practiceRoomRepository.deleteByIdOrThrow(id);
    }
}
