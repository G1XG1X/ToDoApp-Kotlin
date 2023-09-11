package com.example.todos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private var selectedIconResId = 0
    private var selectedDateTime: Calendar = Calendar.getInstance()
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView

    // Stworzenie tablicy z ikonami
    private lateinit var iconViews: Array<ImageView>
    private val iconResIds = intArrayOf(
        R.drawable.ic_phone, R.drawable.ic_favorite, R.drawable.ic_home,
        R.drawable.ic_person, R.drawable.ic_pets, R.drawable.ic_school,
        R.drawable.ic_work
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addtask)
        val toolbar = findViewById<Toolbar>(R.id.addTaskToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val editTextNotes = findViewById<EditText>(R.id.editTextNotes)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Inicjalizacja tablicy ikon
        iconViews = arrayOf(
            findViewById(R.id.phone),
            findViewById(R.id.favorite),
            findViewById(R.id.home),
            findViewById(R.id.person),
            findViewById(R.id.pets),
            findViewById(R.id.school),
            findViewById(R.id.work)
        )
        dateText = findViewById(R.id.dateText)
        timeText = findViewById(R.id.timeText)
        val dateClick = findViewById<LinearLayout>(R.id.dateClick)
        val timeClick = findViewById<LinearLayout>(R.id.timeClick)

        dateClick.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog =
                DatePickerDialog(this@AddTaskActivity, { _, year, month, dayOfMonth ->
                    selectedDateTime.set(year, month, dayOfMonth)
                    dateText.text = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                    dateText.visibility = View.VISIBLE
                }, year, month, day)
            datePickerDialog.show()
        }

        timeClick.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR_OF_DAY]
            val minute = c[Calendar.MINUTE]
            val timePickerDialog =
                TimePickerDialog(this@AddTaskActivity, { _, hourOfDay, minute ->
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedDateTime.set(Calendar.MINUTE, minute)
                    timeText.text = String.format("%02d:%02d", hourOfDay, minute)
                    timeText.visibility = View.VISIBLE
                }, hour, minute, true)
            timePickerDialog.show()
        }

        val iconClickListener = View.OnClickListener { v ->
            if (v.tag == "selected") {
                v.setBackgroundResource(0)
                v.tag = null
                selectedIconResId = 0
                return@OnClickListener
            }

            for (iconView in iconViews) {
                iconView.setBackgroundResource(0)
                iconView.tag = null
            }

            for (i in iconViews.indices) {
                if (v.id == iconViews[i].id) {
                    selectedIconResId = iconResIds[i]
                    iconViews[i].setBackgroundResource(R.drawable.ic_border)
                    iconViews[i].tag = "selected"
                    break
                }
            }
        }

        for (iconView in iconViews) {
            iconView.setOnClickListener(iconClickListener)
        }

        saveButton.setOnClickListener {
            val taskText = editTextTask.text.toString()
            if (taskText.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("TASK_TIME", timeText.text.toString())
                    putExtra("TASK_DATE", dateText.text.toString())
                    putExtra("TASK_NOTES", editTextNotes.text.toString())
                    putExtra("NEW_TASK", taskText)
                    putExtra("SELECTED_ICON", selectedIconResId)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this@AddTaskActivity, "Please enter a task", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
