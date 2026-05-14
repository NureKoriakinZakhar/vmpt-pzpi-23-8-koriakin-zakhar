package com.vmpt.zakhar.koriakin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vmpt.zakhar.koriakin.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY finishedAtMillis DESC")
    fun observeAll(): Flow<List<MatchEntity>>

    @Insert
    suspend fun insert(entity: MatchEntity): Long
}
