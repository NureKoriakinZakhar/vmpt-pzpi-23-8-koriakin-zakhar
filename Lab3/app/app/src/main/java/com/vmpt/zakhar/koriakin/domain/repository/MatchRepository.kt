package com.vmpt.zakhar.koriakin.domain.repository

import com.vmpt.zakhar.koriakin.domain.model.MatchOutcome
import com.vmpt.zakhar.koriakin.domain.model.MatchRecord
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun observeMatchHistory(): Flow<List<MatchRecord>>
    suspend fun saveFinishedMatch(outcome: MatchOutcome, finishedAtMillis: Long)
}
