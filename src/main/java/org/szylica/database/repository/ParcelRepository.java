package org.szylica.repository;

import org.szylica.model.parcel.Parcel;
import org.szylica.repository.db.generic.CrudRepository;

public interface ParcelRepository extends CrudRepository<Parcel, Long> {
}
