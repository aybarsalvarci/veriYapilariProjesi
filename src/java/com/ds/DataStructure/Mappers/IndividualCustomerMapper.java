package com.ds.DataStructure.Mappers;

import com.ds.Entities.IndividualCustomer;

import java.util.Arrays;
import java.util.List;

public class IndividualCustomerMapper implements BaseMapper<IndividualCustomer> {
    @Override
    public IndividualCustomer toEntity(String str) {
        List<String> args = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new IndividualCustomer(Integer.parseInt(args.get(0)), null, null, null, false, args.get(1));
    }

    @Override
    public String toString(IndividualCustomer entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getTc());
        return sb.toString();
    }
}
