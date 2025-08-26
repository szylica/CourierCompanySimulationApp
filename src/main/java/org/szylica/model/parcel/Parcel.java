package org.szylica.model.parcel;

import lombok.RequiredArgsConstructor;
import org.szylica.model.parcel.enums.ParcelStatus;

@RequiredArgsConstructor
public class Parcel {
    private final Long id;
    private final Long lockerId;
    private final double width;
    private final double height;
    private final double depth;
    private final ParcelStatus status;
}
