package com.ds.Managers;

import com.ds.Entities.BaseEntity;

import java.util.List;

public interface IManager<T extends BaseEntity> {

    T get(int id);
    List<T> getAll();
    IManager create(T entity);
    IManager update(T entity);
    IManager delete(int id);
    void save();
}
