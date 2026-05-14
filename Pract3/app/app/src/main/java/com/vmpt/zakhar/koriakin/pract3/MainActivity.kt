package com.vmpt.zakhar.koriakin.pract3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        findViewById<MaterialCardView>(R.id.cardMenuHolidays).setOnClickListener {
            startActivity(Intent(this, HolidaysActivity::class.java))
        }
        findViewById<MaterialCardView>(R.id.cardMenuGame).setOnClickListener {
            startActivity(Intent(this, CardsGameActivity::class.java))
        }
    }
}
