package com.example.todos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import androidx.appcompat.widget.Toolbar;

public class AddTaskActivity extends AppCompatActivity {
    private int selectedIconResId = 0;
    private Calendar selectedDateTime;
    private TextView dateText;
    private TextView timeText;

    // Deklaracja tablicy z ikonami
    private ImageView[] iconViews;
    private final int[] iconResIds = {
            R.drawable.ic_phone, R.drawable.ic_favorite, R.drawable.ic_home,
            R.drawable.ic_person, R.drawable.ic_pets, R.drawable.ic_school,
            R.drawable.ic_work
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        Toolbar toolbar = findViewById(R.id.addTaskToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editTextTask = findViewById(R.id.editTextTask);
        final EditText editTextNotes = findViewById(R.id.editTextNotes);
        Button saveButton = findViewById(R.id.saveButton);

        // Inicjalizacja tablicy ikon
        iconViews = new ImageView[]{
                findViewById(R.id.phone),
                findViewById(R.id.favorite),
                findViewById(R.id.home),
                findViewById(R.id.person),
                findViewById(R.id.pets),
                findViewById(R.id.school),
                findViewById(R.id.work)
        };

        selectedDateTime = Calendar.getInstance();
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);

        LinearLayout dateClick = findViewById(R.id.dateClick);
        LinearLayout timeClick = findViewById(R.id.timeClick);

        dateClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDateTime.set(year, month, dayOfMonth);
                        dateText.setText(String.format("%02d-%02d-%d", dayOfMonth, month + 1, year));
                        dateText.setVisibility(View.VISIBLE);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        timeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        // Aktualizuj TextView z wybraną godziną
                        timeText.setText(String.format("%02d:%02d", hourOfDay, minute));
                        timeText.setVisibility(View.VISIBLE);
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });


        View.OnClickListener iconClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jeżeli ikona jest już zaznaczona
                if (v.getTag() != null && v.getTag().equals("selected")) {
                    v.setBackgroundResource(0); // Odznacz ikonę
                    v.setTag(null); // Usuń znacznik "selected"
                    selectedIconResId = 0; // Resetuj ID wybranej ikony
                    return; // Zakończ metodę
                }

                // Resetowanie tła dla wszystkich ikon
                for (ImageView iconView : iconViews) {
                    iconView.setBackgroundResource(0);
                    iconView.setTag(null);
                }

                // Ustawienie wybranej ikony
                for (int i = 0; i < iconViews.length; i++) {
                    if (v.getId() == iconViews[i].getId()) {
                        selectedIconResId = iconResIds[i];
                        iconViews[i].setBackgroundResource(R.drawable.ic_border);
                        iconViews[i].setTag("selected");
                        break;
                    }
                }
            }
        };

        // Ustawienie listenera dla wszystkich ikon
        for (ImageView iconView : iconViews) {
            iconView.setOnClickListener(iconClickListener);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = editTextTask.getText().toString();
                String notesText = editTextNotes.getText().toString();
                if (!taskText.isEmpty()) {
                    Intent resultIntent = new Intent();
                    String taskTime = timeText.getText().toString();
                    String taskDate = dateText.getText().toString();

                    resultIntent.putExtra("TASK_TIME", taskTime);
                    resultIntent.putExtra("TASK_DATE", taskDate);
                    resultIntent.putExtra("TASK_NOTES", notesText);
                    resultIntent.putExtra("NEW_TASK", taskText);
                    resultIntent.putExtra("SELECTED_ICON", selectedIconResId);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
