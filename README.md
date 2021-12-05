



# 			日记本的设计与实现说明文档 











## 一、软件名称

​	简易日记本

> GitHub项目地址：https://github.com/lnm011223/My_diary/



## 二、软件内容简介

一个比较美观的日记本，有简单的记录，修改，删除，查看日记的功能，对应了sqlite的增删查改（本来想用room的，不过写完了发现有问题，懒得debug了，还是sqlite顺手）。

这次写的还是比较痛苦的，recycleview和各种不熟悉的东西加起来，布局也弄了个比较复杂的布局，一步步调整到现在的样子，这次的配色和布局参考了我在iOS上用的一个日记软件。

有一些有趣的小功能啦，比如五个emoji图标，点按后才会变亮

![](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211205210854751.png)

## 三、界面设计

#### 1.日记列表

> <img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211205233951208.png" alt="" style="zoom: 25%;" />

#### 2.新建日记

> <img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211205234105432.png" alt="" style="zoom:25%;" />

#### 3.修改日历

> <img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211205234142498.png" alt="" style="zoom:25%;" />

4.删除日历

> <img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211205234217125.png" alt="" style="zoom:25%;" />

## 四、关键代码

### 1.Activity逻辑

> 1.MainActivity.kt

```kotlin
package com.lnm011223.my_diary

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lnm011223.my_diary.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var image_uri = ""
    var mood_id:Int = R.drawable.mood_1
    var datetext:String = ""
    var diarytext:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        dbHelper.writableDatabase

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(
        //    setOf(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        //    )
        //)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = Color.parseColor("#F3F3EC")
        window.navigationBarColor = Color.parseColor("#ffffff")
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        binding.fab.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivityForResult(intent,1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                image_uri = data?.getStringExtra("image_uri").toString()
                mood_id = data?.getIntExtra("mood_id",R.drawable.mood_1)!!
                datetext = data.getStringExtra("date_text").toString()
                diarytext = data.getStringExtra("diary_text1").toString()

            }
        }
    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}
```

> 2.AddActivity.kt

```kotlin
package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lnm011223.my_diary.databinding.ActivityAddBinding

import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    var mood_flag:Int = R.drawable.mood_1
    var uri1: String = ""
    @SuppressLint("SimpleDateFormat")
    val formAlbum = 2

    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = Color.parseColor("#F3F3EC")
        window.navigationBarColor = Color.parseColor("#F3F3EC")
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        dbHelper.writableDatabase

        binding.imageShow.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,formAlbum)
        }
        binding.mood1Image.setOnClickListener {
            if (flag1 == false) {
                binding.mood1Image.setImageResource(R.drawable.mood_1)
                flag1 = true
                changeOther(binding.mood1Image,flag1)
                mood_flag = R.drawable.mood_1
            }else{
                binding.mood1Image.setImageResource(R.drawable.mood_1_last)
                flag1 = false
            }
        }
        binding.mood2Image.setOnClickListener {
            if (flag2 == false) {
                binding.mood2Image.setImageResource(R.drawable.mood_2)
                flag2 = true
                changeOther(binding.mood2Image,flag2)
                mood_flag = R.drawable.mood_2
            }else{
                binding.mood2Image.setImageResource(R.drawable.mood_2_last)
                flag2 = false
            }
        }
        binding.mood3Image.setOnClickListener {
            if (flag3 == false) {
                binding.mood3Image.setImageResource(R.drawable.mood_3)
                flag3 = true
                changeOther(binding.mood3Image,flag3)
                mood_flag = R.drawable.mood_3
            }else{
                binding.mood3Image.setImageResource(R.drawable.mood_3_last)
                flag3 = false
            }
        }
        binding.mood4Image.setOnClickListener {
            if (flag4 == false) {
                binding.mood4Image.setImageResource(R.drawable.mood_4)
                flag4 = true
                changeOther(binding.mood4Image,flag4)
                mood_flag = R.drawable.mood_4
            }else{
                binding.mood4Image.setImageResource(R.drawable.mood_4_last)
                flag4 = false
            }
        }
        binding.mood5Image.setOnClickListener {
            if (flag5 == false) {
                binding.mood5Image.setImageResource(R.drawable.mood_5)
                flag5 = true
                changeOther(binding.mood5Image,flag5)
                mood_flag = R.drawable.mood_5
            }else{
                binding.mood5Image.setImageResource(R.drawable.mood_5_last)
                flag5 = false
            }
        }
        binding.completeButton.setOnClickListener {

            val diary_text = binding.diarytextEdit.text.toString()


            val intent = Intent()
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("imageuri",uri1)
                put("moodid",mood_flag)
                put("datetext",SimpleDateFormat("MM 月 dd 日 E").format(Date()))
                put("diarytext",diary_text)
            }
            db.insert("diarydata",null,diary_value)

            intent.apply {
                putExtra("image_uri",uri1)
                putExtra("mood_id",mood_flag)
                putExtra("date_text",SimpleDateFormat("dd E").format(Date()))
                putExtra("diary_text1",diary_text)

            }

            setResult(RESULT_OK,intent)
            finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            formAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        uri1 = uri.toString()
                        val bitmap = getBitmapFromuri(uri)
                        binding.imageShow.setImageURI(uri)
                        binding.imageShow.setPadding(0,0,0,0)


                    }
                }
            }
        }
    }
    private fun getBitmapFromuri(uri: Uri) = contentResolver.openFileDescriptor(uri,"r")?.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
    private fun changeOther(imageView: ImageView,flag:Boolean){

        if (flag==true){
            when (imageView) {
                binding.mood1Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood2Image -> {
                    binding.apply {
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag1 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood3Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag1 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood4Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag1 = false
                    flag5 = false

                }
                binding.mood5Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag1 = false

                }
            }
        }
    }
}
```

