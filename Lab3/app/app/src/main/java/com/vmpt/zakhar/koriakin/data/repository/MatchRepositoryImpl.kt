package com.vmpt.zakhar.koriakin.data.repository

import com.vmpt.zakhar.koriakin.data.local.dao.MatchDao
import com.vmpt.zakhar.koriakin.data.local.entity.MatchEntity
import com.vmpt.zakhar.koriakin.domain.model.MatchOutcome
import com.vmpt.zakhar.koriakin.domain.model.MatchRecord
import com.vmpt.zakhar.koriakin.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao
) : MatchRepository {

    override fun observeMatchHistory(): Flow<List<MatchRecord>> {
        return matchDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveFinishedMatch(outcome: MatchOutcome, finishedAtMillis: Long) {
        val code = when (outcome) {
            MatchOutcome.X_WINS -> CODE_X
            MatchOutcome.O_WINS -> CODE_O
            MatchOutcome.DRAW -> CODE_DRAW
        }
        matchDao.insert(
            MatchEntity(
                finishedAtMillis = finishedAtMillis,
                outcomeCode = code
            )
        )
    }

    private fun MatchEntity.toDomain(): MatchRecord {
        val outcome = when (outcomeCode) {
            CODE_X -> MatchOutcome.X_WINS
            CODE_O -> MatchOutcome.O_WINS
            else -> MatchOutcome.DRAW
        }
        return MatchRecord(
            id = id,
            finishedAtMillis = finishedAtMillis,
            outcome = outcome
        )
    }

    companion object {
        private const val CODE_X = 0
        private const val CODE_O = 1
        private const val CODE_DRAW = 2
    }
}
