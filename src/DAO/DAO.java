package DAO;

import enti.User;

import java.util.List;

public interface DAO <T>{

    void syncListToDB();

    T save(T t);

    boolean delete(T t);

    T findById(long id);

   List<T> getAll();
}