> 3.ReviseActivity.kt

```kotlin
package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import com.lnm011223.my_diary.databinding.ActivityReviseBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviseBinding
    var mood_flag:Int = R.drawable.mood_1
    var uri1: String = ""
    @SuppressLint("SimpleDateFormat")
    val formAlbum = 2

    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        val db = dbHelper.writableDatabase
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = Color.parseColor("#F3F3EC")
        window.navigationBarColor = Color.parseColor("#F3F3EC")
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){


            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true



            }

        }
        val datetext = intent.getStringExtra("datetext")
        var diarytext = intent.getStringExtra("diarytext")
        var imageuri = intent.getStringExtra("imageuri")?.toUri()
        //val moodid = intent.getIntExtra("moodid",R.drawable.mood_1)
        binding.dateText.text = datetext
        binding.diarytextEdit.setText(diarytext)
        binding.imageShow.setImageURI(imageuri)
        val diarytext_backup = intent.getStringExtra("diarytext")

        binding.completeButton.setOnClickListener {
            diarytext = binding.diarytextEdit.text.toString()

            val diary_value = ContentValues().apply {
                put("imageuri",uri1)
                put("moodid",mood_flag)

                put("diarytext",diarytext)
            }
            db.update("diarydata",diary_value,"diarytext = ?", arrayOf(diarytext_backup))
            finish()
        }
        binding.imageShow.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,formAlbum)
        }
        binding.mood1Image.setOnClickListener {
            if (flag1 == false) {
                binding.mood1Image.setImageResource(R.drawable.mood_1)
                flag1 = true
                changeOther(binding.mood1Image,flag1)
                mood_flag = R.drawable.mood_1
            }else{
                binding.mood1Image.setImageResource(R.drawable.mood_1_last)
                flag1 = false
            }
        }
        binding.mood2Image.setOnClickListener {
            if (flag2 == false) {
                binding.mood2Image.setImageResource(R.drawable.mood_2)
                flag2 = true
                changeOther(binding.mood2Image,flag2)
                mood_flag = R.drawable.mood_2
            }else{
                binding.mood2Image.setImageResource(R.drawable.mood_2_last)
                flag2 = false
            }
        }
        binding.mood3Image.setOnClickListener {
            if (flag3 == false) {
                binding.mood3Image.setImageResource(R.drawable.mood_3)
                flag3 = true
                changeOther(binding.mood3Image,flag3)
                mood_flag = R.drawable.mood_3
            }else{
                binding.mood3Image.setImageResource(R.drawable.mood_3_last)
                flag3 = false
            }
        }
        binding.mood4Image.setOnClickListener {
            if (flag4 == false) {
                binding.mood4Image.setImageResource(R.drawable.mood_4)
                flag4 = true
                changeOther(binding.mood4Image,flag4)
                mood_flag = R.drawable.mood_4
            }else{
                binding.mood4Image.setImageResource(R.drawable.mood_4_last)
                flag4 = false
            }
        }
        binding.mood5Image.setOnClickListener {
            if (flag5 == false) {
                binding.mood5Image.setImageResource(R.drawable.mood_5)
                flag5 = true
                changeOther(binding.mood5Image,flag5)
                mood_flag = R.drawable.mood_5
            }else{
                binding.mood5Image.setImageResource(R.drawable.mood_5_last)
                flag5 = false
            }
        }

    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
    private fun changeOther(imageView: ImageView, flag:Boolean){

        if (flag==true){
            when (imageView) {
                binding.mood1Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood2Image -> {
                    binding.apply {
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag1 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood3Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag1 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood4Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag1 = false
                    flag5 = false

                }
                binding.mood5Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag1 = false

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            formAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        uri1 = uri.toString()

                        binding.imageShow.setImageURI(uri)
                        binding.imageShow.setPadding(0,0,0,0)


                    }
                }
            }
        }
    }
}
```

