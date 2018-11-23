package com.test.testapplication.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.test.testapplication.extentions.dpToPx
import android.opengl.ETC1.getHeight


class ContactDividerItemDecoration(val context: Context, val orientation: Int, val margin: Int) :
    RecyclerView.ItemDecoration() {

    var mDivider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else if (orientation == HORIZONTAL_LIST) {
            drawHorizontal(c, parent)
        } else {
            super.onDraw(c, parent, state)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            mDivider?.let {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + it.intrinsicHeight
                it.setBounds(
                    left + context.dpToPx(margin),
                    top, right, bottom
                )
                it.draw(c)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            mDivider?.let {
                val child = parent.getChildAt(i)
                val params = child
                    .layoutParams as RecyclerView.LayoutParams
                val left = child.right + params.rightMargin
                val right = left + it.intrinsicHeight
                it.setBounds(left, top + context.dpToPx(margin), right, bottom - context.dpToPx(margin))
                it.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        mDivider?.let {
            if (orientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, it.intrinsicHeight);
            } else if (orientation == HORIZONTAL_LIST) {
                outRect.set(0, 0, it.intrinsicWidth, 0);
            } else {
                super.getItemOffsets(outRect, view, parent, state)
            }
        } ?: super.getItemOffsets(outRect, view, parent, state)
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)

        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL

        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}