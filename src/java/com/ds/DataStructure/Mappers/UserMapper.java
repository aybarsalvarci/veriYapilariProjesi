package com.ds.DataStructure.Mappers;

import com.ds.Entities.User;

import java.util.Arrays;
import java.util.List;

public class UserMapper implements BaseMapper<User>{
    @Override
    public User toEntity(String str) {

        List<String> userAttrs = Arrays.stream(str.split("\\|"))
                .map(String::trim)
                .toList();

        return new User(Integer.parseInt(userAttrs.get(0)), userAttrs.get(1), userAttrs.get(2), userAttrs.get(3));
    }

    @Override
    public String toString(User entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append("| ");
        sb.append(entity.getFirstName()).append("| ");
        sb.append(entity.getLastName()).append("| ");
        sb.append(entity.getEmail());

        return sb.toString();
    }
}
