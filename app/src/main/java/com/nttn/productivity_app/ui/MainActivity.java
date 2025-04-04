package com.nttn.productivity_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.nttn.productivity_app.R;
import com.nttn.productivity_app.model.Todo;
import com.nttn.productivity_app.ui.habit.HabitBottomSheetFragment;
import com.nttn.productivity_app.ui.habit.HabitViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import static com.nttn.productivity_app.util.Utils.setThemeNightMode;
import com.nttn.productivity_app.util.DialogUtils;

import java.time.LocalDateTime;

// Layout Inflation: Convert xml object to view object, by calling inflater.inflate(xml object, );
// In Recycler view:
//      inflating used to create item views that will be displayed in the list of grid.
// Recycler view pattern:
// 1. Recycler view: (the main view)
// 2. Adapter: (Bind data to views)
// 3. View holder: (Performance optimization trick) Holds references to the views for each item, improving performance by caching views.
// 4. Layout manager: (Manages the layout to display list items)

// TODO: (nttn) add the engine into the app [X]
// TODO: (nttn) show an alert [X]
// TODO: (nttn) show a custom alert [X]
// TODO: (nttn) insert custom habit to the recycler view [X]
// TODO: (nttn) create 1 more side bar buttons: all habits, let the fragment home as today's habits [X]
    // have different stub data for each screen to differentiate
// TODO: (nttn) custom the task creation ui                 [X]
// TODO: (nttn) add time picker right after the date picker [X]
// TODO: (nttn) locate the logic of the app                 [X]
    // NOTE: (nttn) What's logic does the app currently have ??
    // 1. habit crud (add habit, reset progress, Edit habit, delete habit):
          // -> Today Habit Fragment, All Habit Fragment
    // 2. progress timer (increment every 1s): HabitViewHolder
    // -> TODO: (nttn) Gather all distributed logic into a HabitService
// TODO: (nttn) Adjust the Habit to fit our use case     [X]
// TODO: (nttn) Make the habit display a time left format, make it count down [] @Current
// TODO: (nttn) custom the logo (nt118 - group 2) xml    []
// TODO: (nttn) insert custom todo (not habit) to the recycler view  []
// TODO: (nttn) hooks the engine to the home page        []

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ACTIVITY_MAIN";

    private AppBarConfiguration mAppBarConfiguration;
    FloatingActionButton fab;
    Snackbar snackbar;
    private HabitBottomSheetFragment habitBottomSheetFragment;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest, signUpRequest;
    ActivityResultLauncher<IntentSenderRequest> signInResultHandler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        habitBottomSheetFragment = new HabitBottomSheetFragment();
        CoordinatorLayout habitBottomSheetConstraintLayout = findViewById(R.id.habitBottomSheetConstraintLayout);
        BottomSheetBehavior<CoordinatorLayout> habitBottomSheetBehavior =
                BottomSheetBehavior.from(habitBottomSheetConstraintLayout);
        habitBottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        new ViewModelProvider(this)
                .get(HabitViewModel.class)
                .setHabitBottomSheetFragment(habitBottomSheetFragment);

        fab = findViewById(R.id.fab_add_habit);
        fab.setOnClickListener(view -> habitBottomSheetFragment.show(getSupportFragmentManager(), habitBottomSheetFragment.getTag()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_today_habits, R.id.nav_stats)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // preferences
        // initialize with default values.
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        // reading the changed settings value from shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usersName = sharedPreferences.getString(SettingsActivity.KEY_PREF_USERS_NAME, null);
        TextView textViewSmallNavHeader = navigationView.getHeaderView(0).findViewById(R.id.text_view_small_nav_header);
        if (usersName != null && usersName.trim().length() > 0) {
            textViewSmallNavHeader.setText(String.format("Signed in as %s", usersName));
            textViewSmallNavHeader.setVisibility(View.VISIBLE);
        }else {
            textViewSmallNavHeader.setVisibility(View.GONE);
        }

        final String[] prefThemeValues = getResources().getStringArray(R.array.theme_values);
        String prefTheme = sharedPreferences.getString(getString(R.string.pref_theme), prefThemeValues[0]);
        setThemeNightMode(prefThemeValues, prefTheme);

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.your_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(false)
                        .setServerClientId(getString(R.string.your_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
        signInResultHandler = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                    if(result.getResultCode() == RESULT_OK){
                        Log.d(TAG, "RESULT_OK");
                    }else if(result.getResultCode() == RESULT_CANCELED){
                        Log.d(TAG, "RESULT_CANCELED");
                    }else if(result.getResultCode() == RESULT_FIRST_USER) {
                        Log.d(TAG, "RESULT_FIRST_USER");
                    }
                }
        );
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            // intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void dismissSnackbar() {
        snackbar.dismiss();
    }
}