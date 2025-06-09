package com.nt118.group2.View.Activity;



import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.ViewPager2;

import com.nt118.group2.Library.CalendarExtension;
import com.nt118.group2.Library.Extension;
import com.nt118.group2.Library.GeneralData;
import com.nt118.group2.Model.Job;
import com.nt118.group2.R;
import com.nt118.group2.Service.NotificationService;
import com.nt118.group2.View.Adapter.ViewPagerAdapter;
import com.nt118.group2.ViewModel.JobViewModel;
import com.nt118.group2.ViewModel.NotificationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomMenu;
    private JobViewModel jobViewModel;
    private ViewPager2 viewPager;
    private int dlg_mode = 0;
    ViewPagerAdapter viewPagerAdapter;
    private SearchView searchView;
    private MenuItem importFromIcs;
    private MenuItem notificationManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobViewModel = new JobViewModel();
        jobViewModel.setData(getApplicationContext());
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        importFromIcs = menu.findItem(R.id.import_from_ics);
        importFromIcs.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Create an intent to open the file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Only show files that can be opened (optional filter for .ics files)
                String[] mimeTypes = {"text/calendar", "application/octet-stream"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select ICS File"), REQUEST_CODE_IMPORT_ICS);
                } catch (android.content.ActivityNotFoundException ex) {
                    assert(false);
                    // Potentially alert the user that no file manager is available
//                    Toast.makeText(getContext(), "Please install a file manager",
//                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

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
    // Then in your activity/fragment, handle the result:
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if (requestCode == REQUEST_CODE_IMPORT_ICS && resultCode == RESULT_OK) {
        if (requestCode == REQUEST_CODE_IMPORT_ICS) {
//            if (data != null) {
            if (true) {
                try {
                    Uri uri = data.getData();
                    // Read the file content
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    System.out.println(reader.toString());
//                    // Parse the ICS file
                     List<Job> jobs = parseIcsFile(reader);
                    // List<Job> jobs = parseIcsFile();
                    System.out.println(jobs);
                    for (Job job: jobs) {
                        jobViewModel.insert(job);
                    }
                    Toast.makeText(this, "Import ICS successfully", Toast.LENGTH_SHORT).show();
//
//                    // Process the parsed events (save to database, etc.)
//                    saveEvents(events);
//
//                    Toast.makeText(this, "Imported " + events.size() + " events",
//                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error importing file", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private static final int REQUEST_CODE_IMPORT_ICS = 1001;
    @RequiresApi(api = Build.VERSION_CODES.S)
    private List<Job> parseIcsFile(BufferedReader reader) throws IOException {
        List<Job> jobs = new ArrayList<>();
        String line;

        // Temporary storage for event properties
        Integer categoryId = 1; // Default category
        String name = null;
        Date startDate = null;
        Date endDate = null;
        String description = "";

        while ((line = reader.readLine()) != null) {
            // Handle line folding (continuation lines)
            if ((line.startsWith(" ") || line.startsWith("\t")) && name != null) {
                // Append to the last property (description)
                description += line.trim();
                continue;
            }

            if (line.startsWith("BEGIN:VEVENT")) {
                // Reset for new event
                categoryId = 1;
                name = null;
                startDate = null;
                endDate = null;
                description = "";
            } else if (line.startsWith("END:VEVENT")) {
                // Create job if we have all required fields
                if (name != null && startDate != null) {
                    // Set default end date if not specified (1 hour after start)
                    if (endDate == null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDate);
                        cal.add(Calendar.HOUR, 1);
                        endDate = cal.getTime();
                    }

                    jobs.add(new Job(
                            categoryId,
                            name,
                            startDate,
                            endDate,
                            description
                    ));
                }
            } else {
                // Parse event properties
                if (line.startsWith("DTSTART:")) {
                    startDate = parseIcsDateTime(line.substring(8));
                } else if (line.startsWith("DTEND:")) {
                    endDate = parseIcsDateTime(line.substring(6));
                } else if (line.startsWith("SUMMARY:")) {
                    name = line.substring(8);
                } else if (line.startsWith("DESCRIPTION:")) {
                    description = line.substring(12);
                } else if (line.startsWith("CATEGORIES:")) {
                    categoryId = mapIcsCategoryToId(line.substring(11));
                }
            }
        }
        return jobs;
    }

    private int mapIcsCategoryToId(String icsCategories) {
        // Simple category mapping - adjust according to your needs
        if (icsCategories == null || icsCategories.isEmpty()) {
            return 1;
        }

        if (icsCategories.contains("WORK") || icsCategories.contains("JOB")) {
            return 1;
        } else if (icsCategories.contains("STUDY") || icsCategories.contains("SCHOOL")) {
            return 2;
        } else if (icsCategories.contains("PERSONAL") || icsCategories.contains("HOME")) {
            return 3;
        }
        return 1; // Default category
    }

    private Date parseIcsDateTime(String icsDateTime) {
        try {
            // Handle both date-time and date-only formats
            SimpleDateFormat format;
            if (icsDateTime.length() == 8) { // Date only (YYYYMMDD)
                format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            } else {
                // Handle timezone (Z suffix)
                if (icsDateTime.endsWith("Z")) {
                    format = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault());
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                } else {
                    format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
                }
            }
            return format.parse(icsDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(); // Fallback to current time
        }
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
                if (id == R.id.menu_job) {
                    viewPager.setCurrentItem(0);
                } else if (id == R.id.menu_month) {
                    viewPager.setCurrentItem(1);
                } else if (id == R.id.menu_account) {
                    viewPager.setCurrentItem(3);
                } else if (id == R.id.menu_add_new_job) {
                    Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                    startActivity(intent);
                    SelectBottomMenuPosition(0);
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
