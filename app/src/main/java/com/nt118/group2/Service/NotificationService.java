package com.nt118.group2.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.nt118.group2.Library.CalendarExtension;
import com.nt118.group2.Library.Extension;
import com.nt118.group2.Library.GeneralData;
import com.nt118.group2.Library.Key;
import com.nt118.group2.Model.Job;
import com.nt118.group2.Model.NotificationModel;
import com.nt118.group2.R;
import com.nt118.group2.View.Activity.MainActivity;
import com.nt118.group2.ViewModel.JobViewModel;
import com.nt118.group2.ViewModel.NotificationViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class NotificationService extends Service {
    private NotificationViewModel notificationViewModel;
    private JobViewModel jobViewModel;
    private List<Job> jobsComing;
    private List<Job> jobsOnGoing;
    private Date date;
    private boolean isNew = false;
    RemoteViews remoteViews;
    private NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    CountDownTimer timer;
    int coming = 0;
    int going = 0;
    int over = 0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadData();
        return START_REDELIVER_INTENT;
    }

    private void loadData() {
        initViewModel();
        getComingAndOnGoing();
        startTimer();
        sendNotification();
    }

    private void getComingAndOnGoing() {
        jobsComing = null;
        jobsOnGoing = null;
        jobsComing = setJobAndUpDate(GeneralData.STATUS_COMING);
        jobsOnGoing = setJobAndUpDate(GeneralData.STATUS_ON_GOING);
        date = null;
        if (jobsOnGoing != null && jobsOnGoing.size() != 0) {
            Collections.sort(jobsOnGoing, new Comparator<Job>() {
                @Override
                public int compare(Job o1, Job o2) {
                    return (o1.getEndDate()).compareTo(o2.getEndDate());
                }
            });
            date = jobsOnGoing.get(0).getEndDate();
        }
        if (jobsComing != null && jobsComing.size() != 0) {
            Collections.sort(jobsComing, new Comparator<Job>() {
                @Override
                public int compare(Job o1, Job o2) {
                    return (o1.getStartDate()).compareTo(o2.getStartDate());
                }
            });
            if (date == null) {
                date = jobsComing.get(0).getStartDate();
            } else {
                if (CalendarExtension.timeRemaining(date, jobsComing.get(0).getStartDate()) < 0)
                    date = jobsComing.get(0).getStartDate();
            }
        }
        coming = jobViewModel.getTotalStatus(GeneralData.STATUS_COMING);
        going = jobViewModel.getTotalStatus(GeneralData.STATUS_ON_GOING);
        over = notificationViewModel.getTotalNotificationStatus(GeneralData.STATUS_OVER, GeneralData.STATUS_NOTIFICATION_ACTIVE);
        if (coming == 0 && going == 0 && over == 0)
            stopSelf();
    }

    private List<Job> setJobAndUpDate(int status) {
        List<Job> jobList = jobViewModel.getListByStatus(status);
        switch (status) {
            case GeneralData.STATUS_COMING:
                jobList = updateJob(jobList,GeneralData.STATUS_ON_GOING);
                jobList = updateJob(jobList,GeneralData.STATUS_OVER);
                break;
            case GeneralData.STATUS_ON_GOING:
                jobList = updateJob(jobList,GeneralData.STATUS_COMING);
                jobList = updateJob(jobList,GeneralData.STATUS_OVER);
                break;
        }
        return jobList;
    }

    private List<Job> updateJob(List<Job> jobList,int statusTarget) {
        List<Job> jobs =  Extension.getJobsChange(jobList, statusTarget);
        if (jobs.size() != 0) {
            AddNotificationModel(jobs);
            jobViewModel.update(jobs.toArray(new Job[0]));
        }
        jobList.removeAll(jobs);
        return jobList;
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_jobs);
        remoteViews.setTextViewText(R.id.tv_app_name, getApplicationContext().getString(R.string.app_name));
        remoteViews.setTextViewText(R.id.tv_content, contentText());
        mBuilder = new NotificationCompat.Builder(this, Key.CHANNEL_NOTIFICATION)
                .setSmallIcon(R.drawable.logo_final)
                .setSilent(false)
                .setAutoCancel(true)
                .setCustomContentView(remoteViews);
        startForeground(Key.CHANNEL_NOTIFICATION_JOB_ID, mBuilder.build());
    }

    private void AddNotificationModel(List<Job> jobs) {
        for (Job job : jobs) {
            NotificationModel notificationModel = new NotificationModel(job.getId(), job.getStatus(), CalendarExtension.currDate(), GeneralData.STATUS_NOTIFICATION_ACTIVE);
            notificationViewModel.insert(notificationModel);
            sendNotificationJob(job.getId());
        }
    }

    private void sendNotificationJob(int jobID) {
        Intent intent = new Intent(this, NotificationJobService.class);
        intent.putExtra(Key.SEND_NOTIFICATION_JOB_ID, jobID);
        startService(intent);
    }

    private Spannable contentText() {
        ArrayList<Integer> number = new ArrayList<Integer>();
        String strStart = getString(R.string.you_have);
        String strComing = Extension.getStringTotalJob(coming, GeneralData.STATUS_COMING, getApplicationContext());
        String strOnGoing = Extension.getStringTotalJob(going, GeneralData.STATUS_ON_GOING, getApplicationContext());
        String strOver = Extension.getStringTotalJob(over, GeneralData.STATUS_OVER, getApplicationContext());
        SpannableString spannable;
        if (going != 0) {
            number.add(GeneralData.STATUS_ON_GOING);
            number.add(strStart.length() + 1);
            strStart += strOnGoing + ", ";
            number.add(strStart.length() - 2);
        }
        if (coming != 0) {
            number.add(GeneralData.STATUS_COMING);
            number.add(strStart.length() + 1);
            strStart += strComing + ",";
            number.add(strStart.length() - 1);
        }
        if (over != 0) {
            number.add(GeneralData.STATUS_OVER);
            number.add(strStart.length() + 1);
            strStart += strOver + ",";
            number.add(strStart.length() - 1);
        }
        strStart = strStart.substring(0, strStart.length() - 1);
        spannable = new SpannableString(strStart);
        for (int i = 0; i < number.size(); i += 3) {
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ResourcesCompat.getColor(getResources(), GeneralData.getColorStatus(number.get(i)), null));
            spannable.setSpan(foregroundColorSpan, number.get(i + 1), number.get(i + 2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    private void initViewModel() {
        jobViewModel = new JobViewModel();
        jobViewModel.setData(this);
        notificationViewModel = new NotificationViewModel();
        notificationViewModel.setData(this);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (date != null) {
            Date now = Calendar.getInstance().getTime();
            long timeCount = (CalendarExtension.timeRemaining(now, date)) + 1000;
            timer = new CountDownTimer(timeCount, CalendarExtension.ONE_MINUTE) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    loadData();
                }
            };
            if (timeCount > 0)
                timer.start();
            else {
                loadData();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