> 4.DashboardFragment.kt

```kotlin
package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.*
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import kotlin.concurrent.thread


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val diaryList = ArrayList<Diary>()
    val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        initDiary()
        val layoutManager = LinearLayoutManager(context)
        binding.diaryRecycle.layoutManager = layoutManager
        val adapter = DiaryAdapter(diaryList)
        binding.diaryRecycle.adapter = adapter

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }
    @SuppressLint("Range")
    private fun initDiary(){
        thread {
            diaryList.clear()
            val db = dbHelper.writableDatabase
            val cursor = db.query("diarydata",null,null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val imageuri = cursor.getString(cursor.getColumnIndex("imageuri"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    diaryList.add(Diary(datetext,moodid,imageuri.toUri(),diarytext))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
    }





}
```

### 2.Recycleview相关

> 1.Diary.kt

```kotlin
package com.lnm011223.my_diary

import android.net.Uri

class Diary(val date_text:String, val moon:Int, val diary_image:Uri, val diary_text: String)
```

> 2.DiaryAdapter.kt

```kotlin
package com.lnm011223.my_diary

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap




class DiaryAdapter(val diaryList: List<Diary>) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diarycard_mood : ImageView = view.findViewById(R.id.diarycard_mood)
        val diarycard_image : ImageView = view.findViewById(R.id.diarycard_image)
        val diarycard_date : TextView = view.findViewById(R.id.diarycard_date)
        val diarycard_text : TextView = view.findViewById(R.id.diarycard_text)
        val delete_button : ImageView = view.findViewById(R.id.delete_image)
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
                    db.delete("diarydata", "diarytext = ?", arrayOf(diary.diary_text))


                }


                show()
            }

        }
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val diary = diaryList[position]

            val intent = Intent(parent.context,ReviseActivity::class.java)
            intent.apply {
                putExtra("datetext",diary.date_text)
                putExtra("diarytext",diary.diary_text)
                putExtra("imageuri",diary.diary_image.toString())
                putExtra("moonid",diary.moon)

            }
            parent.context.startActivity(intent)

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaryList[position]
        holder.diarycard_mood.setImageResource(diary.moon)
        holder.diarycard_date.text = diary.date_text.substring(5,7)+" "+diary.date_text.substring(10,12)
        holder.diarycard_text.text = diary.diary_text


        holder.diarycard_image.setImageURI(diary.diary_image)
    }

}
```

