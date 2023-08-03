package com.lnm011223.my_diary.ui.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
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
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.ui.dashboard.DiaryAdapter
import com.lnm011223.my_diary.ui.revise.ReviseDiaryActivity
import com.lnm011223.my_diary.ui.revise.ReviseTodoActivity
import com.lnm011223.my_diary.util.BaseUtil
import java.math.BigInteger


/**

 * @Author liangnuoming
 * @Date 2022/6/24-11:25 上午

 */
class UnFinishedAdapter(val unFinishedList: List<Todo>, val activity: Activity) :
    RecyclerView.Adapter<UnFinishedAdapter.ViewHolder>() {
    private var date = BaseUtil.second2Date(System.currentTimeMillis())
    private var year = date.substring(0..3)
    private var month = date.substring(4..5)
    private var day = date.substring(6..7)
    private var hour = date.substring(8..9)
    private var min = date.substring(10..11)
    private var dateStr = year
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    val db = dbHelper.writableDatabase

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var isDoneChecked: CheckBox = view.findViewById(R.id.isDoneChecked)
        val isTopButton: ImageButton = view.findViewById(R.id.isTopButton)
        val todoText: TextView = view.findViewById(R.id.todoText)
        val deadLineText: TextView = view.findViewById(R.id.deadLineText)
        val classificationText: TextView = view.findViewById(R.id.classificationText)
        val splitText: TextView = view.findViewById(R.id.splitText)
        val todoCard: View = view.findViewById(R.id.todoCard)
        val todoNotify: View = view.findViewById(R.id.todoNotifyView)

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
            val intent = Intent(parent.context, ReviseTodoActivity::class.java)
            intent.apply {
                putExtra("id", unFinished.id)
                putExtra("todo", unFinished)
                putExtra("position", position.toString())


            }
            activity.startActivityForResult(
                intent, 4, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    viewHolder.todoCard, "todocard"
                ).toBundle()
            )
            Log.d("todo",unFinished.toString())

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
                    unFinished.endDate = BaseUtil.second2Date(System.currentTimeMillis())
                    val todoIsTop_value = contentValuesOf(
                        "isDone" to 1,
                        "enddate" to unFinished.endDate
                    )
                    db.update(
                        "tododata",
                        todoIsTop_value,
                        "id = ?",
                        arrayOf(unFinished.id.toString())
                    )
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
            val  todoIsTop_value = contentValuesOf("isTop" to unFinished.isTop)
            db.update("tododata", todoIsTop_value, "id = ?", arrayOf(unFinished.id.toString()))

        }
        return viewHolder
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unFinished = unFinishedList[position]
        val itemyear = unFinished.deadline.substring(0..3)
        val itemmonth = unFinished.deadline.substring(4..5)
        val itemday = unFinished.deadline.substring(6..7)
        val itemhour = unFinished.deadline.substring(8..9)
        val itemmin = unFinished.deadline.substring(10..11)
        holder.deadLineText.text = if (itemyear == year
            && itemmonth == month
        ) {
            when (itemday.toInt() - day.toInt()) {
                -1 -> "昨天 $itemhour:$itemmin"
                0 -> "今天 $itemhour:$itemmin"
                1 -> "明天 $itemhour:$itemmin"
                2 -> "后天 $itemhour:$itemmin"
                else -> "$itemmonth-$itemday $itemhour:$itemmin"
            }
        } else (if (itemyear == year) {
            "$itemmonth-$itemday $itemhour:$itemmin"
        } else {
            "$itemyear-$itemmonth-$itemday $itemhour:$itemmin"
        })

        if (date.filter { it.isDigit() }
                .toBigInteger() - unFinished.deadline.filter { it.isDigit() }
                .toBigInteger() > "0".toBigInteger()) {
            holder.deadLineText.setTextColor(activity.getColorStateList(R.color.hpcolorred))
            holder.todoNotify.setBackgroundColor(Color.parseColor("#F44336"))
        }else{
            holder.deadLineText.setTextColor(activity.getColorStateList(R.color.hpcolorblue))
            holder.todoNotify.setBackgroundColor(Color.parseColor("#3EB06A"))
        }
        holder.todoText.text = unFinished.todoText
        holder.classificationText.text = unFinished.classification
        holder.isDoneChecked.isChecked = false
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

