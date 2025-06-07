package com.nt118.group2.Database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConvertor {
    @TypeConverter
    public static Date toDate(long dateLong){
        return new Date(dateLong);
    }

    @TypeConverter
    public static long fromDate(Date date){
        return date == null ? 0 : date.getTime();
    }
}
