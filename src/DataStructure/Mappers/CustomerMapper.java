package DataStructure.Mappers;

import Entities.Customer;

import java.util.Arrays;
import java.util.List;

public class CustomerMapper implements BaseMapper<Customer>{
    @Override
//    int id, String firstName, String lastName, String email, boolean isApproved
    public Customer toEntity(String str) {
        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new Customer(Integer.parseInt(attrs.get(0)), attrs.get(1), attrs.get(2), attrs.get(3),
                Boolean.parseBoolean(attrs.get(4)));
    }

    @Override
    public String toString(Customer entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getIsApproved()).append(",");

        return sb.toString();
    }
}
