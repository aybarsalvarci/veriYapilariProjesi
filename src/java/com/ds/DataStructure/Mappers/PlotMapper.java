package com.ds.DataStructure.Mappers;

import com.ds.Entities.Plot;

import java.util.Arrays;
import java.util.List;

public class PlotMapper implements BaseMapper<Plot> {
    @Override
    public Plot toEntity(String str) {
        List<String> args = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new Plot(Integer.parseInt(args.get(0)), args.get(1));
    }

    @Override
    public String toString(Plot entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(",");
        sb.append(entity.getZoningStatus());

        return sb.toString();
    }
}
