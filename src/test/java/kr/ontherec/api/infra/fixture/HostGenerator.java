package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.host.domain.Bank;
import kr.ontherec.api.domain.host.domain.Host;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HostGenerator {

    public static Host generate(String name) {
        return Host.builder()
                .username(name)
                .bank(Bank.KB국민)
                .account("00000000000000")
                .contactFrom(LocalTime.MIDNIGHT)
                .contactUntil(LocalTime.NOON)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
