package DataStructure.Mappers;

import Entities.Customer;

import java.util.Arrays;
import java.util.List;

public class CustomerMapper implements BaseMapper<Customer>{
    @Override
    public Customer toEntity(String str) {
        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new Customer(Integer.parseInt(attrs.get(0)),null, null, null,
                Boolean.parseBoolean(attrs.get(1)));
    }

    @Override
    public String toString(Customer entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getIsApproved());

        return sb.toString();
    }
}
