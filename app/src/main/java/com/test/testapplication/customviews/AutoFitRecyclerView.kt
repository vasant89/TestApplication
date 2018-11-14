package com.test.testapplication.customviews

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class AutoFitRecyclerView : RecyclerView {

    lateinit var gridLayoutManager: GridLayoutManager
    var columnWidth = -1

    constructor(context: Context) : this(context, null) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val array = context.obtainStyledAttributes(it, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
        gridLayoutManager = GridLayoutManager(getContext(), 1)
        layoutManager = gridLayoutManager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            gridLayoutManager.spanCount  = Math.max(1, measuredWidth / columnWidth)
        }
    }
}