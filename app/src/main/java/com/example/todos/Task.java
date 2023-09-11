package com.example.todos;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String taskName;
    private boolean isCompleted;
    private int iconResId = -1; // brak ikony
    private String time;
    private String date;
    private String notes;

    public Task(String taskName, boolean isCompleted, int iconResId, String date, String time, String notes) {
        this.taskName = taskName;
        this.isCompleted = isCompleted;
        this.iconResId = iconResId;
        this.time = time;
        this.date = date;
        this.notes = notes;
    }

    public Task(String taskName, boolean isCompleted, String date, String time, String notes) {
        this(taskName, isCompleted, -1, date, time, notes);
    }

    protected Task(Parcel in) {
        taskName = in.readString();
        isCompleted = in.readByte() != 0;  // my boolean
        iconResId = in.readInt();
        date = in.readString();
        time = in.readString();
        notes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeInt(iconResId);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}



