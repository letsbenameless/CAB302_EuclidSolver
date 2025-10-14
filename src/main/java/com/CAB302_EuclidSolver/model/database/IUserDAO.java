package com.CAB302_EuclidSolver.model.database;

import com.CAB302_EuclidSolver.model.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    void createUserTable();

    void insert(User user) throws DataAccessException;

    List<User> getAll() throws DataAccessException;

    Optional<User> getUserByUsername(String username) throws DataAccessException;

    Optional<User> getUserByEmail(String email) throws DataAccessException;

}
