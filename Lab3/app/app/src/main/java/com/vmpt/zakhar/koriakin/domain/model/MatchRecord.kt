package com.vmpt.zakhar.koriakin.domain.model

data class MatchRecord(
    val id: Long,
    val finishedAtMillis: Long,
    val outcome: MatchOutcome
)
