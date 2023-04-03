package com.lnm011223.my_diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.logic.model.Todo

class MainViewModel() : ViewModel() {
    val diaryList: LiveData<ArrayList<Diary>>
        get() = _diaryList

    private var _diaryList = MutableLiveData<ArrayList<Diary>>(arrayListOf())


    val finishedList: LiveData<ArrayList<Todo>>
        get() = _finishedList

    private var _finishedList = MutableLiveData<ArrayList<Todo>>(arrayListOf())


    val unfinishedList: LiveData<ArrayList<Todo>>
        get() = _unfinishedList

    private var _unfinishedList = MutableLiveData<ArrayList<Todo>>(arrayListOf())
    init {

    }

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
    var addTodoPosition = MutableLiveData<Int>(-1)

    var addunfinishedItem = Todo(-1,"","","","","",0,0)



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

    fun setAllUnfinished(list: ArrayList<Todo>) {
        _unfinishedList.value?.clear()
        _unfinishedList.value?.addAll(list)
    }

    fun setAllfinished(list: ArrayList<Todo>) {
        _finishedList.value?.clear()
        _finishedList.value?.addAll(list)
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


    fun addunfinishedTodo(todo: Todo){
        _unfinishedList.value?.add(todo)
        addTodoPosition.value = -1

    }


}