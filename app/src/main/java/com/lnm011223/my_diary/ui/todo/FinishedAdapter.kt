package com.lnm011223.my_diary.ui.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
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
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.util.BaseUtil

/**

 * @Author liangnuoming
 * @Date 2022/6/24-11:25 上午

 */
class FinishedAdapter(val FinishedList: List<Todo>, val activity: Activity) :
    RecyclerView.Adapter<FinishedAdapter.ViewHolder>() {
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    val db = dbHelper.writableDatabase
    private var date = BaseUtil.second2Date(System.currentTimeMillis())
    private var year = date.substring(0..3)
    private var month = date.substring(5..6)
    private var day = date.substring(8..9)

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

    interface ItemListenter {
        fun deleteItemLongClick(position: Int)
        fun reviseItemClick(position: Int)
        fun topItemClick(position: Int)
        fun noTopItemClick(position: Int)

        fun unfinishItemClick(position: Int)
    }

    private var itemListenter: FinishedAdapter.ItemListenter? = null
    fun setOnItemClickListener(itemListenter: FinishedAdapter.ItemListenter?) {
        this.itemListenter = itemListenter
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
                    val todoIsTop_value = contentValuesOf("isDone" to 0, "enddate" to "0")
                    db.update(
                        "tododata",
                        todoIsTop_value,
                        "id = ?",
                        arrayOf(finished.id.toString())
                    )
                    itemListenter?.unfinishItemClick(position)
                }
            }
        }
        viewHolder.isTopButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val finished = FinishedList[position]
            val scaleAnimation = ScaleAnimation(
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
            when (finished.isTop) {
                0 -> {
                    finished.isTop = 1
                    viewHolder.isTopButton.startAnimation(animationSet)
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                    viewHolder.isTopButton.imageTintList = activity.getColorStateList(R.color.green)
                }
                1 -> {
                    finished.isTop = 0
                    viewHolder.isTopButton.startAnimation(animationSet)
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
                    viewHolder.isTopButton.imageTintList =
                        activity.getColorStateList(R.color.selector_color)
                }
            }
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finished = FinishedList[position]
        val itemyear = finished.deadline.substring(0..3)
        val itemmonth = finished.deadline.substring(4..5)
        val itemday = finished.deadline.substring(6..7)
        val itemhour = finished.deadline.substring(8..9)
        val itemmin = finished.deadline.substring(10..11)
//        holder.startText.text = finished.startDate
        holder.endText.text = "完成时间：$itemyear-$itemmonth-$itemday $itemhour:$itemmin"
        holder.todoText.text = finished.todoText
        holder.todoText.paintFlags = (holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        holder.classificationText.text = finished.classification
        holder.isDoneChecked.isChecked = true
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
        val deadlinenum = finished.deadline.filter { it.isDigit() }.toBigInteger()
        val enddatenum = finished.endDate.filter { it.isDigit() }.toBigInteger()

        if (enddatenum - deadlinenum > "0".toBigInteger()) {
            holder.endText.setTextColor(activity.getColorStateList(R.color.red))
        } else {
            holder.endText.setTextColor(activity.getColorStateList(R.color.main))
        }


    }

    override fun getItemCount() = FinishedList.size
}