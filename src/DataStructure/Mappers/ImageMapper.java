package DataStructure.Mappers;

import Entities.Image;

import java.util.Arrays;
import java.util.List;

public class ImageMapper implements BaseMapper<Image> {
    @Override
    public Image toEntity(String str) {
        List<String> attrs = Arrays.stream(str.split(",")).map(String::trim).toList();

        return new Image(Integer.parseInt(attrs.get(0)), Integer.parseInt(attrs.get(1)), attrs.get(2));
    }

    @Override
    public String toString(Image entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(", ");
        sb.append(entity.getRealEstateId()).append(", ");
        sb.append(entity.getPath()).append(", ");

        return  sb.toString();
    }
}
