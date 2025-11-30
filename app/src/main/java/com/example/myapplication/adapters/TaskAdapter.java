package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Task;
import com.example.myapplication.ui.AddEditTaskActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private Context context;
    private OnTaskDeleteListener deleteListener;
    private OnTaskCompleteListener completeListener;

    public interface OnTaskDeleteListener {
        void onTaskDeleted(Task task);
    }

    public interface OnTaskCompleteListener {
        void onTaskCompleted(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void setDeleteListener(OnTaskDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setCompleteListener(OnTaskCompleteListener listener) {
        this.completeListener = listener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV;
        private TextView descriptionTV;
        private TextView dueDateTV;
        private TextView priorityTV;
        private ImageView editBtnIV;
        private ImageView deleteBtnIV;
        private ImageView completeBtnIV;
        private View priorityIndicator;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title_tv);
            descriptionTV = itemView.findViewById(R.id.description_tv);
            dueDateTV = itemView.findViewById(R.id.due_date_tv);
            priorityTV = itemView.findViewById(R.id.priority_tv);
            editBtnIV = itemView.findViewById(R.id.edit_btn);
            deleteBtnIV = itemView.findViewById(R.id.delete_btn);
            completeBtnIV = itemView.findViewById(R.id.complete_btn);
            priorityIndicator = itemView.findViewById(R.id.priority_indicator);
        }

        public void bind(Task task) {
            titleTV.setText(task.getTitle());
            descriptionTV.setText(task.getDescription());
            dueDateTV.setText("Due: " + task.getDueDate());
            priorityTV.setText(task.getPriorityString());

            // Set priority color
            int priorityColor = getPriorityColor(task.getPriority());
            priorityIndicator.setBackgroundColor(priorityColor);
            priorityTV.setTextColor(priorityColor);

            // Set title strikethrough if completed
            if (task.isCompleted()) {
                titleTV.setPaintFlags(titleTV.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                titleTV.setAlpha(0.6f);
            } else {
                titleTV.setPaintFlags(titleTV.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                titleTV.setAlpha(1.0f);
            }

            // Edit button click
            editBtnIV.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEditTaskActivity.class);
                intent.putExtra("task_id", task.getId());
                context.startActivity(intent);
            });

            // Delete button click
            deleteBtnIV.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onTaskDeleted(task);
                }
            });

            // Complete button click
            completeBtnIV.setOnClickListener(v -> {
                task.setCompleted(!task.isCompleted());
                if (completeListener != null) {
                    completeListener.onTaskCompleted(task);
                }
            });
        }

        private int getPriorityColor(int priority) {
            switch (priority) {
                case Task.PRIORITY_HIGH:
                    return Color.parseColor("#FF6B6B");
                case Task.PRIORITY_MEDIUM:
                    return Color.parseColor("#FFA500");
                case Task.PRIORITY_LOW:
                default:
                    return Color.parseColor("#4ECDC4");
            }
        }
    }
}