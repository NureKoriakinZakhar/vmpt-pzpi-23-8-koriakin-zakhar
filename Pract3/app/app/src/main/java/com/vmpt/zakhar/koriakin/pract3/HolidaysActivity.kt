package com.vmpt.zakhar.koriakin.pract3

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class HolidaysActivity : AppCompatActivity() {

    private val holidaysByMonth: Map<Int, List<String>> = mapOf(
        1 to listOf(
            "1 січня — Новий рік",
            "7 січня — Різдво Христове (юліанський календар)",
            "22 січня — День Соборності України",
        ),
        2 to listOf(
            "14 лютого — День закоханих",
        ),
        3 to listOf(
            "8 березня — Міжнародний жіночий день",
        ),
        4 to listOf(
            "1 квітня — День сміху",
            "7 квітня — Всесвітній день здоров'я",
            "Великдень — пересувна дата",
        ),
        5 to listOf(
            "1 травня — День праці",
            "9 травня — День перемоги над нацизмом у Другій світовій війні",
            "Друга неділя травня — День матері",
        ),
        6 to listOf(
            "1 червня — Міжнародний день захисту дітей",
            "28 червня — День Конституції України",
            "Трійця — пересувна дата",
        ),
        7 to listOf(
            "28 липня — День Української Державності",
        ),
        8 to listOf(
            "23 серпня — День Державного Прапора України",
            "24 серпня — День Незалежності України",
        ),
        9 to listOf(
            "1 вересня — День знань",
        ),
        10 to listOf(
            "1 жовтня — Міжнародний день людей похилого віку",
            "14 жовтня — День захисників і захисниць України",
        ),
        11 to listOf(
            "21 листопада — День Гідності та Свободи",
            "Четверта субота листопада — День пам'яті жертв Голодоморів",
        ),
        12 to listOf(
            "6 грудня — День Збройних Сил України",
            "13 грудня — День добровольця",
            "19 грудня — День святого Миколая",
            "25 грудня — Різдво Христове (григоріанський календар)",
            "31 грудня — Новий рік",
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_holidays)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val monthNames = resources.getStringArray(R.array.month_names)
        val spinner = findViewById<Spinner>(R.id.spinnerMonth)
        val listView = findViewById<ListView>(R.id.listHolidays)

        val adapterSpinner = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            monthNames,
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        val listAdapter = ArrayAdapter(
            this,
            R.layout.item_holiday_row,
            R.id.holiday_text,
            mutableListOf<String>(),
        )
        listView.adapter = listAdapter

        fun showMonth(monthIndexZeroBased: Int) {
            val monthNumber = monthIndexZeroBased + 1
            val items = holidaysByMonth[monthNumber].orEmpty()
            listAdapter.clear()
            listAdapter.addAll(items)
            listAdapter.notifyDataSetChanged()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                showMonth(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        showMonth(spinner.selectedItemPosition.coerceAtLeast(0))

        findViewById<MaterialButton>(R.id.btnHolidaysToMain).setOnClickListener {
            finish()
        }
    }
}
