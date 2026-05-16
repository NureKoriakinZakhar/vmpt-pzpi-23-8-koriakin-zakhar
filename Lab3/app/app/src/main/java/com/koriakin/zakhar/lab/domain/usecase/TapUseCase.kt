package com.koriakin.zakhar.lab.domain.usecase

import com.koriakin.zakhar.lab.domain.repository.GameRepository
import javax.inject.Inject

class TapUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke() {
        repository.sendTap()
    }
}
