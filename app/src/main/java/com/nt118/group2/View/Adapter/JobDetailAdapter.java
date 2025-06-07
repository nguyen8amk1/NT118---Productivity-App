package com.nt118.group2.View.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt118.group2.Library.Action;
import com.nt118.group2.Library.DialogExtension;
import com.nt118.group2.Library.Extension;
import com.nt118.group2.Library.GeneralData;
import com.nt118.group2.Library.Key;
import com.nt118.group2.Model.Job;
import com.nt118.group2.Model.JobDetail;
import com.nt118.group2.R;
import com.nt118.group2.Service.CountUpService;
import com.nt118.group2.View.Activity.AddJobDetailActivity;
import com.nt118.group2.View.Activity.JobDetailActivity;
import com.nt118.group2.ViewModel.JobDetailViewModel;
import com.nt118.group2.ViewModel.JobViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobDetailAdapter extends RecyclerView.Adapter<JobDetailAdapter.JobDetailHolder> implements Filterable {
    private final Context mContext;
    private List<JobDetail> listShowJobDetail;
    private List<JobDetail> listJobDetail;
    private JobDetailViewModel jobDetailViewModel;
    private JobViewModel jobViewModel;
    private Job job;

    public JobDetailAdapter(Context mContext, JobDetailViewModel jobDetailViewModel, Job job) {
        this.jobDetailViewModel = jobDetailViewModel;
        this.mContext = mContext;
        this.job = job;
    }

    public void setData(List<JobDetail> jobDetails) {
        listJobDetail = jobDetails;
        listShowJobDetail = jobDetails;
    }

    @Override
    public JobDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.job_detail_item, parent, false);
        return new JobDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobDetailHolder holder, int position) {
        JobDetail item = listShowJobDetail.get(position);
        holder.tvJdName.setText(item.getName());
        holder.tvJdDes.setText(item.getDescription());
        holder.tvEstimatedTime.setText(String.valueOf(item.getEstimatedCompletedTime()));
        holder.tvActualTime.setText(String.valueOf(item.getActualCompletedTime()));
        if (item.isPriority())
            holder.img_Priority.setImageResource(R.drawable.ic_baseline_star_24);
        else
            holder.img_Priority.setImageResource(R.drawable.ic_baseline_star_outline_24);
        holder.checkBox.setChecked(item.getStatus());
        holder.jobDetailItem.setOnClickListener(v -> {
            if (Extension.canCheck(mContext, holder.checkBox, job)) {
                if (holder.checkBox.isChecked()) {
                    DialogUnCheckJobDetail(item, holder.checkBox);
                }
                if (!holder.checkBox.isChecked())
                    JobClock(item);
            }
        });

        holder.checkBox.setOnClickListener(v -> {
            if (Extension.canCheck(mContext, holder.checkBox, job))
                IsFinish(holder.checkBox, item);
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeleteJobDetail(item);
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddJobDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("JobDetailToUpdate", (Serializable) item);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void IsFinish(CheckBox checkBox, JobDetail jobDetail) {
        if (checkBox.isChecked()) {
            if (((JobDetailActivity) mContext).isRunning) {
                ((JobDetailActivity) mContext).complete();
            } else {
                jobDetail.setStatus(true);
                jobDetailViewModel.update(jobDetail);
            }
            notifyDataSetChanged();
        } else {
            DialogUnCheckJobDetail(jobDetail, checkBox);
        }
    }

    public void JobClock(JobDetail jobDetail) {
        Intent countIntent = new Intent(mContext, CountUpService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Key.SEND_JOB_DETAIL_BY_ACTIVITY, jobDetail);
        bundle.putInt(Key.SEND_ACTION, Action.ACTION_START);
        countIntent.putExtras(bundle);
        mContext.startService(countIntent);
    }

    void DialogDeleteJobDetail(JobDetail jobDetail) {
        final Dialog dialogYesNo = new Dialog(mContext);
        DialogExtension.dialogYesNo(dialogYesNo, mContext.getString(R.string.confirm_delete), mContext.getString(R.string.message_delete_all_job_detail));
        Button btn_yes = dialogYesNo.findViewById(R.id.btn_dialog_yes);
        Button btn_no = dialogYesNo.findViewById(R.id.btn_dialog_no);
        dialogYesNo.setCancelable(true);
        btn_yes.setOnClickListener(v -> {
            jobDetailViewModel.delete(jobDetail);
            dialogYesNo.dismiss();
        });
        btn_no.setOnClickListener(v -> dialogYesNo.dismiss());
        dialogYesNo.show();
    }

    void DialogUnCheckJobDetail(JobDetail jobDetail, CheckBox checkBox) {
        final Dialog dialogYesNo = new Dialog(mContext);
        DialogExtension.dialogYesNo(dialogYesNo, mContext.getString(R.string.confirm_delete), mContext.getString(R.string.message_uncheck_job_detail));
        Button btn_yes = dialogYesNo.findViewById(R.id.btn_dialog_yes);
        Button btn_no = dialogYesNo.findViewById(R.id.btn_dialog_no);
        dialogYesNo.setCancelable(true);
        btn_yes.setOnClickListener(v -> {
            jobDetail.setStatus(false);
            jobDetailViewModel.update(jobDetail);
            jobDetail.setActualCompletedTime(0);
            notifyDataSetChanged();
            dialogYesNo.dismiss();
        });
        btn_no.setOnClickListener(v -> {
            dialogYesNo.dismiss();
            checkBox.setChecked(true);
        });
        dialogYesNo.show();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strsearch = charSequence.toString();
                if (strsearch.isEmpty()) {
                    listShowJobDetail = listJobDetail;
                } else {
                    List<JobDetail> list = new ArrayList<>();
                    for (JobDetail j : listJobDetail) {
                        if (j.getName().toLowerCase().contains(strsearch.toLowerCase())) {
                            list.add(j);
                        }
                    }
                    listShowJobDetail = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listShowJobDetail;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listShowJobDetail = (List<JobDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public int getNumJobDetail(int status) {
        switch (status) {
            case GeneralData.NON_STATUS:
                return listJobDetail.size();
            case GeneralData.STATUS_DETAIL_FINISH:
                return numByStatus(true);
            default:
                return numByStatus(false);
        }
    }

    private int numByStatus(boolean status) {
        int num = 0;
        for (JobDetail jobDetail : listJobDetail
        ) {
            if (jobDetail.getStatus() == status)
                num++;
        }
        return num;
    }


    @Override
    public int getItemCount() {
        return listShowJobDetail.size();
    }

    public class JobDetailHolder extends RecyclerView.ViewHolder {
        LinearLayout jobDetailItem;
        TextView tvJdName;
        TextView tvJdDes;
        TextView tvEstimatedTime;
        TextView tvActualTime;
        ImageView img_Priority;
        CheckBox checkBox;
        FrameLayout delete;
        FrameLayout update;

        public JobDetailHolder(View itemView) {
            super(itemView);
            jobDetailItem = itemView.findViewById(R.id.ln_item_job_detail);
            tvJdName = itemView.findViewById(R.id.tv_jd_name);
            tvJdDes = itemView.findViewById(R.id.tv_jd_description);
            tvEstimatedTime = itemView.findViewById(R.id.tv_jd_estimated_time);
            tvActualTime = itemView.findViewById(R.id.tv_jd_actual_time);
            //SeekBar sb_Progress = vJobDetail.findViewById(R.id.sb_jd_progress);
            img_Priority = itemView.findViewById(R.id.img_jd_level);
            checkBox = itemView.findViewById(R.id.chk_finish_job_detail);
            delete = itemView.findViewById(R.id.frm_function_delete);
            update = itemView.findViewById(R.id.frm_function_update);
        }
    }
}
