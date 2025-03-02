package kr.ontherec.api.domain.host.dao;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.exception.HostException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kr.ontherec.api.domain.host.exception.HostExceptionCode.NOT_FOUND;

@Repository
@Transactional
public interface HostRepository extends JpaRepository<Host, Long> {
    boolean existsByUsername(String username);

    Optional<Host> findByUsername(String username);

    default Host findByUsernameOrThrow(String username) {
        return findByUsername(username).orElseThrow(() -> new HostException(NOT_FOUND));
    }

    default Host findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new HostException(NOT_FOUND));
    }
}
