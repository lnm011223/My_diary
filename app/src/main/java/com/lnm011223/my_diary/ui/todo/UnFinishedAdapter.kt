package com.lnm011223.my_diary.ui.todo

import android.app.Activity
import android.app.AlertDialog
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
import com.lnm011223.my_diary.ui.dashboard.DiaryAdapter
import com.lnm011223.my_diary.util.BaseUtil


/**

 * @Author liangnuoming
 * @Date 2022/6/24-11:25 上午

 */
class UnFinishedAdapter(val unFinishedList: List<Todo>, val activity: Activity) :
    RecyclerView.Adapter<UnFinishedAdapter.ViewHolder>() {
    private var date = BaseUtil.second2Date(System.currentTimeMillis())
    private var year = date.substring(0..3)
    private var month = date.substring(5..6)
    private var day = date.substring(8..9)
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    val db = dbHelper.writableDatabase
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val isDoneChecked: CheckBox = view.findViewById(R.id.isDoneChecked)
        val isTopButton: ImageButton = view.findViewById(R.id.isTopButton)
        val todoText: TextView = view.findViewById(R.id.todoText)
        val deadLineText: TextView = view.findViewById(R.id.deadLineText)
        val classificationText: TextView = view.findViewById(R.id.classificationText)
        val splitText: TextView = view.findViewById(R.id.splitText)

    }


    interface ItemListenter {
        fun deleteItemLongClick(position: Int)
        fun reviseItemClick(position: Int)
        fun topItemClick(position: Int)
        fun noTopItemClick(position: Int)

        fun finishItemClick(position: Int)
    }

    private var itemListenter: UnFinishedAdapter.ItemListenter? = null
    fun setOnItemClickListener(itemListenter: UnFinishedAdapter.ItemListenter?) {
        this.itemListenter = itemListenter
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.unfinished_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {

            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            itemListenter?.reviseItemClick(position)


        }
        viewHolder.itemView.setOnLongClickListener {

            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            AlertDialog.Builder(parent.context).apply {
                setTitle("确认：")
                setMessage("真的要删除这条记录吗？")
                setNegativeButton("否") { _, _ ->


                }
                setPositiveButton("是") { _, _ ->
                    db.delete("tododata", "id = ?", arrayOf(unFinished.id.toString()))
                    itemListenter?.deleteItemLongClick(position)
                }


                show()
            }
            true
        }
        viewHolder.isDoneChecked.setOnCheckedChangeListener { buttonView, isChecked ->
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]
            when (isChecked) {
                true -> {
                    unFinished.isDone = 1

                    itemListenter?.finishItemClick(position)
                }
                false -> {

                }
            }
        }
        viewHolder.isTopButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            val unFinished = unFinishedList[position]

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

            when (unFinished.isTop) {
                0 -> {

                    unFinished.isTop = 1

                    viewHolder.isTopButton.startAnimation(animationSet)
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_baseline_grade_24)
                    viewHolder.isTopButton.imageTintList = activity.getColorStateList(R.color.green)
                    itemListenter?.topItemClick(position)

                }
                1 -> {
                    unFinished.isTop = 0
                    viewHolder.isTopButton.startAnimation(animationSet)
                    viewHolder.isTopButton.setImageResource(R.drawable.ic_twotone_grade_24)
                    viewHolder.isTopButton.imageTintList =
                        activity.getColorStateList(R.color.selector_color)
                    itemListenter?.noTopItemClick(position)
                }
            }
            val todoIsTop_value = contentValuesOf("isTop" to unFinished.isTop)
            db.update("tododata",todoIsTop_value,"id = ?", arrayOf(unFinished.id.toString()))

        }
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unFinished = unFinishedList[position]

        holder.deadLineText.text = if (unFinished.deadline.substring(0..3) == year
            && unFinished.deadline.substring(5..6) == month
        ) {
            when (unFinished.deadline.substring(8..9).toInt() - day.toInt()) {
                -1 -> "昨天"
                0 -> "今天"
                1 -> "明天"
                2 -> "后天"
                else -> unFinished.deadline.substring(5..9)
            }
        } else (if (unFinished.deadline.substring(0..3) == year) {
            unFinished.deadline.substring(5..9)
        } else {
            unFinished.deadline.substring(0..9)
        })
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