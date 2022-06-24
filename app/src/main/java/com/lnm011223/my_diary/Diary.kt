package com.lnm011223.my_diary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Diary(
    val id: Int,
    val date_text: String,
    val moon: Int,
    val diary_image: String,
    val diary_text: String
) : Parcelable