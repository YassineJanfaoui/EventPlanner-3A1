package tn.esprit.services;

import tn.esprit.entities.Workshop;

import java.sql.SQLException;
import java.util.List;

public interface IService<X> {

    void add(X w) throws SQLException;
    void addP(X w) throws SQLException;
    List<X> returnList() throws SQLException;
    void delete(X w);
    void update(X w);
}


