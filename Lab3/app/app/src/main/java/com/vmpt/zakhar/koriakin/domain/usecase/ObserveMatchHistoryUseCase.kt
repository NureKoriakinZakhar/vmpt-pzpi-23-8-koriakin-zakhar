package com.vmpt.zakhar.koriakin.domain.usecase

import com.vmpt.zakhar.koriakin.domain.model.MatchRecord
import com.vmpt.zakhar.koriakin.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMatchHistoryUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(): Flow<List<MatchRecord>> = repository.observeMatchHistory()
}
