package com.example.todos

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var dateTextView: TextView
    private val toDoTasksList: MutableList<Task?> = mutableListOf()
    private lateinit var toDoTasksAdapter: TaskAdapter
    private var currentDisplayedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        loadTasksFromFile()

        toDoTasksAdapter = TaskAdapter(this, toDoTasksList, this)
        findViewById<ListView>(R.id.toDoList).adapter = toDoTasksAdapter

        displayDate()
    }
    private fun initViews() {
        dateTextView = findViewById(R.id.textViewDate)
        dateTextView.setOnClickListener { showDatePicker() }

        val newTaskButton: Button = findViewById(R.id.newTaskButton)
        newTaskButton.setOnClickListener { onNewTaskButtonClicked() }
    }
    private fun onNewTaskButtonClicked() {
        if (!isDateValid(currentDisplayedDate)) {
            Toast.makeText(this@MainActivity, "Incorrect Date", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this@MainActivity, AddTaskActivity::class.java).apply {
            putExtra(
                "SELECTED_DATE",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(currentDisplayedDate.time)
            )
        }
        startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val newTask = data.getStringExtra("NEW_TASK")
            if (newTask != null) {
                addNewTask(data, newTask)
            } else {
                Toast.makeText(this, "Error retrieving new task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewTask(data: Intent, newTask: String) {
        val taskObject = Task(
            title = newTask,
            isCompleted = false,
            iconResId = data.getIntExtra("SELECTED_ICON", 0),
            date = data.getStringExtra("TASK_DATE") ?: "",
            time = data.getStringExtra("TASK_TIME") ?: "",
            notes = data.getStringExtra("TASK_NOTES") ?: ""
        )

        toDoTasksList.add(taskObject)
        toDoTasksAdapter.notifyDataSetChanged()

        Toast.makeText(this, "New Task Added: $newTask", Toast.LENGTH_SHORT).show()

        saveTasksToFile()
    }

    fun saveTasksToFile() {
        val file = File(filesDir, TASKS_FILE_NAME)

        try {
            file.writer().use {
                val gson = Gson()
                gson.toJson(toDoTasksList, it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun loadTasksFromFile() {
        val file = File(filesDir, TASKS_FILE_NAME)
        if (file.exists()) {
            try {
                file.reader().use {
                    val listType = object : TypeToken<List<Task?>?>() {}.type
                    val gson = Gson()
                    val loadedTasks: List<Task?> = gson.fromJson(it, listType)
                    toDoTasksList.clear()
                    toDoTasksList.addAll(loadedTasks)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun isDateValid(selectedDate: Calendar): Boolean {
        val today = Calendar.getInstance().apply { clearTimeComponents() }
        selectedDate.clearTimeComponents()
        return !selectedDate.before(today)
    }

    private fun Calendar.clearTimeComponents() {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private fun displayDate() {
        dateTextView.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(currentDisplayedDate.time)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this@MainActivity,
            { _, year, month, dayOfMonth ->
                currentDisplayedDate.set(year, month, dayOfMonth)
                displayDate()
            },
            currentDisplayedDate[Calendar.YEAR],
            currentDisplayedDate[Calendar.MONTH],
            currentDisplayedDate[Calendar.DAY_OF_MONTH]
        ).show()
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1
        private const val TASKS_FILE_NAME = "tasks.json"
    }
}
