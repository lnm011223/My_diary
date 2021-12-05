package com.lnm011223.mysimplediary

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DiaryAdapter(activity: Activity,val resourceId: Int,data: List<Diary>) : ArrayAdapter<Diary>(activity,resourceId,data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)
        val datetext: TextView = view.findViewById(R.id.item_date)
        val diarytext: TextView = view.findViewById(R.id.item_text)
        val diary = getItem(position)
        if (diary != null) {
            datetext.text = diary.date
            diarytext.text = diary.diarytext
        }
        return view
    }
}