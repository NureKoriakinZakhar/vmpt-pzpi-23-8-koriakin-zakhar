package com.vmpt.zakhar.koriakin.pract3

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class CardsGameActivity : AppCompatActivity() {

    private lateinit var statusView: TextView
    private lateinit var cardViews: List<MaterialCardView>
    private lateinit var cardTexts: List<TextView>

    private var winningIndex: Int = 0
    private val revealed = BooleanArray(3)
    private var gameEnded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cards_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cardsRoot)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        statusView = findViewById(R.id.textGameStatus)
        cardViews = listOf(
            findViewById(R.id.cardSlot1),
            findViewById(R.id.cardSlot2),
            findViewById(R.id.cardSlot3),
        )
        cardTexts = listOf(
            findViewById(R.id.textCardSlot1),
            findViewById(R.id.textCardSlot2),
            findViewById(R.id.textCardSlot3),
        )

        findViewById<MaterialButton>(R.id.btnPlayAgain).setOnClickListener {
            startNewRound()
        }
        findViewById<MaterialButton>(R.id.btnCardsToMain).setOnClickListener {
            finish()
        }

        cardViews.forEachIndexed { index, card ->
            card.setOnClickListener { onCardClicked(index) }
        }

        startNewRound()
    }

    private fun color(@ColorRes id: Int): Int =
        ContextCompat.getColor(this, id)

    private fun resetFaces() {
        cardViews.forEach { card ->
            card.isClickable = true
            card.setStrokeColor(ColorStateList.valueOf(color(R.color.card_face_down_stroke)))
            card.setCardBackgroundColor(color(R.color.card_face_down))
        }
        cardTexts.forEach { text ->
            text.setTextColor(color(R.color.card_label_on_dark))
        }
    }

    private fun startNewRound() {
        winningIndex = Random.nextInt(3)
        revealed.fill(false)
        gameEnded = false
        statusView.text = ""
        statusView.setTextColor(color(R.color.status_hint))
        resetFaces()
        cardTexts.forEach { it.text = getString(R.string.card_back) }
    }

    private fun onCardClicked(index: Int) {
        if (gameEnded || revealed[index]) return
        revealed[index] = true
        val card = cardViews[index]
        val label = cardTexts[index]
        if (index == winningIndex) {
            label.text = getString(R.string.card_win)
            label.setTextColor(color(R.color.card_label_on_light))
            card.setCardBackgroundColor(color(R.color.card_face_win))
            card.setStrokeColor(ColorStateList.valueOf(color(R.color.accent_gold)))
            statusView.text = getString(R.string.game_won)
            statusView.setTextColor(color(R.color.status_win))
            gameEnded = true
            revealRemainingFaces()
        } else {
            label.text = getString(R.string.card_empty)
            label.setTextColor(color(R.color.card_label_on_light))
            card.setCardBackgroundColor(color(R.color.card_face_empty))
            card.setStrokeColor(ColorStateList.valueOf(color(R.color.outline_soft)))
            card.isClickable = false
            statusView.text = getString(R.string.game_try_another)
            statusView.setTextColor(color(R.color.status_hint))
        }
    }

    private fun revealRemainingFaces() {
        cardViews.forEachIndexed { i, card ->
            if (!revealed[i]) {
                val isWinning = i == winningIndex
                cardTexts[i].text = if (isWinning) {
                    getString(R.string.card_win)
                } else {
                    getString(R.string.card_empty)
                }
                cardTexts[i].setTextColor(color(R.color.card_label_on_light))
                card.setCardBackgroundColor(
                    color(if (isWinning) R.color.card_face_win else R.color.card_face_empty),
                )
                card.setStrokeColor(
                    ColorStateList.valueOf(
                        color(if (isWinning) R.color.accent_gold else R.color.outline_soft),
                    ),
                )
            }
            card.isClickable = false
        }
    }
}
