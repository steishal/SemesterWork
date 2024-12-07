package com.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;

    @NonNull
    private String email = "";
    @NonNull
    private String phoneNumber = "";
    private String vkLink = "";
    private String tgLink = "";

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



