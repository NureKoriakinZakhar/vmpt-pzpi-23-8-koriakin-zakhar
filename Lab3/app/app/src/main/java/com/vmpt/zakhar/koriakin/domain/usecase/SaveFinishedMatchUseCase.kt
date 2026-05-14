package com.vmpt.zakhar.koriakin.domain.usecase

import com.vmpt.zakhar.koriakin.domain.model.MatchOutcome
import com.vmpt.zakhar.koriakin.domain.repository.MatchRepository
import javax.inject.Inject

class SaveFinishedMatchUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(outcome: MatchOutcome, finishedAtMillis: Long) {
        repository.saveFinishedMatch(outcome, finishedAtMillis)
    }
}
