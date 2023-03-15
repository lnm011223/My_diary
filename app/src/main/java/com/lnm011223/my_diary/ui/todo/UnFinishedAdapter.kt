package com.lnm011223.my_diary.ui.todo

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.logic.model.Todo


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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.unfinished_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]

        }
        viewHolder.isDoneChecked.setOnCheckedChangeListener { buttonView, isChecked ->
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            when (isChecked) {
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

            val scaleAnimation= ScaleAnimation(
                0.5f, 1.2f, 0.5f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            //scaleAnimation.duration = 300
            val alphaAnimation = AlphaAnimation(0.0f, 1.0f)

            val animationSet = AnimationSet(true)
            animationSet.apply {
                addAnimation(scaleAnimation)
                addAnimation(alphaAnimation)
                duration = 300
            }

            when (unFinished.isTop) {
                0 -> {

                    unFinished.isTop = 1

                    viewHolder.isTopButton.startAnimation(animationSet)
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                    viewHolder.isTopButton.imageTintList = activity.getColorStateList(R.color.green)
                }
                1 -> {
                    unFinished.isTop = 0
                    viewHolder.isTopButton.startAnimation(animationSet)
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
        val unFinished = unFinishedList[position]
        holder.deadLineText.text = unFinished.deadline
        holder.todoText.text = unFinished.todoText
        holder.classificationText.text = unFinished.classification

        when (unFinished.isTop) {
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

        if (unFinished.classification == "" || unFinished.deadline == "") {
            holder.splitText.text = ""
        }

        if (unFinished.classification == "" && unFinished.deadline == "") {
            holder.apply {
                splitText.visibility = View.GONE
                classificationText.visibility = View.GONE
                deadLineText.visibility = View.GONE
            }
        }


    }

    override fun getItemCount() = unFinishedList.size
}