package com.addresslist.neary.bean;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE name LIKE '%' || :name || '%'")
    List<User> findByName(String name);

    @Query("SELECT * FROM user WHERE uid  = :uid")
    User findByID(long uid);

    @Update
    void update(User user);

    @Insert
    long insertUser(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

}
