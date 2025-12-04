package com.ds.DataStructure.Mappers;

import com.ds.Entities.Company;

import java.util.Arrays;
import java.util.List;

public class CompanyMapper implements BaseMapper<Company> {
    @Override
    public Company toEntity(String str) {

        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();
        return new Company(Integer.parseInt(attrs.get(0)), attrs.get(1), attrs.get(2), attrs.get(3));

    }

    @Override
    public String toString(Company entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getTitle()).append(",");
        sb.append(entity.getLocation()).append(",");
        sb.append(entity.getLogo());

        return sb.toString();
    }
}
