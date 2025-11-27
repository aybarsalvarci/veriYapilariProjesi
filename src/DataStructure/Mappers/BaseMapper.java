package DataStructure.Mappers;

import Entities.BaseEntity;

public interface BaseMapper<T extends BaseEntity> {

    T toEntity(String str);
    String toString(T entity);
}
