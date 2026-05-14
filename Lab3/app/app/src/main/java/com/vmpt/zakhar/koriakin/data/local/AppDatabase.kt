package com.vmpt.zakhar.koriakin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vmpt.zakhar.koriakin.data.local.dao.MatchDao
import com.vmpt.zakhar.koriakin.data.local.entity.MatchEntity

@Database(
    entities = [MatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
}
