package com.nttn.productivity_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// TODO: (nttn) show momentum home page [X]
// TODO: (nttn) add good habits to the game [] @Current
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.goodhabits);

        // BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_following, R.id.navigation_add_habit, R.id.navigation_sharing)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupWithNavController(navView, navController);
    }
}