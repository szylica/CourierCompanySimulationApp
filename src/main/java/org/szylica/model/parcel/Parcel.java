package org.szylica.model.parcel;

import lombok.*;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.parcel.enums.ParcelStatus;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Parcel {
    @Getter
    private Long id;
    private Long lockerId;
    private Integer width;
    private Integer height;
    private Integer depth;
    private ParcelStatus status;


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

    public LockerSize calculateLockerSize() {
        return Locker.calculateLockerSize(this.width, this.height, this.depth);
    }
}
