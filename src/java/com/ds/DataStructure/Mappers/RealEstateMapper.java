package com.ds.DataStructure.Mappers;

import com.ds.Entities.RealEstate;

import java.util.Arrays;
import java.util.List;

public class RealEstateMapper implements BaseMapper<RealEstate> {

    @Override
    public RealEstate toEntity(String str) {
        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();

        RealEstate e = new RealEstate();
        e.setId(Integer.parseInt(attrs.get(0)));
        e.setCustomerId(Integer.parseInt(attrs.get(1)));
        e.setTitle(attrs.get(2));
        e.setDescription(attrs.get(3));
        e.setSize(Double.parseDouble(attrs.get(4)));
        e.setLocation(attrs.get(5));
        e.setPrice(Double.parseDouble(attrs.get(6)));

        return e;
    }

    @Override
    public String toString(RealEstate entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(", ");
        sb.append(entity.getCustomerId()).append(", ");
        sb.append(entity.getTitle()).append(", ");
        sb.append(entity.getDescription()).append(", ");
        sb.append(entity.getSize()).append(", ");
        sb.append(entity.getLocation()).append(", ");
        sb.append(entity.getPrice());

        return sb.toString();
    }
}
