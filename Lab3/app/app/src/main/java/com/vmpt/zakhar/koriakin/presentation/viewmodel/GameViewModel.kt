package com.vmpt.zakhar.koriakin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmpt.zakhar.koriakin.domain.model.MatchOutcome
import com.vmpt.zakhar.koriakin.domain.usecase.SaveFinishedMatchUseCase
import com.vmpt.zakhar.koriakin.presentation.model.CellMark
import com.vmpt.zakhar.koriakin.presentation.model.GameStatus
import com.vmpt.zakhar.koriakin.presentation.model.GameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val saveFinishedMatch: SaveFinishedMatchUseCase
) : ViewModel() {

    private val winningLines: List<List<Int>> = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private var roundPersisted = false

    fun onCellClicked(index: Int) {
        val snap = _state.value
        if (snap.status != GameStatus.IN_PROGRESS) return
        if (index !in 0..8) return
        if (snap.cells[index] != CellMark.EMPTY) return
        val updatedCells = snap.cells.toMutableList()
        updatedCells[index] = snap.nextMark
        val lineWinner = winnerMark(updatedCells)
        val newStatus = when {
            lineWinner == CellMark.X -> GameStatus.X_WON
            lineWinner == CellMark.O -> GameStatus.O_WON
            updatedCells.none { it == CellMark.EMPTY } -> GameStatus.DRAW
            else -> GameStatus.IN_PROGRESS
        }
        val nextMark = if (newStatus == GameStatus.IN_PROGRESS) {
            toggleTurnMark(snap.nextMark)
        } else {
            snap.nextMark
        }
        _state.value = GameUiState(
            cells = updatedCells,
            status = newStatus,
            nextMark = nextMark
        )
        if (newStatus != GameStatus.IN_PROGRESS && !roundPersisted) {
            persistFinishedRound(newStatus)
        }
    }

    fun onNewRoundClicked() {
        _state.value = GameUiState()
        roundPersisted = false
    }

    private fun winnerMark(cells: List<CellMark>): CellMark? {
        for (line in winningLines) {
            val a = cells[line[0]]
            val b = cells[line[1]]
            val c = cells[line[2]]
            if (a != CellMark.EMPTY && a == b && a == c) {
                return a
            }
        }
        return null
    }

    private fun toggleTurnMark(mark: CellMark): CellMark {
        return when (mark) {
            CellMark.X -> CellMark.O
            CellMark.O -> CellMark.X
            CellMark.EMPTY -> CellMark.X
        }
    }

    private fun persistFinishedRound(status: GameStatus) {
        val outcome = when (status) {
            GameStatus.X_WON -> MatchOutcome.X_WINS
            GameStatus.O_WON -> MatchOutcome.O_WINS
            GameStatus.DRAW -> MatchOutcome.DRAW
            GameStatus.IN_PROGRESS -> return
        }
        roundPersisted = true
        val finishedAt = System.currentTimeMillis()
        viewModelScope.launch {
            saveFinishedMatch(outcome, finishedAt)
        }
    }
}
