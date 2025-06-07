package com.nt118.group2.ViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.nt118.group2.Database.DataLocal.DataLocalManager;
import com.nt118.group2.Database.Repository.UserRepository;
import com.nt118.group2.Model.User;

public class UserViewModel extends ViewModel {
    private UserRepository UserRepository;
    private User user;

    public UserViewModel() {

    }

    public void setContext(Context context) {
        UserRepository = new UserRepository(context);
        user = UserRepository.getUser(DataLocalManager.getEmail());
    }

    public User getUser() {
        return user;
    }

    public void insert(User... users) {
        UserRepository.insert(users);
    }

    public void update(User... users) {
        UserRepository.update(users);
    }

    public void delete(User... users) {
        UserRepository.delete(users);
    }
}
