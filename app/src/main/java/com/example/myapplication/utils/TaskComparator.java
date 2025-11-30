package com.example.myapplication.utils;

import com.example.myapplication.models.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class TaskComparator implements Comparator<Task> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Override
    public int compare(Task t1, Task t2) {
        // First, sort by priority (High > Medium > Low)
        int priorityComparison = Integer.compare(t2.getPriority(), t1.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // If priorities are equal, sort by due date (earlier dates first)
        try {
            Date date1 = sdf.parse(t1.getDueDate());
            Date date2 = sdf.parse(t2.getDueDate());

            if (date1 != null && date2 != null) {
                return date1.compareTo(date2);
            } else if (date1 != null) {
                return -1;
            } else if (date2 != null) {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // If dates are equal or invalid, maintain original order
        return Integer.compare(t1.getId(), t2.getId());
    }
}