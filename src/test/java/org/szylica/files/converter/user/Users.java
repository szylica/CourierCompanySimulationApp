package org.szylica.files.converter.user;

import org.szylica.files.model.UserData;

public interface Users {
    UserData ADAM_USER = new UserData(
            1L,
            "Adam",
            "Kowalski",
            "adam.nowak@gmail.com",
            "+48111222333",
            100.12,
            101.12);

    UserData EWA_USER = new UserData(
            2L,
            "Ewa",
            "Kowal",
            "ewa.kowal@gmail.com",
            "+48555222333",
            102.12,
            103.12);
}
