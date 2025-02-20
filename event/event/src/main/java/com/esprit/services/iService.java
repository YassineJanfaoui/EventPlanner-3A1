package com.esprit.services;

import java.util.ArrayList;

public interface iService<T> {
    /**
     * Adds a new object of type T to the database.
     *
     * @param t The object to add.
     */
    void ajouter(T t);

    /**
     * Modifies an existing object of type T in the database.
     *
     * @param t The object with updated information.
     */
    void modifier(T t);

    /**
     * Deletes an object of type T from the database.
     *
     * @param t The object to delete.
     */
    void supprimer(T t);

    /**
     * Retrieves a list of all objects of type T from the database.
     *
     * @return A list of objects of type T.
     */
    ArrayList<T> rechercher();
}
