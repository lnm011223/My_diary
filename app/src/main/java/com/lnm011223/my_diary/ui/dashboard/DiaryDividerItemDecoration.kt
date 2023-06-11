package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.util.DensityUtil


/**

 * @Author liangnuoming
 * @Date 2023/6/10-18:20

 */

class DiaryDividerItemDecoration : RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint()
    private var rectPaint: Paint = Paint()


    init {
        mPaint.color = Color.parseColor("#3EB06A")
        if (BaseUtil.isDarkTheme(MyApplication.context)) {
            rectPaint.color = Color.parseColor("#292929")
        }else{
            rectPaint.color = Color.parseColor("#b2b2b2")

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
            c.drawRect((x - 5).toFloat(), 0f, (x + 5).toFloat(), child.bottom.toFloat(), rectPaint)


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
            val x = manager.getLeftDecorationWidth(child) / 2
            val y = child.top + child.height / 2
            c.drawCircle(x.toFloat(), y.toFloat(), 20f, mPaint)


        }
    }
}


class DividerItemDecoration : ItemDecoration() {
    private val mPaint: Paint

    // 在构造函数里进行绘制的初始化，如画笔属性设置等
    init {
        mPaint = Paint()
        mPaint.color = Color.RED
        // 画笔颜色设置为红色
    }

    // 重写getItemOffsets（）方法
    // 作用：设置矩形OutRect 与 Item 的间隔区域
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        // 获得每个Item的位置

        // 第1个Item不绘制分割线
        if (itemPosition != 0) {
            outRect[0, 0, 0] = 10
            // 设置间隔区域为10px,即onDraw()可绘制的区域为10px
        }
    }

    // 重写onDraw（）
    // 作用:在间隔区域里绘制一个矩形，即分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        // 获取RecyclerView的Child view的个数
        val childCount = parent.childCount

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (i in 0 until childCount) {
            // 获取每个Item的位置
            val child = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(child)
            // 第1个Item不需要绘制
            if (index == 0) {
                continue
            }

            // 获取布局参数
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            // 设置矩形(分割线)的宽度为10px
            val mDivider = 10

            // 根据子视图的位置 & 间隔区域，设置矩形（分割线）的2个顶点坐标(左上 & 右下)

            // 矩形左上顶点 = (ItemView的左边界,ItemView的下边界)
            // ItemView的左边界 = RecyclerView 的左边界 + paddingLeft距离 后的位置
            val left = parent.paddingLeft
            // ItemView的下边界：ItemView 的 bottom坐标 + 距离RecyclerView底部距离 +translationY
            val top = child.bottom + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child))

            // 矩形右下顶点 = (ItemView的右边界,矩形的下边界)
            // ItemView的右边界 = RecyclerView 的右边界减去 paddingRight 后的坐标位置
            val right = parent.width - parent.paddingRight
            // 绘制分割线的下边界 = ItemView的下边界+分割线的高度
            val bottom = top + mDivider


            // 通过Canvas绘制矩形（分割线）
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }
}
