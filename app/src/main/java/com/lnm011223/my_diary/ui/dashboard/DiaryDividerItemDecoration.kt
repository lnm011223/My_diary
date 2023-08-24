package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.util.BaseUtil.printToLog
import com.lnm011223.my_diary.util.DensityUtil


/**

 * @Author liangnuoming
 * @Date 2023/6/10-18:20

 */

@SuppressLint("ResourceAsColor")
class DiaryDividerItemDecoration(activity:Activity) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint()
    private var rectPaint: Paint = Paint()
    private var mPaint1: Paint = Paint()


    init {
        mPaint.color = Color.parseColor("#87b99f")
        if (BaseUtil.isDarkTheme(activity)) {
            rectPaint.color = Color.parseColor("#292929")
            mPaint1.color = Color.parseColor("#1E1E1E")
        } else {
            rectPaint.color = Color.parseColor("#b2b2b2")
            mPaint1.color = Color.parseColor("#f6f6f6")
        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        // 获得每个Item的位置
        outRect.left = 100


    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val manager = parent.layoutManager as RecyclerView.LayoutManager
            val x = manager.getLeftDecorationWidth(child) / 2
            val y = child.top + child.height / 2
            c.drawRect((x - 3).toFloat(), 0f, (x + 3).toFloat(), parent.height.toFloat(), rectPaint)
            break

        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)

            val index = parent.getChildAdapterPosition(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val manager = parent.layoutManager as RecyclerView.LayoutManager
            val viewToMeasure = child.findViewById<View>(R.id.diarycard_date) // 替换成你要获取高度的控件ID
            val itemHeight = viewToMeasure?.height ?: 0
            val x = manager.getLeftDecorationWidth(child) / 2
//            val y = child.top + child.height / 2
            val y = child.top + itemHeight/2 + 5
            c.drawCircle(x.toFloat(), y.toFloat(), 30f, mPaint1)
            c.drawCircle(x.toFloat(), y.toFloat(), 20f, mPaint)
            c.drawCircle(x.toFloat(), y.toFloat(), 10f, mPaint1)

        }
    }
}


