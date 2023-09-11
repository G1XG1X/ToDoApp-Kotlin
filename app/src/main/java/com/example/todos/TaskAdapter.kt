package com.example.todos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class TaskAdapter(
    private val mContext: Context,
    private val tasks: MutableList<Task?>,
    private val mainActivity: MainActivity
) : ArrayAdapter<Task>(mContext, 0, tasks) {

    private class ViewHolder(
        val taskCheckBox: CheckBox,
        val taskNameTextView: TextView,
        val taskIconImageView: ImageView,
        val taskDateTextView: TextView,
        val taskTimeTextView: TextView,
        val taskNotesTextView: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentTask = getItem(position)
        val viewHolder: ViewHolder
        val view: View
        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            view = inflater.inflate(R.layout.task_item, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.taskCheckBox),
                view.findViewById(R.id.taskNameTextView),
                view.findViewById(R.id.taskIconImageView),
                view.findViewById(R.id.taskDateTextView),
                view.findViewById(R.id.taskTimeTextView),
                view.findViewById(R.id.taskNotesTextView)
            )
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }
        currentTask?.let {
            with(viewHolder) {
                taskNameTextView.text = it.title
                taskCheckBox.isChecked = it.isCompleted

                if (it.iconResId != -1) {
                    taskIconImageView.setImageResource(it.iconResId)
                    taskIconImageView.visibility = View.VISIBLE
                } else {
                    taskIconImageView.visibility = View.GONE
                }
                taskDateTextView.text = it.date
                taskTimeTextView.text = it.time
                taskNotesTextView.text = it.notes
                taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        tasks.remove(it) // Usuń zadanie z listy
                        notifyDataSetChanged() // Informuj adapter o zmianie danych
                        mainActivity.saveTasksToFile()
                    }
                }
            }
        }
        return view
    }
}
