package org.szylica.files.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "users")
public class UsersData {
    private List<UserData> users;

    public UsersData(List<UserData> users) {
        this.users = users;
    }

    public UsersData() {
    }

    @XmlElement(name="user")
    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;

        UsersData usersData = (UsersData) o;
        return Objects.equals(users, usersData.users);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(users);
    }

    @Override
    public String toString() {
        return "UsersData{" +
                "users=" + users +
                '}';
    }
}

