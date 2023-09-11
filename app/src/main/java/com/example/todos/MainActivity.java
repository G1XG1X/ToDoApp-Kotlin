package com.example.todos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_TASK_REQUEST_CODE = 1;
    private Calendar currentDisplayedDate;
    private TextView dateTextView;
    private final List<Task> toDoTasksList = new ArrayList<>();
    private final List<Task> completedTasksList = new ArrayList<>();
    private TaskAdapter toDoTasksAdapter;
    private static final String TASKS_FILE_NAME = "tasks.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTextView = findViewById(R.id.textViewDate);
        ListView toDoListView = findViewById(R.id.toDoList);

        loadTasksFromFile();

        toDoTasksAdapter = new TaskAdapter(this, (ArrayList<Task>) toDoTasksList, this);
        toDoListView.setAdapter(toDoTasksAdapter);

        currentDisplayedDate = Calendar.getInstance();
        displayDate();

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Button newTaskButton = findViewById(R.id.newTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                if (!isDateValid(currentDisplayedDate)) {
                    // Jeżeli wybrana data jest przed dzisiejszą datą
                    Toast.makeText(MainActivity.this, "Incorrect Date", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                    intent.putExtra("SELECTED_DATE", new SimpleDateFormat
                            ("dd MMMM yyyy", Locale.getDefault()).format(currentDisplayedDate.getTime()));
                    startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Odbierz dane z intencji i dodaj zadanie do listy
                String newTask = data.getStringExtra("NEW_TASK");
                String taskDate = data.getStringExtra("TASK_DATE");
                String taskTime = data.getStringExtra("TASK_TIME");
                String taskNotes = data.getStringExtra("TASK_NOTES");
                int selectedIconResId = data.getIntExtra("SELECTED_ICON", 0); // Odbieranie wybranej ikony

                Task taskObject = new Task(newTask, false, selectedIconResId, taskDate, taskTime, taskNotes);
                toDoTasksList.add(taskObject);
                toDoTasksAdapter.notifyDataSetChanged();
                Toast.makeText(this, "New Task Added: " + newTask, Toast.LENGTH_SHORT).show();
                saveTasksToFile();
            }
        }
    }
    public void saveTasksToFile() {
        File file = new File(getFilesDir(), TASKS_FILE_NAME);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            Gson gson = new Gson();
            gson.toJson(toDoTasksList, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void loadTasksFromFile() {
        File file = new File(getFilesDir(), TASKS_FILE_NAME);
        if (file.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(file);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Task>>(){}.getType();
                List<Task> loadedTasks = gson.fromJson(reader, listType);
                toDoTasksList.clear();
                toDoTasksList.addAll(loadedTasks);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            toDoTasksList.clear();
        }
    }
    private boolean isDateValid(Calendar selectedDate) {
        Calendar today = Calendar.getInstance();

        clearTimeComponents(today);
        clearTimeComponents(selectedDate);

        return !selectedDate.before(today);
    }

    private void clearTimeComponents(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }

    private void displayDate() {
        String formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(currentDisplayedDate.getTime());
        dateTextView.setText(formattedDate);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentDisplayedDate.set(Calendar.YEAR, year);
                        currentDisplayedDate.set(Calendar.MONTH, month);
                        currentDisplayedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        displayDate();
                    }
                },
                currentDisplayedDate.get(Calendar.YEAR),
                currentDisplayedDate.get(Calendar.MONTH),
                currentDisplayedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
