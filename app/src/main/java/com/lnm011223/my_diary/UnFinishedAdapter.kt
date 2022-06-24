package com.lnm011223.my_diary

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author liangnuoming
 * @Date 2022/6/24-11:25 上午

 */
class UnFinishedAdapter(val unFinishedList: List<Todo>, val activity: Activity) :
    RecyclerView.Adapter<UnFinishedAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val isDoneChecked: CheckBox = view.findViewById(R.id.isDoneChecked)
        val isTopButton: ImageButton = view.findViewById(R.id.isTopButton)
        val todoText: TextView = view.findViewById(R.id.todoText)
        val deadLineText: TextView = view.findViewById(R.id.deadLineText)
        val classificationText: TextView = view.findViewById(R.id.classificationText)
        val splitText: TextView = view.findViewById(R.id.splitText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.unfinished_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]

        }
        viewHolder.isDoneChecked.setOnCheckedChangeListener { buttonView, isChecked ->
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            when(isChecked) {
                true -> {
                    unFinished.isDone = 1
                }
                false -> {

                }
            }
        }
        viewHolder.isTopButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            when(unFinished.isTop){
                0 -> {
                    unFinished.isTop = 1
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                }
                1 -> {
                    unFinished.isTop = 0
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
                }
            }
        }
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unFinished = unFinishedList[position]
        holder.deadLineText.text = unFinished.deadline
        holder.todoText.text = unFinished.todoText
        holder.classificationText.text = unFinished.classification

        when(unFinished.isTop) {
            1 -> holder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
            0 -> holder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
        }
        if (unFinished.classification == ""){
            holder.splitText.text = ""
        }

        if (unFinished.classification == "" && unFinished.deadline == ""){

        }


    }

    override fun getItemCount() = unFinishedList.size
}