package com.ds.DataStructure.Mappers;

import com.ds.Entities.Image;

import java.util.Arrays;
import java.util.List;

public class ImageMapper implements BaseMapper<Image> {
    @Override
    public Image toEntity(String str) {

        List<String> imageAttrs = Arrays.stream(str.split("\\|"))
                .map(String::trim)
                .toList();

        return new Image(Integer.parseInt(imageAttrs.get(0)), Integer.parseInt(imageAttrs.get(1)), imageAttrs.get(2));
    }

    @Override
    public String toString(Image entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append("| ");
        sb.append(entity.getRealEstateId()).append("| ");
        sb.append(entity.getPath());

        return sb.toString();
    }
}
