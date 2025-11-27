package DataStructure.Mappers;

import Entities.User;

import java.util.Arrays;
import java.util.List;

public class UserMapper implements BaseMapper<User>{
    @Override
    public User toEntity(String str) {
//        id, firstName, lastName, email

        List<String> userAttrs = Arrays.stream(str.split(","))
                .map(String::trim)
                .toList();

        return new User(Integer.parseInt(userAttrs.get(0)), userAttrs.get(1), userAttrs.get(2), userAttrs.get(3));
    }

    @Override
    public String toString(User entity) {
        return entity.getId() + ", " + entity.getFirstName() + ", " + entity.getLastName() + ", " + entity.getEmail();
    }
}
