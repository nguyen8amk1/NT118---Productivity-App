package com.nt118.group2.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nt118.group2.Database.Repository.JobRepository;
import com.nt118.group2.Library.CalendarExtension;
import com.nt118.group2.Library.Extension;
import com.nt118.group2.Model.Job;
import com.nt118.group2.Service.NotificationService;

import java.util.Date;
import java.util.List;

public class JobViewModel extends ViewModel {
    private JobRepository jobRepository;
    Context context;

    public JobViewModel() {
    }

    public void setData(Context context) {
        this.context = context;
        jobRepository = new JobRepository(context);
    }

    public Job getById(int id) {
        return jobRepository.getById(id);
    }

    public void insert(Job... jobs) {
        jobRepository.insert(jobs);
        startService();
    }

    public void update(Job... jobs) {
        Log.e("TAG", "update: " );
        jobRepository.update(jobs);
        startService();
    }

    public void delete(Job... jobs) {
        jobRepository.delete(jobs);
        startService();
    }
    public LiveData<List<Job>> getJobs(Date endDate) {
        return jobRepository.getJobs(endDate);
    }

    public Job getJobById(int Id){
        return jobRepository.getById(Id);
    }

    public LiveData<List<Job>> getJobs() {
        return jobRepository.getJobs();
    }

    public LiveData<List<Job>> getJobs(Date startDate, Date endDate) {
        return jobRepository.getJobs(startDate, endDate);
    }

    public LiveData<List<Job>> getByCategoryId(int categoryId) {
        return jobRepository.getByCategoryId(categoryId);
    }

    public LiveData<List<Job>> getJobsInDay(Date date){
        return jobRepository.getJobAboutTime(date,date);
    }

    public LiveData<List<Job>> getJobsMonth(int month,int year){
        return jobRepository.getJobAboutTime(CalendarExtension.getStartTimeOfMonth(month,year),CalendarExtension.getEndTimeOfMonth(month,year));
    }

    public List<Job> getListJobMonth(int month,int year){
        return jobRepository.getListAboutTime(CalendarExtension.getStartTimeOfMonth(month,year),CalendarExtension.getEndTimeOfMonth(month,year));
    }

    public LiveData<List<Job>> getJobsWeek(Date date,int position){

        return jobRepository.getJobAboutTime(CalendarExtension.getStartTimeOfWeek(date,position),CalendarExtension.getEndTimeOfWeek(date,position));
    }
    public LiveData<List<Job>> getJobsStatus(int status,Date start,Date end){
        return jobRepository.getJobByStatus(status,start,end);
    }

    public List<Job> getJobsByCategory(int id){
        return jobRepository.getJobByCategory(id);
    }

    public int getCountJobOneDate(Date date){
        Date start = CalendarExtension.getStartTimeOfDate(date);
        Date end = CalendarExtension.getEndTimeOfDate(date);
        return jobRepository.getListEndTimeByTime(CalendarExtension.getStartTimeOfDate(date),CalendarExtension.getEndTimeOfDate(date));
    }

    public int countStatusMonth(int status,int month,int year){
        return jobRepository.getSumJobByStatusMonth(status,CalendarExtension.getStartTimeOfMonth(month,year),CalendarExtension.getEndTimeOfMonth(month,year));
    }

    public int getTotalStatus(int status){
        return jobRepository.getTotalStatus(status);
    }

    public void getJobBy(Job... jobs) {
        jobRepository.update(jobs);
    }

    public List<Job> getListByStatus(int status){
        return jobRepository.getJobByStatus(status);
    }

    public void checkOrUncheck(Job job,boolean check){
        if(check){
            job.setProgress(1); // 1 is 100%
            job.setStatus(Extension.CheckStatus(job));
        }else{
            job.setProgress(0);
            job.setStatus(Extension.CheckStatus(job));
        }
        update(job);
    }

    public void startService(){
        Intent intent = new Intent(context, NotificationService.class);
        context.startService(intent);
    }


}
