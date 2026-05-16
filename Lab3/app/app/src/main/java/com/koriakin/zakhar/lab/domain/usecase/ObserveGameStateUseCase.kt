package com.koriakin.zakhar.lab.domain.usecase

import com.koriakin.zakhar.lab.domain.model.GameState
import com.koriakin.zakhar.lab.domain.repository.GameRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveGameStateUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): StateFlow<GameState> = repository.gameState
}
