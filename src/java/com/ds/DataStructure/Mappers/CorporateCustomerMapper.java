package com.ds.DataStructure.Mappers;

import com.ds.Entities.CorporateCustomer;

import java.util.Arrays;
import java.util.List;

public class CorporateCustomerMapper implements BaseMapper<CorporateCustomer> {
    @Override
    public CorporateCustomer toEntity(String str) {
        List<String> args = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new CorporateCustomer(Integer.parseInt(args.get(0)), null, null, null, false, args.get(1));
    }

    @Override
    public String toString(CorporateCustomer entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getTaxNumber());
        return sb.toString();
    }
}
