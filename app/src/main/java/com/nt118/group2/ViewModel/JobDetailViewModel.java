package com.nt118.group2.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nt118.group2.Database.Repository.JobDetailRepository;
import com.nt118.group2.Model.Job;
import com.nt118.group2.Model.JobDetail;

import java.util.List;

public class JobDetailViewModel extends ViewModel {
    private JobDetailRepository jobDetailRepository;
    private LiveData<List<JobDetail>> jobDetails;
    private List<JobDetail> listJobDetail;

    public JobDetailViewModel() {

    }

    public void setContext(Context context, int jobId) {
        jobDetailRepository = new JobDetailRepository(context, jobId);
            listJobDetail = jobDetailRepository.getListJobDetail();
            jobDetails = jobDetailRepository.getJobDetails();
    }
/*    public void setContext(Context context) {
        jobDetailRepository = new JobDetailRepository(context);
        listJobDetail = jobDetailRepository.getListJobDetail();
        jobDetails = jobDetailRepository.getJobDetails();
    }*/

    public LiveData<List<JobDetail>> getJobDetails() {
        return jobDetails;
    }

    public void insert(JobDetail... jobDetails) {
        jobDetailRepository.insert(jobDetails);
    }

    public void update(JobDetail... jobDetails) {
        jobDetailRepository.update(jobDetails);

    }

    public void delete(JobDetail... jobDetails) {
        jobDetailRepository.delete(jobDetails);
    }

    public List<JobDetail> checkList() {
        if (jobDetails.getValue() != null) {
            return jobDetails.getValue();
        } else{
            return listJobDetail;
        }
    }

    public double updateProgress() {
        double before = 0;
        for (JobDetail jobDetail : checkList()) {
            if (jobDetail.getStatus()) {
                ++before;
            }
        }
        double after = checkList().size();
        if(after == 0)
            return 0;
        return before / after;
    }
    public int count(){
        return checkList().size();
    }

    public void syncJob(Job job) {
        double curr = updateProgress();
        if (job.getProgress() == 1 && curr != 1.0) {
            for (JobDetail jobDetail : checkList()
            ) {
                jobDetail.setStatus(true);
            }
        } else if (job.getProgress() == 0 && job.getProgress() != curr) {
            for (JobDetail jobDetail : checkList()
            ) {
                jobDetail.setStatus(false);
            }
        }
        update(checkList().toArray(new JobDetail[0]));
    }

}
