package com.nt118.group2.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nt118.group2.Model.User;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM User WHERE Email = :email")
    User getUser(String email);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

}
