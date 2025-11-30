package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TaskAdapter;
import com.example.myapplication.database.TaskDAO;
import com.example.myapplication.models.Task;
import com.example.myapplication.utils.TaskComparator;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity11 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private FloatingActionButton addFab;
    private TaskDAO taskDAO;
    private List<Task> taskList = new ArrayList<>();
    private Toolbar toolbar;
    private ChipGroup filterChipGroup;


    private TextView totalTasksTv, completedTasksTv, pendingTasksTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        initializeViews();
        setupRecyclerView();
        setupFAB();
        setupFilterChips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.tasks_recycler_view);
        addFab = findViewById(R.id.add_task_fab);
        taskDAO = new TaskDAO(this);
        filterChipGroup = findViewById(R.id.filter_chip_group);


        totalTasksTv = findViewById(R.id.total_tasks_tv);
        completedTasksTv = findViewById(R.id.completed_tasks_tv);
        pendingTasksTv = findViewById(R.id.pending_tasks_tv);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(this, taskList);


        adapter.setDeleteListener(task -> {
            taskDAO.deleteTask(task.getId());
            loadTasks(); // Reload tasks to reflect deletion
            Toast.makeText(MainActivity11.this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
        });


        adapter.setCompleteListener(task -> {
            taskDAO.updateTask(task);
            loadTasks(); // Reload tasks to reflect completion status change
        });

        recyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity11.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }

    private void setupFilterChips() {
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            filterTasks(checkedId);
        });
    }

    private void filterTasks(int checkedId) {
        List<Task> allTasks = taskDAO.getAllTasks();
        List<Task> filteredList = new ArrayList<>();

        if (checkedId == R.id.chip_all) {
            filteredList.addAll(allTasks);
        } else if (checkedId == R.id.chip_today) {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            for (Task task : allTasks) {
                if (task.getDueDate().equals(today)) {
                    filteredList.add(task);
                }
            }
        } else if (checkedId == R.id.chip_upcoming) {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            for (Task task : allTasks) {
                if (task.getDueDate().compareTo(today) > 0) {
                    filteredList.add(task);
                }
            }
        } else if (checkedId == R.id.chip_completed) {
            for (Task task : allTasks) {
                if (task.isCompleted()) {
                    filteredList.add(task);
                }
            }
        }

        Collections.sort(filteredList, new TaskComparator());
        adapter.updateTasks(filteredList);
    }


    private void loadTasks() {
        int checkedId = filterChipGroup.getCheckedChipId();
        filterTasks(checkedId);
        updateDashboard();
    }

    private void updateDashboard() {
        List<Task> allTasks = taskDAO.getAllTasks();
        int total = allTasks.size();
        int completed = 0;
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        int pending = total - completed;

        totalTasksTv.setText(String.valueOf(total));
        completedTasksTv.setText(String.valueOf(completed));
        pendingTasksTv.setText(String.valueOf(pending));
    }
}