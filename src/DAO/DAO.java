package DAO;

import java.util.Collection;
import java.util.List;

public interface DAO <T>{

    void syncListToDB();

    boolean save(T t);

    boolean delete(T t);

    T findById(long id);

    List<T> getAll();
}
