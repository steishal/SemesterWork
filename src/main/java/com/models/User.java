package com.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.Objects;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private ArrayList<Integer> followers;
    private ArrayList<Integer> subscriptions;

    @NonNull
    private String email = "";
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



