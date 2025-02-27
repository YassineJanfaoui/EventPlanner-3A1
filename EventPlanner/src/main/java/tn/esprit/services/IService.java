package tn.esprit.services;

import java.util.List;

public interface IService<T> {
    void addp(T t);

    List<T> returnList() ;

    void update(T t);

    void delete(T t);

    void add(T t);

}
