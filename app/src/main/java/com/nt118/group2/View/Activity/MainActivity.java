package com.nt118.group2.View.Activity;



import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.ViewPager2;

import com.nt118.group2.Library.DialogExtension;
import com.nt118.group2.Library.Extension;
import com.nt118.group2.Library.GeneralData;
import com.nt118.group2.R;
import com.nt118.group2.Service.NotificationService;
import com.nt118.group2.View.Adapter.ViewPagerAdapter;
import com.nt118.group2.ViewModel.JobViewModel;
import com.nt118.group2.ViewModel.NotificationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomMenu;
    private JobViewModel jobViewModel;
    private ViewPager2 viewPager;
    private int dlg_mode = 0;
    ViewPagerAdapter viewPagerAdapter;
    private SearchView searchView;
//    private Button import_from_ics;
    private MenuItem notificationManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
//        Button importFromIcs = findViewById(R.id.import_from_ics);
//        importFromIcs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println(view);
////                assert(false);
//            }
//        });
////

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (viewPager.getCurrentItem() == 0) {
                    Extension.filterSearch(viewPager, viewPagerAdapter, query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                if (viewPager.getCurrentItem() == 0) {
                    Extension.filterSearch(viewPager, viewPagerAdapter, search);
                }
                return false;
            }
        });
//        import_from_ics = menu.findItem(R.id.import_from_ics);
//        import_from_ics
//        import_from_ics.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                DialogExtension.onOpenMenuDialog(MainActivity.this);
//                return true;
//            }
//        });
        notificationManagement = menu.findItem(R.id.menu_item_notification);
        updateNotification();
        notificationManagement.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, NotificationManagementActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    private void init() {
        StartService();
        viewPager = findViewById(R.id.view_pager_main);
        bottomMenu = findViewById(R.id.bottom_Menu);
        viewPagerAdapter= new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);
        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_job:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_month:
                        viewPager.setCurrentItem(1);
                        break;
//                    case R.id.menu_setting:
//                        viewPager.setCurrentItem(2);
//                        break;
                    case R.id.menu_account:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.menu_add_new_job:
                        Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                        startActivity(intent);
                        SelectBottomMenuPosition(0);
                        break;
                }
                return true;
            }
        });

    }
    private void StartService(){
        Intent intent = new Intent(this, NotificationService.class);
        this.startService(intent);
    }
    public void SelectBottomMenuPosition(int position){
        bottomMenu.getMenu().getItem(position).setChecked(true);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void updateNotification(){
        NotificationViewModel notificationViewModel = new NotificationViewModel();
        notificationViewModel.setData(this);
        if(notificationViewModel.geNotificationTotal(GeneralData.STATUS_NOTIFICATION_ACTIVE)>0){
            notificationManagement.setIcon(R.drawable.ic_notifications);
        }else{
            notificationManagement.setIcon(R.drawable.ic_baseline_notifications_24);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.invalidateOptionsMenu();

    }
}
