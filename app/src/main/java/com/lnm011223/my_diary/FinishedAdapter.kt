package com.lnm011223.my_diary

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author liangnuoming
 * @Date 2022/6/24-11:25 上午

 */
class FinishedAdapter(val FinishedList: List<Todo>, val activity: Activity) :
    RecyclerView.Adapter<FinishedAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val isDoneChecked: CheckBox = view.findViewById(R.id.isDoneChecked_F)
        val isTopButton: ImageButton = view.findViewById(R.id.isTopButton_F)
        val todoText: TextView = view.findViewById(R.id.todoText_F)
        val startText: TextView = view.findViewById(R.id.startText_F)
        val endText: TextView = view.findViewById(R.id.endText_F)
        val classificationText: TextView = view.findViewById(R.id.classificationText_F)
        val splitText: TextView = view.findViewById(R.id.splitText_F)
        val splitText2: TextView = view.findViewById(R.id.splitText2_F)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.finished_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val finished = FinishedList[position]

        }
        viewHolder.isDoneChecked.setOnCheckedChangeListener { buttonView, isChecked ->
            val position = viewHolder.adapterPosition
            val finished = FinishedList[position]
            when (isChecked) {
                true -> {

                }
                false -> {
                    finished.isDone = 0
                }
            }
        }
        viewHolder.isTopButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val finished = FinishedList[position]
            when (finished.isTop) {
                0 -> {
                    finished.isTop = 1
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                    viewHolder.isTopButton.imageTintList = activity.getColorStateList(R.color.green)
                }
                1 -> {
                    finished.isTop = 0
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
                    viewHolder.isTopButton.imageTintList =
                        activity.getColorStateList(R.color.selector_color)
                }
            }
        }
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finished = FinishedList[position]
        holder.startText.text = finished.startDate
        holder.endText.text = finished.endDate
        holder.todoText.text = finished.todoText
        holder.classificationText.text = finished.classification

        when (finished.isTop) {
            1 -> {
                holder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                holder.isTopButton.imageTintList = activity.getColorStateList(R.color.green)
            }
            0 -> {
                holder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
                holder.isTopButton.imageTintList =
                    activity.getColorStateList(R.color.selector_color)
            }
        }

        if (finished.classification == "") {
            holder.apply {
                splitText2.visibility = View.GONE
                classificationText.visibility = View.GONE

            }
        }


    }

    override fun getItemCount() = FinishedList.size
}