> 3.diary_card.xml        //recycleview的核心

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_marginVertical="5dp"
    >

    <com.google.android.material.card.MaterialCardView

        android:layout_width="match_parent"
        android:layout_height="264dp"
        android:layout_marginHorizontal="15dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        >

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_margin="15dp">

            <ImageView
                android:id="@+id/diarycard_mood"
                android:layout_width="50dp"
                android:layout_height="57dp"
                android:layout_marginStart="5dp"
                android:src="@mipmap/ic_launcher"
                app:layout_constraintBottom_toTopOf="@+id/materialCardView"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.0" />

            <com.google.android.material.card.MaterialCardView
                android:id="@+id/materialCardView"
                android:layout_width="63dp"
                android:layout_height="30dp"

                android:layout_marginBottom="140dp"
                android:backgroundTint="#eeeeee"
                app:cardCornerRadius="4dp"
                app:cardElevation="0dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent">

                <TextView
                    android:id="@+id/diarycard_date"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginHorizontal="6dp"
                    android:layout_marginVertical="5dp"
                    android:text="01 周五"
                    android:textColor="#707070"
                    android:textStyle="bold"/>
            </com.google.android.material.card.MaterialCardView>

            <ImageView
                android:id="@+id/delete_image"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:layout_marginStart="15dp"
                android:layout_marginTop="24dp"
                android:src="@drawable/ic_delete"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/materialCardView" />

            <ImageView
                android:id="@+id/imageView2"
                android:layout_width="3dp"
                android:layout_height="234dp"
                android:layout_marginStart="5dp"

                android:background="@drawable/button_drawable"
                android:backgroundTint="#eeeeee"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toEndOf="@+id/materialCardView"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/diarycard_text"
                android:layout_width="255dp"
                android:layout_height="40dp"
                android:maxLines="2"
                android:layout_marginStart="10dp"
                android:text="dddd"
                android:textColor="#707070"
                app:layout_constraintStart_toEndOf="@+id/imageView2"
                tools:layout_editor_absoluteY="0dp" />

            <com.google.android.material.card.MaterialCardView
                android:layout_width="255dp"
                android:layout_height="190dp"
                android:layout_marginStart="6dp"
                android:layout_marginTop="10dp"
                android:backgroundTint="#eeeeee"
                android:scaleType="center"
                app:cardCornerRadius="10dp"
                app:cardElevation="0dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toEndOf="@+id/imageView2"
                app:layout_constraintTop_toBottomOf="@+id/diarycard_text"
                app:layout_constraintVertical_bias="1.0">

                <ImageView
                    android:id="@+id/diarycard_image"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:scaleType="center" />
            </com.google.android.material.card.MaterialCardView>

        </androidx.constraintlayout.widget.ConstraintLayout>


    </com.google.android.material.card.MaterialCardView>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 3.Sqlite相关

> MyDatabaseHelper.kt

```kotlin
package com.lnm011223.my_diary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int): SQLiteOpenHelper(context,name,null,version) {

    private val account_creat = "create table diarydata (" +
            "id integer primary key autoincrement," +
            "datetext text," +
            "moodid integer," +
            "imageuri text," +
            "diarytext text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(account_creat)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
```

### 4.布局文件

> 1.activity_main.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F3F3EC"

    >

    <fragment
        android:id="@+id/nav_host_fragment_activity_main"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toTopOf="@+id/bottomAppBar"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/mobile_navigation"
        tools:layout_editor_absoluteX="0dp"
        android:layout_marginBottom="56dp"/>

    <com.google.android.material.bottomappbar.BottomAppBar
        android:id="@+id/bottomAppBar"

        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        app:backgroundTint="#ffffff"

        app:contentInsetEnd="0dp"
        app:contentInsetStart="0dp"
        app:elevation="0dp"
        app:fabAlignmentMode="end"
        app:layout_constraintBottom_toBottomOf="parent"
        >

        <com.google.android.material.bottomnavigation.BottomNavigationView
            android:layout_gravity="bottom"
            app:labelVisibilityMode="selected"
            android:id="@+id/nav_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="0dp"
            android:layout_marginEnd="100dp"
            android:elevation="0dp"
            app:elevation="0dp"
            app:backgroundTint="@android:color/transparent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:menu="@menu/bottom_nav_menu"
            app:itemIconTint="@color/selector_color"
            app:itemTextColor="@color/selector_color"
            />
    </com.google.android.material.bottomappbar.BottomAppBar>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:backgroundTint="#B8D68A"
        app:borderWidth="0dp"
        android:src="@drawable/ic_baseline_add_24"

        app:tint="#000000"

        app:elevation="0dp"
        app:layout_anchor="@id/bottomAppBar" />
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

