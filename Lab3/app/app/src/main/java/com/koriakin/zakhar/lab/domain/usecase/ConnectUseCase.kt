package com.koriakin.zakhar.lab.domain.usecase

import com.koriakin.zakhar.lab.domain.repository.GameRepository
import javax.inject.Inject

class ConnectUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(serverAddress: String, playerName: String) {
        repository.connect(serverAddress, playerName)
    }
}
