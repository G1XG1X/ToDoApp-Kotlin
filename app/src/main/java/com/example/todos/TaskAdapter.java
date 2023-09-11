package com.example.todos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    private final Context mContext;
    private final ArrayList<Task> tasks;
    private final MainActivity mainActivity;

    public TaskAdapter(Context context, ArrayList<Task> tasks, MainActivity mainActivity) {
        super(context, 0, tasks);
        this.mContext = context;
        this.tasks = tasks;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Pobierz dane dla tej pozycji
        Task currentTask = getItem(position);
        // Sprawdź, czy istniejący widok jest używany ponownie
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
        }
        // Znajdź widoki do modyfikacji w widoku elementu
        CheckBox taskCheckBox = convertView.findViewById(R.id.taskCheckBox);
        TextView taskNameTextView = convertView.findViewById(R.id.taskNameTextView);
        ImageView taskIconImageView = convertView.findViewById(R.id.taskIconImageView);
        TextView taskDateTextView = convertView.findViewById(R.id.taskDateTextView);
        TextView taskTimeTextView = convertView.findViewById(R.id.taskTimeTextView);
        TextView taskNotesTextView = convertView.findViewById(R.id.taskNotesTextView);

        assert currentTask != null;
        taskNameTextView.setText(currentTask.getTaskName());
        taskCheckBox.setChecked(currentTask.isCompleted());

        if (currentTask.getIconResId() != -1) {
            taskIconImageView.setImageResource(currentTask.getIconResId());
            taskIconImageView.setVisibility(View.VISIBLE);
        } else {
            taskIconImageView.setVisibility(View.GONE);
        }
        //Ustaw date
        taskDateTextView.setText(currentTask.getDate());
        // Ustaw godzinę
        taskTimeTextView.setText(currentTask.getTime());
        // Ustaw notatki
        taskNotesTextView.setText(currentTask.getNotes());
        // Ustaw listener dla checkboxa
        taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tasks.remove(currentTask); // Usuń zadanie z listy
                notifyDataSetChanged();   // Informuj adapter o zmianie danych
                mainActivity.saveTasksToFile();
            }
        });
        return convertView;
    }
}