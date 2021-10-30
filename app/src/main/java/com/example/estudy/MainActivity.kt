package com.example.estudy

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import android.view.*
import android.widget.*

import androidx.core.view.MenuItemCompat


class MainActivity : AppCompatActivity() {

    val days = arrayOf("", "월", "화", "수", "목", "금", "토", "일")
    val times = Array(24, { i -> (i.toString() + "시") })
    var calendarData = mutableMapOf(
        0 to CalendarData(0, "응용통계학", "김도엽", "미래관 2층 32호실"),
        15 to CalendarData(15, "인공지능", "김도3", "미래관 6층 23호실")
    )
    var testData = mutableMapOf(
        13 to CalendarData(13, "응용통계학", "김도엽", "미래관 2층 32호실"),
        24 to CalendarData(24, "인공지능", "김도3", "미래관 6층 23호실")
    )
    var cells = mutableMapOf<Int, View>()
    lateinit var spinner: Spinner

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
                cells[idx] = cell
                if (calendarData.containsKey(idx)) {
                    val data = calendarData[idx]
                    cell.findViewById<TextView>(R.id.calendar_item_title).text = data?.title
                    cell.findViewById<TextView>(R.id.calendar_item_location).text = data?.location
                    cell.findViewById<TextView>(R.id.calendar_item_user).text = data?.user
                    cell.setOnClickListener {
                        val view = layoutInflater.inflate(R.layout.dialog_calendar, null)
                        view.findViewById<EditText>(R.id.dialog_calendar_title)
                            .setText(calendarData[idx]?.title)
                        view.findViewById<EditText>(R.id.dialog_calendar_location)
                            .setText(calendarData[idx]?.location)
                        view.findViewById<EditText>(R.id.dialog_calendar_user)
                            .setText(calendarData[idx]?.user)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                        builder
                            .setView(view)
                            .setPositiveButton(
                                "수정",
                                DialogInterface.OnClickListener { dialog, index ->
                                    calendarData[idx]?.title =
                                        view.findViewById<EditText>(R.id.dialog_calendar_title).text.toString()
                                    calendarData[idx]?.location =
                                        view.findViewById<EditText>(R.id.dialog_calendar_location).text.toString()
                                    calendarData[idx]?.user =
                                        view.findViewById<EditText>(R.id.dialog_calendar_user).text.toString()
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.cancel()
                            })
                            .create().show()
                    }
                    cell.setOnLongClickListener {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("삭제하시겠습니까?")
                            .setPositiveButton("삭제",
                                DialogInterface.OnClickListener { dialog, id ->
                                    calendarData.remove(idx)
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                            .create().show()
                        return@setOnLongClickListener true
                    }
                } else {
                    layout.setOnClickListener {
                        val view = layoutInflater.inflate(R.layout.dialog_calendar, null)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                        builder
                            .setView(view)
                            .setPositiveButton(
                                "등록",
                                DialogInterface.OnClickListener { dialog, index ->
                                    calendarData[idx] = CalendarData(
                                        idx,
                                        view.findViewById<EditText>(R.id.dialog_calendar_title).text.toString(),
                                        view.findViewById<EditText>(R.id.dialog_calendar_user).text.toString(),
                                        view.findViewById<EditText>(R.id.dialog_calendar_location).text.toString()
                                    )
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.cancel()
                            })
                            .create().show()
                    }
                }
            }
        }
    }

    private fun refreshCell(datas: MutableMap<Int, CalendarData>) {
        val grid: GridLayout = findViewById(R.id.gridLayout)

        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                val cell: View? = cells[idx]
                if (datas.containsKey(idx)) {
                    val data = datas[idx]
                    cell?.findViewById<TextView>(R.id.calendar_item_title)?.text = data?.title
                    cell?.findViewById<TextView>(R.id.calendar_item_location)?.text = data?.location
                    cell?.findViewById<TextView>(R.id.calendar_item_user)?.text = data?.user
                } else {
                    cell?.findViewById<TextView>(R.id.calendar_item_title)?.text = ""
                    cell?.findViewById<TextView>(R.id.calendar_item_location)?.text = ""
                    cell?.findViewById<TextView>(R.id.calendar_item_user)?.text = ""
                }
            }
        }
    }

    private fun compareData(data0: MutableMap<Int, CalendarData>, data1: MutableMap<Int, CalendarData>) {
        val grid: GridLayout = findViewById(R.id.gridLayout)

        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                val cell: View? = cells[idx]
                if(!data0.containsKey(idx) and !data1.containsKey(idx)) {
                    cell?.findViewById<TextView>(R.id.calendar_item_title)?.text = "비는 시간"
                    cell?.findViewById<TextView>(R.id.calendar_item_location)?.text = ""
                    cell?.findViewById<TextView>(R.id.calendar_item_user)?.text = ""
                } else {
                    cell?.findViewById<TextView>(R.id.calendar_item_title)?.text = ""
                    cell?.findViewById<TextView>(R.id.calendar_item_location)?.text = ""
                    cell?.findViewById<TextView>(R.id.calendar_item_user)?.text = ""
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.android_action_bar_spinner_menu, menu)
        val item: MenuItem = menu.findItem(R.id.spinner)
        spinner = MenuItemCompat.getActionView(item) as Spinner
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf("시간표 1", "시간표 2", "시간표 대조")
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    refreshCell(calendarData)
                } else if (position == 1) {
                    refreshCell(testData)
                } else if (position == 2) {
                    compareData(calendarData, testData)
                }
            }

        }
        return true
    }
}