> 2.activity_add.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F3F3EC"

    android:fitsSystemWindows="true"
    tools:context=".AddActivity">

    <TextView
        android:id="@+id/date_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="#707070"
        android:textSize="20sp"
        app:layout_constraintBottom_toTopOf="@+id/card_mood"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card_mood"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/photo_card"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_margin="10dp">

            <TextView
                android:id="@+id/textView"
                android:layout_width="wrap_content"

                android:layout_height="wrap_content"
                android:text="今天的心情"
                android:textColor="#707070"
                android:textSize="16sp"
                android:textStyle="bold"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageView
                android:layout_marginTop="10dp"
                android:id="@+id/mood1_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:backgroundTint="#00000000"
                android:src="@drawable/mood_1_last"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0" />

            <ImageView
                android:id="@+id/mood2_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:layout_marginStart="15dp"
                android:layout_marginEnd="15dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_2_last"
                app:layout_constraintStart_toEndOf="@+id/mood1_image"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>


            <ImageView
                android:id="@+id/mood3_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginHorizontal="20dp"
                android:layout_marginStart="20dp"
                android:layout_marginEnd="20dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_3_last"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="@+id/mood4_image"
                app:layout_constraintHorizontal_bias="0.512"
                app:layout_constraintStart_toEndOf="@+id/mood2_image"
                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0" />

            <ImageView
                android:id="@+id/mood4_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:layout_marginEnd="15dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_4_last"
                app:layout_constraintEnd_toStartOf="@+id/mood5_image"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>

            <ImageView
                android:id="@+id/mood5_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:backgroundTint="#00000000"
                android:src="@drawable/mood_5_last"
                app:layout_constraintEnd_toEndOf="parent"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>
        </androidx.constraintlayout.widget.ConstraintLayout>




    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/photo_card"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/card_text"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginHorizontal="10dp"
                android:layout_marginTop="10dp"
                android:text="今天的照片"
                android:textColor="#707070"
                android:textSize="16sp"
                android:textStyle="bold"

                />
            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:backgroundTint="#eeeeee"
                android:layout_marginHorizontal="10dp"
                android:layout_marginTop="7dp"
                android:layout_marginBottom="15dp"
                app:cardElevation="0dp"
                app:cardCornerRadius="10dp">
                <ImageView
                    android:id="@+id/image_show"
                    android:layout_width="match_parent"
                    android:layout_height="300dp"
                    android:scaleType="center"


                    android:src="@drawable/ic_baseline_camera_alt_24"

                    android:padding="65dp"


                    />
            </com.google.android.material.card.MaterialCardView>


        </LinearLayout>


    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card_text"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/complete_Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="10dp"
            android:layout_marginTop="10dp"
            android:text="一行日记"
            android:textColor="#707070"
            android:textSize="16sp"
            android:textStyle="bold" />

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/diarytext_edit"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:layout_marginHorizontal="10dp"
            android:layout_marginBottom="15dp"
            android:background="@drawable/button_drawable"
            android:backgroundTint="#eeeeee"
            android:maxLines="2"
            android:hint="请输入内容"
            android:paddingStart="10dp" />
    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/complete_Button"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:backgroundTint="#3EB06A"
        android:insetTop="0dp"
        android:insetBottom="0dp"
        android:text="制作完成"
        android:textSize="18dp"
        android:textStyle="bold"
        app:cornerRadius="0dp"
        app:elevation="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        tools:layout_editor_absoluteX="0dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

