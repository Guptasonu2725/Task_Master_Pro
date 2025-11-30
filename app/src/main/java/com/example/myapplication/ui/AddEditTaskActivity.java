package com.example.myapplication.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.TaskDAO;
import com.example.myapplication.models.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText titleET;
    private EditText descriptionET;
    private RadioGroup priorityRG;
    private TextView dueDateTV;
    private Button saveBtn;
    private Button cancelBtn;

    private TaskDAO taskDAO;
    private Task currentTask;
    private int taskId = -1;
    private String selectedDate;
    private final Calendar calendar = Calendar.getInstance();
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        initializeViews();
        setupDatePicker();
        checkIfEditingTask();
        setupListeners();
    }

    private void initializeViews() {
        titleET = findViewById(R.id.title_et);
        descriptionET = findViewById(R.id.description_et);
        priorityRG = findViewById(R.id.priority_rg);
        dueDateTV = findViewById(R.id.due_date_tv);
        saveBtn = findViewById(R.id.save_btn);
        cancelBtn = findViewById(R.id.cancel_btn);

        taskDAO = new TaskDAO(this);

        // Set default date to today
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        selectedDate = sdf.format(new Date());
        dueDateTV.setText(selectedDate);

        // Set default priority to Low
        priorityRG.check(R.id.priority_low_rb);
    }

    private void setupDatePicker() {
        dueDateTV.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            selectedDate = sdf.format(calendar.getTime());
            dueDateTV.setText(selectedDate);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void checkIfEditingTask() {
        if (getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", -1);
            currentTask = taskDAO.getTaskById(taskId);

            if (currentTask != null) {
                titleET.setText(currentTask.getTitle());
                descriptionET.setText(currentTask.getDescription());
                dueDateTV.setText(currentTask.getDueDate());
                selectedDate = currentTask.getDueDate();

                int priorityRadioId = getPriorityRadioId(currentTask.getPriority());
                priorityRG.check(priorityRadioId);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Edit Task");
                }
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add New Task");
            }
        }
    }

    private int getPriorityRadioId(int priority) {
        switch (priority) {
            case Task.PRIORITY_HIGH:
                return R.id.priority_high_rb;
            case Task.PRIORITY_MEDIUM:
                return R.id.priority_medium_rb;
            case Task.PRIORITY_LOW:
            default:
                return R.id.priority_low_rb;
        }
    }

    private void setupListeners() {
        saveBtn.setOnClickListener(v -> saveTask());
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void saveTask() {
        String title = titleET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.enter_task_title, Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPriorityId = priorityRG.getCheckedRadioButtonId();
        int priority = getPriorityFromRadioId(selectedPriorityId);

        if (taskId == -1) {
            // Create new task
            Task newTask = new Task(title, description, priority, selectedDate);
            taskDAO.addTask(newTask);
            Toast.makeText(this, R.string.task_added_success, Toast.LENGTH_SHORT).show();
        } else {
            // Update existing task
            currentTask.setTitle(title);
            currentTask.setDescription(description);
            currentTask.setPriority(priority);
            currentTask.setDueDate(selectedDate);
            taskDAO.updateTask(currentTask);
            Toast.makeText(this, R.string.task_updated_success, Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private int getPriorityFromRadioId(int radioId) {
        if (radioId == R.id.priority_high_rb) {
            return Task.PRIORITY_HIGH;
        } else if (radioId == R.id.priority_medium_rb) {
            return Task.PRIORITY_MEDIUM;
        } else {
            return Task.PRIORITY_LOW;
        }
    }
}