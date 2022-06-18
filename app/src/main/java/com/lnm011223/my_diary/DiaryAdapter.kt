package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.diary_card.view.*
import java.io.File


class DiaryAdapter(val diaryList: List<Diary>,val activity: Activity) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diarycard_mood : ImageView = view.findViewById(R.id.diarycard_mood)
        val diarycard_image : ImageView = view.findViewById(R.id.diarycard_image)
        val diarycard_date : TextView = view.findViewById(R.id.diarycard_date)
        val diarycard_text : TextView = view.findViewById(R.id.diarycard_text)
        val delete_button : ImageView = view.findViewById(R.id.delete_image)
        val diarycard_image_background : View = view.findViewById(R.id.diarycard_image_background)
        val diary_card : View = view.findViewById(R.id.diary_card)
    }

    override fun getItemCount() = diaryList.size



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_card,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.delete_button.setOnClickListener {
            val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
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
                    val intent = Intent("DiaryDataChangeReceiver")

                    context.sendBroadcast(intent)

                }


                show()
            }

        }
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val diary = diaryList[position]

            val intent = Intent(parent.context,ReviseActivity::class.java)
            intent.apply {
                putExtra("id",diary.id)
                putExtra("datetext",diary.date_text)
                putExtra("diarytext",diary.diary_text)
                putExtra("imageuri",diary.diary_image)
                putExtra("moodid",diary.moon)
                putExtra("test",diary)

            }

            parent.context.startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                viewHolder.diary_card,"sharedcard").toBundle())

        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaryList[position]
        holder.diarycard_mood.setImageResource(diary.moon)
        holder.diarycard_date.text = diary.date_text.substring(5,7)+" "+diary.date_text.substring(10,12)
        holder.diarycard_text.text = diary.diary_text
        Log.d("image-url",diary.diary_image)
        if (diary.diary_image == ""){
            Log.d("image-url","null")

            holder.diarycard_image_background.setVisibility(View.GONE)
        }

        holder.diarycard_image.setImageURI(Uri.fromFile(File(diary.diary_image)))
    }

}