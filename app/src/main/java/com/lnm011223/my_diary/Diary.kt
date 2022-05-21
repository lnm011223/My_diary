package com.lnm011223.my_diary

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
class Diary(val date_text:String, val moon:Int, val diary_image:String, val diary_text: String) : Parcelable