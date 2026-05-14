package com.vmpt.zakhar.koriakin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val finishedAtMillis: Long,
    val outcomeCode: Int
)
