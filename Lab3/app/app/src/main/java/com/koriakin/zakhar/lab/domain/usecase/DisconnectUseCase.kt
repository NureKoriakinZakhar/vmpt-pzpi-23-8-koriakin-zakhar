package com.koriakin.zakhar.lab.domain.usecase

import com.koriakin.zakhar.lab.domain.repository.GameRepository
import javax.inject.Inject

class DisconnectUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}
