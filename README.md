## 1.listview相关

#### 1.Diary类

```kotlin
class Diary(val date: String,val diarytext: String)
```

#### 2.DiaryAdapter.kt

```kotlin
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
```

#### 3.diary_item.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <TextView
        android:layout_marginTop="5dp"
        android:id="@+id/item_date"
        android:textColor="#2196f3"
        android:textSize="13sp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
    <TextView
        android:id="@+id/item_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:maxLines="1"/>

</LinearLayout>
```

## 2.Sqlite相关

#### 1.MyDatabaseHelper.kt

```kotlin
package com.lnm011223.mysimplediary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int): SQLiteOpenHelper(context,name,null,version) {

    private val account_creat = "create table diarydata (" +
            "id integer primary key autoincrement," +
            "datetext text," +
            "diarytext text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(account_creat)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
```

## 3.Activity

#### 1.MainActivity.kt

```kotlin
package com.lnm011223.mysimplediary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.diary_layout.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val diarylist = ArrayList<Diary>()
    val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        add_button.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        listview.setOnItemClickListener { parent, view, position, id ->
            val diary = diarylist[position]
            val intent = Intent(this,CheckActivity::class.java)
            intent.putExtra("diarytext",diary.diarytext)
            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()
        val adapter = DiaryAdapter(this,R.layout.diary_item,diarylist)
        listview.adapter = adapter
        val db = dbHelper.writableDatabase
        initDiarys()

    }
    @SuppressLint("Range")
    private fun initDiarys(){

        thread {
            diarylist.clear()
            val db = dbHelper.writableDatabase
            val cursor = db.query("diarydata",null,null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))

                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    diarylist.add(Diary(datetext,diarytext))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }

    }
}
```

#### 2.AddActivity.kt

```kotlin
package com.lnm011223.mysimplediary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        datetext.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        complete_button.setOnClickListener {
            val diary_text = diary_edit.text.toString()
            val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("datetext", datetext.text as String)
                put("diarytext",diary_text)
            }
            db.insert("diarydata",null,diary_value)
            finish()
        }
    }
}
```

#### 3.CheckActivity.kt

```kotlin
package com.lnm011223.mysimplediary

import android.app.AlertDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_check.*

class CheckActivity : AppCompatActivity() {
    var diarytext = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        diarytext = intent.getStringExtra("diarytext").toString()
        val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
        val db = dbHelper.writableDatabase
        val diarytext_backup = diarytext
        diary_edit1.setText(diarytext)
        reset_button.setOnClickListener {
            diarytext = diary_edit1.text.toString()
            val diary_value = ContentValues().apply {


                put("diarytext",diarytext)
            }
            db.update("diarydata",diary_value,"diarytext = ?", arrayOf(diarytext_backup))
            finish()
        }
        delete_button.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("确认：")
                setMessage("真的要删除这条记录吗？")
                setNegativeButton("否") { _, _ ->


                }
                setPositiveButton("是") { _, _ ->
                    db.delete("diarydata", "diarytext = ?", arrayOf(diarytext_backup))
                    finish()


                }


                show()
            }
        }
    }
}
```



## 4.布局文件

#### 1.activity_main.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    >


    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card1"
        android:layout_width="match_parent"
        android:layout_height="615dp"
        android:layout_margin="20dp"

        app:cardElevation="5dp"
        app:layout_constraintTop_toTopOf="parent"
        tools:layout_editor_absoluteX="24dp">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginBottom="8dp">

            <ListView
                android:id="@+id/listview"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

        </androidx.constraintlayout.widget.ConstraintLayout>


    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
        android:id="@+id/add_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:backgroundTint="#2196F3"
        android:text="编写日记"
        android:textColor="#ffffff"
        app:borderWidth="0dp"

        app:icon="@drawable/ic_baseline_add_24"
        app:iconTint="#ffffff"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/card1" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 2.activity_add.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".AddActivity">

    <TextView
        android:textSize="20sp"
        android:layout_marginTop="20dp"
        android:id="@+id/datetext"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="#2196f3"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card3"
        android:layout_width="match_parent"
        android:layout_height="452dp"
        android:layout_margin="20dp"

        android:layout_marginTop="90dp"

        app:cardElevation="5dp"
        app:layout_constraintTop_toBottomOf="@id/datetext"
        tools:layout_editor_absoluteX="20dp">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginBottom="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="日记内容"
                android:textSize="25sp"
                android:id="@+id/title_text"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textField"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_constraintTop_toBottomOf="@id/title_text"
                android:layout_marginTop="10dp"
                >

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:id="@+id/diary_edit"
                    />

            </com.google.android.material.textfield.TextInputLayout>


        </androidx.constraintlayout.widget.ConstraintLayout>


    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
        android:id="@+id/complete_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:backgroundTint="#2196F3"
        android:text="完成"
        android:textColor="#ffffff"
        app:borderWidth="0dp"

        app:icon="@drawable/ic_baseline_check_24"
        app:iconTint="#ffffff"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/card2" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 3.activity_check.xml

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".CheckActivity">
    <com.google.android.material.card.MaterialCardView
        android:id="@+id/card1"
        android:layout_width="match_parent"
        android:layout_height="615dp"
        android:layout_margin="20dp"

        app:cardElevation="5dp"
        app:layout_constraintTop_toTopOf="parent"
        tools:layout_editor_absoluteX="24dp">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginBottom="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="日记内容"
                android:textSize="25sp"
                android:id="@+id/title_text"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textField"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_constraintTop_toBottomOf="@id/title_text"
                android:layout_marginTop="10dp"
                >

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:id="@+id/diary_edit1"
                    />

            </com.google.android.material.textfield.TextInputLayout>

        </androidx.constraintlayout.widget.ConstraintLayout>


    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
        android:id="@+id/reset_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:backgroundTint="#2196F3"
        android:text="修改日记"
        android:textColor="#ffffff"
        app:borderWidth="0dp"

        app:icon="@drawable/ic_baseline_edit_24"
        app:iconTint="#ffffff"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/delete_button"
        app:layout_constraintHorizontal_bias="0.913"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/card1" />

    <com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
        android:id="@+id/delete_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:backgroundTint="#F44336"
        android:text="删除日记"
        android:textColor="#ffffff"
        app:borderWidth="0dp"

        app:icon="@drawable/ic_baseline_delete_24"
        app:iconTint="#ffffff"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.807"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/card1" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## 难点

1.创建和修改完日记立即生效

解决方案：把加载操作放在onstart（）里面，这个方法会在该activity或fragment变为可见时立即调用，即返回日记列表以后就会重新加载新的

2.修改日历操作

解决方案：通过intent传递该item的数据，再匹配表里的内容就好了