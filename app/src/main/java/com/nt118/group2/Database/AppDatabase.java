package com.nt118.group2.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nt118.group2.Database.DAO.CategoryDAO;
import com.nt118.group2.Database.DAO.JobDAO;
import com.nt118.group2.Database.DAO.JobDetailDAO;
import com.nt118.group2.Database.DAO.NotificationModelDAO;
import com.nt118.group2.Database.DAO.UserDAO;
import com.nt118.group2.Library.CalendarExtension;
import com.nt118.group2.Model.Category;
import com.nt118.group2.Model.Job;
import com.nt118.group2.Model.JobDetail;
import com.nt118.group2.Model.NotificationModel;
import com.nt118.group2.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Database(entities = {Category.class, Job.class, JobDetail.class, User.class , NotificationModel.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "JobManagement.db";

    private static final RoomDatabase.Callback CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new SampledData(instance).execute();
        }
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static AppDatabase instance;

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .addCallback(CALLBACK)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CategoryDAO getCategoryDAO();

    public abstract JobDAO getJobDAO();

    public abstract JobDetailDAO getJobDetailDAO();

    public abstract UserDAO getUserDAO();

    public abstract NotificationModelDAO getNotificationModelDAO();

    private static class SampledData extends AsyncTask<Void, Void, Void> {
        private final CategoryDAO categoryDAO;
        private final JobDAO jobDAO;
        private final JobDetailDAO jobDetailDAO;
        private final UserDAO userDAO;

        private SampledData(AppDatabase db) {
            super();
            categoryDAO = db.getCategoryDAO();
            jobDAO = db.getJobDAO();
            userDAO = db.getUserDAO();
            jobDetailDAO = db.getJobDetailDAO();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            String Date = "31/12/2021";
            Date date;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(Date);
                assert date != null;
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.set(Calendar.HOUR_OF_DAY, 6); // for 6 hour
            calendar.set(Calendar.MINUTE, 0); // for 0 min
            calendar.set(Calendar.SECOND, 0); // for 0 sec
            Date start = Calendar.getInstance().getTime();
            Date end = calendar.getTime();
            userDAO.insert(new User("default@example.vn","Người dùng"));
            categoryDAO.insert(new Category("Mặc định","default@example.vn"),
                    new Category("Học tập","default@example.vn"),
                    new Category("Giải trí","default@example.vn"));
            jobDAO.insert(new Job(1, "Nghiên cứu SQLite", CalendarExtension.getEndDateOfWeek(start,-1), CalendarExtension.getEndDateOfWeek(start,0), "Tìm hiểu cơ sở dữ liệu SQLite cho dự án"));
            jobDAO.insert(new Job(1, "Làm bài tập Lab 8", CalendarExtension.getDateStartOfMonth(2,2022),CalendarExtension.getDateEndOfMonth(5,2022), "Hoàn thành bài tập môn Lập trình ứng dụng di động"));
            jobDAO.insert(new Job(1, "Phát triển ứng dụng Android", CalendarExtension.getDateStartOfMonth(3,2022), CalendarExtension.getDateEndOfMonth(3,2022), "Xây dựng ứng dụng quản lý công việc"));
            jobDAO.insert(new Job(1, "Thiết kế giao diện web", CalendarExtension.getDateStartOfMonth(4,2022), CalendarExtension.getDateEndOfMonth(4,2022), "Thiết kế UI/UX cho website quản lý"));
            jobDAO.insert(new Job(1, "Ôn tập cuối kỳ", CalendarExtension.getEndDateOfWeek(start,0), CalendarExtension.getStartTimeOfWeek(start,1), "Chuẩn bị cho kỳ thi kết thúc môn"));
            jobDAO.insert(new Job(1, "Nấu ăn cuối tuần",CalendarExtension.getEndDateOfWeek(start,1), CalendarExtension.getStartTimeOfWeek(start,1), "Chuẩn bị bữa ăn gia đình"));
            jobDAO.insert(new Job(1, "Làm đồ án tốt nghiệp", CalendarExtension.getStartTimeOfWeek(start,-1), CalendarExtension.getEndTimeOfWeek(start,0), "Phát triển hệ thống quản lý đồ án"));
            jobDAO.insert(new Job(1, "Họp nhóm dự án", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,0), "Bàn giao công việc giữa các thành viên"));

            jobDAO.insert(new Job(3, "Viết báo cáo tuần", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,1), "Tổng hợp kết quả công việc tuần",0));
            jobDAO.insert(new Job(3, "Kiểm tra code", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,2), "Review code cho đồ án",1));
            jobDAO.insert(new Job(3, "Cập nhật tài liệu", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,1), "Bổ sung tài liệu hướng dẫn",3));
            jobDAO.insert(new Job(3, "Tìm hiểu công nghệ mới", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,0), "Nghiên cứu framework mới",2));
            jobDAO.insert(new Job(3, "Fix bug ứng dụng", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,1), "Sửa lỗi giao diện",1));
            jobDAO.insert(new Job(3, "Thuyết trình đồ án", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,1), "Chuẩn bị slide thuyết trình",0));
            jobDAO.insert(new Job(3, "Gặp gỡ cố vấn", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,1), "Trao đổi với giảng viên hướng dẫn",1));
            jobDAO.insert(new Job(3, "Triển khai hệ thống", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,2), "Deploy ứng dụng lên server",2));
            jobDAO.insert(new Job(3, "Test ứng dụng", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,4), "Kiểm thử tính năng mới",1));
            jobDAO.insert(new Job(3, "Học online", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,4), "Tham gia lớp học trực tuyến",3));
            jobDAO.insert(new Job(3, "Hoàn thiện luận văn", CalendarExtension.getStartTimeOfWeek(start,-3), CalendarExtension.getEndTimeOfWeek(start,4), "Chỉnh sửa luận văn tốt nghiệp",3));
            jobDAO.insert(new Job(3, "Làm bài tập lớn", CalendarExtension.getStartTimeOfWeek(start,-2), CalendarExtension.getEndTimeOfWeek(start,4), "Hoàn thành bài tập nhóm",1));
            jobDAO.insert(new Job(3, "Thực hành phòng lab", CalendarExtension.getStartTimeOfWeek(start,1), CalendarExtension.getEndTimeOfWeek(start,3), "Làm bài thực hành tại phòng máy",0));
            jobDAO.insert(new Job(3, "Đăng ký môn học", CalendarExtension.getStartTimeOfWeek(start,-1), CalendarExtension.getEndTimeOfWeek(start,1), "Đăng ký môn học kỳ mới",0));

            jobDAO.insert(new Job(2, "Bảo vệ đồ án", CalendarExtension.getEndDateOfWeek(start,-1), CalendarExtension.getStartTimeOfWeek(start,1), "Chuẩn bị bảo vệ đồ án tốt nghiệp",1));
            jobDAO.insert(new Job(2, "Viết tài liệu", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,0), "Hoàn thiện tài liệu hướng dẫn"));
            jobDAO.insert(new Job(2, "Khảo sát yêu cầu", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,0), "Thu thập yêu cầu khách hàng"));
            jobDAO.insert(new Job(2, "Phân tích hệ thống", CalendarExtension.getStartTimeOfWeek(start,-3), CalendarExtension.getEndTimeOfWeek(start,0), "Phân tích yêu cầu hệ thống"));
            jobDAO.insert(new Job(2, "Thiết kế database", CalendarExtension.getStartTimeOfWeek(start,0), CalendarExtension.getEndTimeOfWeek(start,-1), "Thiết kế cơ sở dữ liệu"));
            jobDAO.insert(new Job(2, "Xây dựng API", CalendarExtension.getStartTimeOfWeek(start,-3), CalendarExtension.getEndTimeOfWeek(start,-1), "Phát triển API backend"));
            jobDAO.insert(new Job(2, "Tối ưu hiệu năng", CalendarExtension.getStartTimeOfWeek(start,-2), CalendarExtension.getEndTimeOfWeek(start,-1), "Cải thiện hiệu suất ứng dụng"));
            jobDAO.insert(new Job(2, "Nghiên cứu khoa học", CalendarExtension.getStartTimeOfWeek(start,-4),CalendarExtension.getEndTimeOfWeek(start,1), "Viết bài báo khoa học"));
            jobDAO.insert(new Job(2, "Tham gia hội thảo", CalendarExtension.getStartTimeOfWeek(start,-2), CalendarExtension.getEndTimeOfWeek(start,0), "Tham dự hội thảo công nghệ"));
            jobDAO.insert(new Job(2, "Demo sản phẩm", CalendarExtension.getStartTimeOfWeek(start,-3), CalendarExtension.getEndTimeOfWeek(start,1), "Trình bày sản phẩm cuối kỳ"));

            jobDetailDAO.insert(new JobDetail(1, "Tìm hiểu SQLite", 270, "Tìm hiểu thôi"));
            jobDetailDAO.insert(new JobDetail(1, "Làm app demo", 270, "Làm kiểu j á",true));
            jobDetailDAO.insert(new JobDetail(1, "Ghi j bây giờ", 270, "Ghi cho có vậy"));
            jobDetailDAO.insert(new JobDetail(3, "Câu gì á kì lạ", 50, "Tìm hiểu thôi",true));
            jobDetailDAO.insert(new JobDetail(3, "Câu gì á kì lạ", 550, "Tìm hiểu thôi"));
            jobDetailDAO.insert(new JobDetail(3, "Câu gì á kì lạ-1", 1100, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(3, "Cây gì á kì lạày thú vị", 200, "GhiKhơn học được có cái bản chưa nâng cấp nghi lắm"));
            jobDetailDAO.insert(new JobDetail(3, "Câu gì á kì lạ", 100, "Tìm hiểu thôi",true));
            jobDetailDAO.insert(new JobDetail(3, "Khơn học được ", 200, "Tìm hiểu thôi",true));
            jobDetailDAO.insert(new JobDetail(4, "Khơn học được -1", 50, "Làm kiểu j á",true));
            jobDetailDAO.insert(new JobDetail(4, "Khơn học được ày thú vị", 70, "Ghi cho có cái bản chưa nâng cấp nghi lắm"));
            jobDetailDAO.insert(new JobDetail(4, "Khơn học được ", 80, "Tìm hiểu thôi"));
            jobDetailDAO.insert(new JobDetail(4, "Khơn học được ", 40, "Tìm hiểu thôi"));
            jobDetailDAO.insert(new JobDetail(4, "con cá là con cá zàng N-1", 1100, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(4, "con cá là con cá zàng này thú vị", 350, "Ghi cho có cái bản chưa nâng cấp nghi lắm",true));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng 2", 123, "Chẳng qua là thú zị kìa"));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng 3", 231, "Chẳng qua là thú zị kìa"));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng N-1", 157, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng này thú vị", 3587, "Ghi cho có cái bản chưa nâng cấp nghi lắm",true));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng 2", 300, "Chẳng qua là thú zị kìa"));
            jobDetailDAO.insert(new JobDetail(5, "con cá là con cá zàng 3", 300, "Chẳng qua là thú zị kìa"));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhở-1", 300, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhởày thú vị", 300, "Ghi cho có vậy"));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhở", 300, "Khi trầm cảm online lên tiếng"));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhở", 300, "Khi trầm cảm online lên tiếng",true));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhở-1", 300, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(6, "Người đừng hòng thú zị thế nhởày thú vị", 300, "Ghi cho có vậy"));
            jobDetailDAO.insert(new JobDetail(7, "Người đừng hòng thú zị thế nhở", 300, "Khi trầm cảm online lên tiếng"));
            jobDetailDAO.insert(new JobDetail(7, "Người đừng hòng thú zị thế nhở", 300, "Khi trầm cảm online lên tiếng",true));
            jobDetailDAO.insert(new JobDetail(7, "C-1", 300, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(7, "Cày thú vị", 300, "Ghi cho có vậy"));
            jobDetailDAO.insert(new JobDetail(7, "C", 300, "Mũi tên trái phải trên dưới alo",true));
            jobDetailDAO.insert(new JobDetail(7, "C", 300, "Mũi tên trái phải trên dưới alo",true));
            jobDetailDAO.insert(new JobDetail(8, "C-1", 300, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(8, "Cày thú vị", 300, "Ghi cho có vậy"));
            jobDetailDAO.insert(new JobDetail(8, "C", 300, "Mũi tên trái phải trên dưới alo",true));
            jobDetailDAO.insert(new JobDetail(8, "3", 300, "Mũi tên trái phải trên dưới alo",true));
            jobDetailDAO.insert(new JobDetail(8, "Câu N-1", 300, "Làm kiểu j á"));
            jobDetailDAO.insert(new JobDetail(8, "Cây này thú vị", 300, "Ghi cho có vậy"));

            return null;
        }
    }
}
