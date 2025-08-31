package org.szylica.data.model.locker;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.szylica.data.model.locker.enums.LockerSize;
import org.szylica.data.model.locker.enums.LockerStatus;

@RequiredArgsConstructor
@Builder
public class Locker {
    @Getter
    private final Long id;
    private final Long parcelMachineId;
    private final LockerSize size;
    private final LockerStatus status;

    public boolean isLockerFree(){
        return status.equals(LockerStatus.FREE);
    }
    public boolean isLockerInSize(LockerSize size){return this.size.equals(size); }

    public Locker withStatus(LockerStatus status){
        return Locker.builder()
                .id(this.id)
                .parcelMachineId(this.parcelMachineId)
                .size(this.size)
                .status(status)
                .build();
    }
}
