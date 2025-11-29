package DataStructure.Mappers;

import Entities.BuildingProperty;
import Enums.ContractType;

import java.util.Arrays;
import java.util.List;

public class BuildingPropertyMapper implements BaseMapper<BuildingProperty> {
    @Override
    public BuildingProperty  toEntity(String str) {
        List<String> args = Arrays.stream(str.split(",")).map(String::trim).toList();
        
        return new BuildingProperty(Integer.parseInt(args.get(0)), args.get(1), ContractType.valueOf(args.get(2)));
    }

    @Override
    public String toString(BuildingProperty entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getId()).append(", ");
        sb.append(entity.getIndependenceType()).append(", ");
        sb.append(entity.getContractType());

        return sb.toString();
    }
}
