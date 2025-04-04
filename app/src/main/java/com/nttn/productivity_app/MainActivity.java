package com.nttn.productivity_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nttn.productivity_app.habits.Habits;

// TODO: (nttn) show momentum home page [X]
// TODO: (nttn) add good habits to the game [X]
// TODO: (nttn) show some fake good habits [] @Current
// TODO: (nttn) switching betweens fragments using bottom navigation (all habits) [] @Current
// TODO: (nttn) integrate good habits completely
// TODO: (nttn) disable the all of its functional code
// Makes habits counts down


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.goodhabits);
        // setContentView(R.layout.habit_lists);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final Habits myFragment = new Habits();
        fragmentTransaction.add(R.id.habits_list, myFragment);
        fragmentTransaction.commit();

        // BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_following, R.id.navigation_add_habit, R.id.navigation_sharing)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupWithNavController(navView, navController);
    }
}