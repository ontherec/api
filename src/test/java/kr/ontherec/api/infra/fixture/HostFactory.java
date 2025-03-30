package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.host.application.HostService;
import kr.ontherec.api.modules.host.entity.Bank;
import kr.ontherec.api.modules.host.entity.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class HostFactory {
    @Autowired private HostService hostService;

    public Host create(String name) {
        Host newHost = Host.builder()
                .username(name)
                .bank(Bank.KB국민)
                .account("00000000000000")
                .contactFrom(LocalTime.MIDNIGHT)
                .contactUntil(LocalTime.NOON)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return hostService.register(newHost);
    }
}
