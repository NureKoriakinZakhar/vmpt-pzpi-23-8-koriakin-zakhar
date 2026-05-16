package com.koriakin.zakhar.lab.domain.repository

import com.koriakin.zakhar.lab.domain.model.GameState
import kotlinx.coroutines.flow.StateFlow

interface GameRepository {
    val gameState: StateFlow<GameState>
    fun connect(serverAddress: String, playerName: String)
    fun sendTap()
    fun disconnect()
}
