package org.szylica.model.locker;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;

@RequiredArgsConstructor
@Builder
public class Locker {
    private final Long id;
    private final Long parcelMachineId;
    private final LockerSize size;
    private final LockerStatus status;

    public boolean isLockerFree(){
        return status.equals(LockerStatus.FREE);
    }
}
