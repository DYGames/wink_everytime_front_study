package com.example.estudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    val days = arrayOf("", "월", "화", "수", "목", "금", "토", "일")
    val times = Array(24, { i -> (i.toString() + "시") })
    val calendarData: Map<Int, CalendarData> = mapOf(
        0 to CalendarData(0, "응용통계학", "김도엽", "미래관 2층 32호실"),
        15 to CalendarData(15, "인공지능", "김도3", "미래관 6층 23호실")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val grid: GridLayout = findViewById(R.id.gridLayout)
        grid.columnCount = 8
        grid.rowCount = 49

        createCell(100, 100, 0, 0, grid)

        for (i: Int in 1 until grid.columnCount) {
            val layout = createCell(500, 100, i, 0, grid)
            val text = TextView(this)
            text.textSize = 10f
            text.text = days[i]
            text.gravity = Gravity.CENTER
            text.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            layout.addView(text)
        }

        for (i: Int in 1 until grid.rowCount) {
            val layout = createCell(100, 300, 0, i, grid)
            val text = TextView(this)
            text.text =
                if ((i - 1) % 2 == 0) times[(i - 1) / 2].toString() else times[(i - 1) / 2].toString() + "\n30분"
            text.textSize = 10f
            text.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            text.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            layout.addView(text)
        }

        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val layout = createCell(500, 300, j, i, grid)
                val cell: View = layoutInflater.inflate(R.layout.calendar_item, layout)
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                if (calendarData.containsKey(idx)) {
                    val data = calendarData[idx]
                    cell.findViewById<TextView>(R.id.calendar_item_title).text = data?.title
                    cell.findViewById<TextView>(R.id.calendar_item_location).text = data?.location
                    cell.findViewById<TextView>(R.id.calendar_item_user).text = data?.user
                }
            }
        }
    }

    private fun createCell(w: Int, h: Int, c: Int, r: Int, grid: GridLayout): ConstraintLayout {
        val layout = ConstraintLayout(this)
        val param: GridLayout.LayoutParams = GridLayout.LayoutParams()
        param.setGravity(Gravity.CENTER)
        param.columnSpec = GridLayout.spec(c)
        param.rowSpec = GridLayout.spec(r)
        param.width = w
        param.height = h
        layout.layoutParams = param
        grid.addView(layout)
        return layout
    }
}