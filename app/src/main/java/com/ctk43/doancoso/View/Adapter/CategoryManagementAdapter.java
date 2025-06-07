package com.ctk43.doancoso.View.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctk43.doancoso.Library.CalendarExtension;
import com.ctk43.doancoso.Library.DialogExtension;
import com.ctk43.doancoso.Library.Extension;
import com.ctk43.doancoso.Model.Category;
import com.ctk43.doancoso.Model.Job;
import com.ctk43.doancoso.R;
import com.ctk43.doancoso.ViewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementAdapter extends RecyclerView.Adapter<CategoryManagementAdapter.CategoryHolder> implements Filterable {
    private final Context mContext;
    private final CategoryViewModel categoryViewModel;
    private List<Category> categories;
    private List<Category> mlistCategoryOld;

    public CategoryManagementAdapter(Context mContext, List<Category> categories) {
        this.mContext = mContext;
        this.mlistCategoryOld = categories;
        this.categories = categories;
        categoryViewModel = new CategoryViewModel();
        categoryViewModel.setContext(mContext);
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = categories.get(position);
        holder.tv_category_name.setText(category.getName());

        int numberOfJobs = categoryViewModel.countJob(category.getId());
        holder.tv_category_number_job.setText(String.valueOf(numberOfJobs));

        holder.update.setOnClickListener(view -> DialogExtension.onOpenCategoryDiaLog(mContext, categoryViewModel, category));
        holder.ln_category_item.setOnClickListener(v -> {
            Job job = new Job(category.getId(), category.getName(), CalendarExtension.currDate(), CalendarExtension.currDate(), null);
            Extension.callJobActivity(mContext, job, false);
        });
        holder.delete.setOnClickListener(view -> DialogDeleteCategory(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private void DialogDeleteCategory(Category category) {
        final Dialog dialogYesNo = new Dialog(mContext);
        DialogExtension.dialogYesNo(dialogYesNo, mContext.getString(R.string.confirm_delete), mContext.getString(R.string.message_delete_category));
        Button btn_yes = dialogYesNo.findViewById(R.id.btn_dialog_yes);
        Button btn_no = dialogYesNo.findViewById(R.id.btn_dialog_no);
        dialogYesNo.setCancelable(true);
        btn_yes.setOnClickListener(v -> {
            categoryViewModel.delete(category);
            dialogYesNo.dismiss();
        });
        btn_no.setOnClickListener(v -> dialogYesNo.dismiss());
        dialogYesNo.show();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strsearch = charSequence.toString();
                if (strsearch.isEmpty()) {
                    categories = mlistCategoryOld;
                } else {
                    List<Category> list = new ArrayList<>();
                    for (Category j : mlistCategoryOld) {
                        if (j.getName().toLowerCase().contains(strsearch.toLowerCase())) {
                            list.add(j);
                        }
                    }
                    categories = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = categories;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categories = (List<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {
        TextView tv_category_name;
        TextView tv_category_number_job;

        FrameLayout delete;
        FrameLayout update;
        LinearLayout ln_category_item;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            tv_category_number_job = itemView.findViewById(R.id.tv_category_number_job);
            ln_category_item = itemView.findViewById(R.id.ln_category_item);
            delete = itemView.findViewById(R.id.frm_function_delete);
            update = itemView.findViewById(R.id.frm_function_update);
        }
    }
}