> 3.activity_revise.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F3F3EC"

    android:fitsSystemWindows="true"
    tools:context=".ReviseActivity">

    <TextView
        android:id="@+id/date_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="#707070"
        android:textSize="20sp"
        app:layout_constraintBottom_toTopOf="@+id/card_mood"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card_mood"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/photo_card"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_margin="10dp">

            <TextView
                android:id="@+id/textView"
                android:layout_width="wrap_content"

                android:layout_height="wrap_content"
                android:text="今天的心情"
                android:textColor="#707070"
                android:textSize="16sp"
                android:textStyle="bold"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageView
                android:layout_marginTop="10dp"
                android:id="@+id/mood1_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:backgroundTint="#00000000"
                android:src="@drawable/mood_1_last"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0" />

            <ImageView
                android:id="@+id/mood2_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:layout_marginStart="15dp"
                android:layout_marginEnd="15dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_2_last"
                app:layout_constraintStart_toEndOf="@+id/mood1_image"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>


            <ImageView
                android:id="@+id/mood3_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginHorizontal="20dp"
                android:layout_marginStart="20dp"
                android:layout_marginEnd="20dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_3_last"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="@+id/mood4_image"
                app:layout_constraintHorizontal_bias="0.512"
                app:layout_constraintStart_toEndOf="@+id/mood2_image"
                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0" />

            <ImageView
                android:id="@+id/mood4_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:layout_marginEnd="15dp"
                android:backgroundTint="#00000000"
                android:src="@drawable/mood_4_last"
                app:layout_constraintEnd_toStartOf="@+id/mood5_image"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>

            <ImageView
                android:id="@+id/mood5_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:backgroundTint="#00000000"
                android:src="@drawable/mood_5_last"
                app:layout_constraintEnd_toEndOf="parent"
                tools:layout_editor_absoluteY="37dp"
                app:layout_constraintBottom_toBottomOf="parent"

                app:layout_constraintTop_toBottomOf="@+id/textView"
                app:layout_constraintVertical_bias="1.0"/>
        </androidx.constraintlayout.widget.ConstraintLayout>




    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/photo_card"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/card_text"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginHorizontal="10dp"
                android:layout_marginTop="10dp"
                android:text="今天的照片"
                android:textColor="#707070"
                android:textSize="16sp"
                android:textStyle="bold"

                />
            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:backgroundTint="#eeeeee"
                android:layout_marginHorizontal="10dp"
                android:layout_marginTop="7dp"
                android:layout_marginBottom="15dp"
                app:cardElevation="0dp"
                app:cardCornerRadius="10dp">
                <ImageView
                    android:id="@+id/image_show"
                    android:layout_width="match_parent"
                    android:layout_height="300dp"
                    android:scaleType="center"


                    android:src="@drawable/ic_baseline_camera_alt_24"

                    android:padding="65dp"


                    />
            </com.google.android.material.card.MaterialCardView>


        </LinearLayout>


    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card_text"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:layout_marginHorizontal="15dp"
        android:layout_marginBottom="10dp"
        android:elevation="0dp"
        app:cardCornerRadius="10dp"
        app:cardElevation="0dp"
        app:layout_constraintBottom_toTopOf="@+id/complete_Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="10dp"
            android:layout_marginTop="10dp"
            android:text="一行日记"
            android:textColor="#707070"
            android:textSize="16sp"
            android:textStyle="bold" />

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/diarytext_edit"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:layout_marginHorizontal="10dp"
            android:layout_marginBottom="15dp"
            android:background="@drawable/button_drawable"
            android:backgroundTint="#eeeeee"
            android:maxLines="2"
            android:hint="请输入内容"
            android:paddingStart="10dp" />
    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/complete_Button"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:backgroundTint="#3EB06A"
        android:insetTop="0dp"
        android:insetBottom="0dp"
        android:text="修改完成"
        android:textSize="18dp"
        android:textStyle="bold"
        app:cornerRadius="0dp"
        app:elevation="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        tools:layout_editor_absoluteX="0dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
```





## 五、软件操作流程

![](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211206000220688.png)

## 六、难点和解决方案

对于我个人而言本次的难点主要是布局的问题以及对recycleview的不熟悉，和一些奇奇怪怪的bug，导致体验极差，现在项目里也遗留了一些垃圾代码

#### 1.日记列表刷新问题

解决方案：利用Fragment的生命周期，把加载Recycleview放在`onstart( )`方法里，每当fragment变为可见状态，便会执行一次加载（即创建完新日记和修改完日记回到这个列表fragment就会加载出来）

#### 2.context获取问题

解决方案：有的时候this用作context会翻车，所以可以弄个全局的context

```kotlin
package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
```

#### 3.recycleview点击问题

解决方案：一定要记得context的参数是`parent.context`,不然会直接闪退



## 七、不足之处

虽然目前程序跑的效果还是不错的，但是也只是在数据量少的时候，因为这个列表加载是一次加载全部，所以数据多的时候，肯定会导致卡顿以及难找到想要的记录。而且现在日记能记录的东西也只有时间，文字，图片以及心情，内容比较少。



## 八、今后设想

1.可以加入按年份月份分类的功能，就不用一次性加载全部记录

2.可以加入定时提醒功能，每天自定义时间提醒用户该写日记了

3.加入按具体日期查看日记的功能

