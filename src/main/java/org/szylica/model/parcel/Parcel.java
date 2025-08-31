package org.szylica.data.model.parcel;

import lombok.*;
import org.szylica.data.model.locker.enums.LockerSize;
import org.szylica.data.model.parcel.enums.ParcelStatus;

@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Parcel {
    @Getter
    private final Long id;
    private Long lockerId;
    private final double width;
    private final double height;
    private final double depth;
    private final ParcelStatus status;

    public static LockerSize calculateLockerSize(Double width, Double height, Double depth) {
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

    public static LockerSize calculateLockerSize(Parcel parcel) {
        return calculateLockerSize(parcel.width, parcel.height, parcel.depth);
    }

    public Parcel withLocker(Long lockerId) {
        return Parcel.builder()
                .id(this.id)
                .lockerId(lockerId)
                .width(this.width)
                .height(this.height)
                .depth(this.depth)
                .status(this.status)
                .build();
    }
}
