package com.koriakin.zakhar.lab.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.koriakin.zakhar.lab.domain.model.GameState
import com.koriakin.zakhar.lab.domain.usecase.ConnectUseCase
import com.koriakin.zakhar.lab.domain.usecase.DisconnectUseCase
import com.koriakin.zakhar.lab.domain.usecase.ObserveGameStateUseCase
import com.koriakin.zakhar.lab.domain.usecase.TapUseCase
import com.koriakin.zakhar.lab.presentation.model.PlayerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val connectUseCase: ConnectUseCase,
    private val tapUseCase: TapUseCase,
    private val disconnectUseCase: DisconnectUseCase,
    observeGameStateUseCase: ObserveGameStateUseCase
) : ViewModel() {

    val gameState: StateFlow<GameState> = observeGameStateUseCase()

    fun connect(serverAddress: String, playerName: String) {
        connectUseCase(serverAddress, playerName)
    }

    fun tap() {
        tapUseCase()
    }

    fun disconnect() {
        disconnectUseCase()
    }

    fun buildLeaderboardItems(): List<PlayerUiModel> {
        val state = gameState.value
        return state.leaderboard.mapIndexed { index, player ->
            PlayerUiModel(
                id = player.id,
                rank = index + 1,
                name = player.name,
                score = player.score,
                isMe = player.id == state.myPlayerId
            )
        }
    }

    fun getMyScore(): Int {
        val state = gameState.value
        return state.leaderboard.find { it.id == state.myPlayerId }?.score ?: 0
    }

    override fun onCleared() {
        super.onCleared()
        disconnectUseCase()
    }
}
