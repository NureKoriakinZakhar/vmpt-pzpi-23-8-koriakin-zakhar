package com.vmpt.zakhar.koriakin.presentation.model

data class GameUiState(
    val cells: List<CellMark> = List(9) { CellMark.EMPTY },
    val status: GameStatus = GameStatus.IN_PROGRESS,
    val nextMark: CellMark = CellMark.X
)
