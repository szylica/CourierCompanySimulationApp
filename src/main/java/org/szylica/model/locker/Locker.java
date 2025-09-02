package org.szylica.model.locker;

import lombok.*;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Locker {
    @Getter
    private Long id;
    private Long parcelMachineId;
    private LockerSize size;
    private LockerStatus status;

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
