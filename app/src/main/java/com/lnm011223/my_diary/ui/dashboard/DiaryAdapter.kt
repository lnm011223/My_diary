package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.ui.revise.ReviseDiaryActivity
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.util.BaseUtil


class DiaryAdapter(val diaryList: List<Diary>, val activity: Activity) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diarycard_mood: ImageView = view.findViewById(R.id.diarycard_mood)
        val diarycard_image: ImageView = view.findViewById(R.id.diarycard_image)
        val diarycard_date: TextView = view.findViewById(R.id.diarycard_date)
        val diarycard_text: TextView = view.findViewById(R.id.diarycard_text)
        val delete_button: ImageView = view.findViewById(R.id.delete_image)
        val diarycard_image_background: View = view.findViewById(R.id.diarycard_image_background)
        val diary_card: View = view.findViewById(R.id.diary_card)
        val edit_button: ImageView = view.findViewById(R.id.edit_image)
    }


    override fun getItemCount() = diaryList.size

    //点击事件接口
    interface ItemListenter {
        fun deleteItemClick(position: Int)
        fun reviseItemClick(position: Int)
        fun showItemImageClick(position: Int)
    }

    private var itemListenter: ItemListenter? = null
    fun setOnItemClickListener(itemListenter: ItemListenter?) {
        this.itemListenter = itemListenter
    }

    val moodMap = mapOf(
        1 to R.drawable.mood_1,
        2 to R.drawable.mood_2,
        3 to R.drawable.mood_3,
        4 to R.drawable.mood_4,
        5 to R.drawable.mood_5,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_card, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.delete_button.setOnClickListener {
            val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
            val db = dbHelper.writableDatabase
            val position = viewHolder.adapterPosition
            val diary = diaryList[position]

            AlertDialog.Builder(parent.context).apply {
                setTitle("确认：")
                setMessage("真的要删除这条记录吗？")
                setNegativeButton("否") { _, _ ->


                }
                setPositiveButton("是") { _, _ ->
                    db.delete("diarydata", "id = ?", arrayOf(diary.id.toString()))
                    itemListenter?.deleteItemClick(position)
                }


                show()
            }

        }
        viewHolder.diarycard_image.setOnClickListener {
            val position = viewHolder.adapterPosition
            val diary = diaryList[position]
            itemListenter?.showItemImageClick(position)
        }
        viewHolder.edit_button.setOnClickListener {
            val position = viewHolder.adapterPosition
            val diary = diaryList[position]
            itemListenter?.reviseItemClick(position)
            val intent = Intent(parent.context, ReviseDiaryActivity::class.java)
            intent.apply {
                putExtra("id", diary.id)
                putExtra("datetext", diary.date_text)
                putExtra("diarytext", diary.diary_text)
                putExtra("imageuri", diary.diary_image)
                putExtra("moodid", diary.moon)
                putExtra("diary", diary)
                putExtra("position", position.toString())


            }
            activity.startActivityForResult(
                intent, 2, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    viewHolder.diary_card, "sharedcard"
                ).toBundle()
            )
            Log.d("livedata", activity.toString())

        }
        viewHolder.itemView.setOnClickListener {


        }
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaryList[position]
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recyclerviewshow)
        holder.diarycard_mood.setImageResource(moodMap[diary.moon]!!)
        holder.diarycard_date.text =
            diary.date_text.substring(6..7) + " " + BaseUtil.dayOfWeek(diary.date_text)
        holder.diarycard_text.text = diary.diary_text
        Log.d("image-url", diary.diary_image)
        if (diary.diary_image == "") {
            Log.d("image-url", "null")
            holder.diarycard_image_background.visibility = View.GONE
        } else {
            holder.diarycard_image_background.visibility = View.VISIBLE
        }

        //holder.diarycard_image.setImageURI(Uri.fromFile(File(diary.diary_image)))
        holder.diarycard_image.setImageURI(diary.diary_image.toUri())
    }

}