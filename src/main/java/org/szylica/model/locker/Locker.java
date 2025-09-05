package org.szylica.model.locker;

import lombok.*;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.szylica.model.parcel.Parcel;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    public static LockerSize calculateLockerSize(Integer width, Integer height, Integer depth) {
        if(width <= 10 && height <= 10 && depth <= 10){
            return LockerSize.SMALL;
        }
        else if(width <= 20 && height <= 20 && depth <= 20){
            return LockerSize.MEDIUM;
        }
        else if(width <= 30 && height <= 30 && depth <= 30){
            return LockerSize.LARGE;
        }
        throw new IllegalStateException("Parcel is too big for Parcel Machine");
    }


    public Locker withStatus(LockerStatus status){
        return Locker.builder()
                .id(this.id)
                .parcelMachineId(this.parcelMachineId)
                .size(this.size)
                .status(status)
                .build();
    }


}
