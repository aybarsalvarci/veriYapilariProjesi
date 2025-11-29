package DataStructure.Mappers;

import Entities.SystemUser;
import Enums.Role;

import java.util.Arrays;
import java.util.List;

public class SystemUserMapper implements BaseMapper<SystemUser> {


    @Override
    public SystemUser toEntity(String str) {
        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();
        return new SystemUser(Integer.parseInt(attrs.get(0)), attrs.get(1), Role.valueOf(attrs.get(2)));
    }

    @Override
    public String toString(SystemUser entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getPasswordHash()).append(",");
        sb.append(entity.getRole());

        return sb.toString();
    }
}
