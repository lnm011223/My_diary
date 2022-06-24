package com.lnm011223.my_diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    val diaryList: LiveData<ArrayList<Diary>>
        get() = _diaryList

    private var _diaryList = MutableLiveData<ArrayList<Diary>>(arrayListOf())

    init {

    }
    var unFinishedList = MutableLiveData<ArrayList<Todo>>(arrayListOf())
    var selectid = R.drawable.mood_1
    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    var selectflag = false
    var reviseDiaryItem = Diary(-1, "", -1, "", "")
    var revisePosition = MutableLiveData<Int>(-1)
    var addDiaryItem = Diary(-1, "", -1, "", "")
    var addPosition = MutableLiveData<Int>(-1)
    fun setPosition(position: Int) {
        revisePosition.value = position
    }

    fun resetPostion() {
        revisePosition.value = -1
    }

    fun setAll(list: ArrayList<Diary>) {
        _diaryList.value?.clear()
        _diaryList.value?.addAll(list)

    }

    fun addDiary(diary: Diary) {
        _diaryList.value?.add(diary)
        addPosition.value = -1
    }

    fun clearAll() {
        diaryList.value?.clear()
        _diaryList.value?.clear()
    }

    fun deleteDiary(position: Int) {
        _diaryList.value?.removeAt(position)
        Log.d("livedata", "deletesucceed")
        Log.d("livedata", diaryList.value.toString())
    }

    fun changeDiary(position: Int, diary: Diary) {
        Log.d("livedateview", "${_diaryList.value?.get(position)?.diary_text}")
        _diaryList.value!![position] = diary
        Log.d("livedateview", diary.diary_text)
        Log.d("livedateview", "${_diaryList.value?.get(position)?.diary_text}")
        reviseDiaryItem = Diary(-1, "", -1, "", "")
    }
}