package com.example.myapplication.models;

public class Task {
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_HIGH = 2;

    private int id;
    private String title;
    private String description;
    private int priority;
    private String dueDate;
    private boolean completed;
    private String createdAt;

    // Constructor (for new tasks)
    public Task(String title, String description, int priority, String dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
    }

    // Full constructor (for retrieved tasks)
    public Task(int id, String title, String description, int priority,
                String dueDate, boolean completed, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public String getPriorityString() {
        switch (priority) {
            case PRIORITY_HIGH:
                return "High";
            case PRIORITY_MEDIUM:
                return "Medium";
            case PRIORITY_LOW:
                return "Low";
            default:
                return "Unknown";
        }
    }

    public static int getPriorityFromString(String priorityStr) {
        switch (priorityStr.toLowerCase()) {
            case "high":
                return PRIORITY_HIGH;
            case "medium":
                return PRIORITY_MEDIUM;
            case "low":
                return PRIORITY_LOW;
            default:
                return PRIORITY_LOW;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}