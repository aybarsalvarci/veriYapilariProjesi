package com.ds.DataStructure.Mappers;

import com.ds.Entities.BaseEntity;

public interface BaseMapper<T extends BaseEntity> {

    T toEntity(String str);
    String toString(T entity);
}
