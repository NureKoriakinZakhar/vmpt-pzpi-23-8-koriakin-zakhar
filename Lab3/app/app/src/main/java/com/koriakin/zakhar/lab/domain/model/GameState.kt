package com.koriakin.zakhar.lab.domain.model

data class GameState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val myPlayerId: String? = null,
    val leaderboard: List<Player> = emptyList()
)
