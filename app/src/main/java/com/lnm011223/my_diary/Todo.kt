package com.lnm011223.my_diary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**

 * @Author liangnuoming
 * @Date 2022/6/24-12:13 下午

 */

@Parcelize
class Todo(
    val id: Int,
    val todoText: String,
    val classification: String,
    val startDate: String,
    val endDate: String,
    val deadline: String,
    var isTop: Int,
    var isDone: Int
) : Parcelable