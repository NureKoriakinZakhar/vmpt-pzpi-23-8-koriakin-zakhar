package com.koriakin.zakhar.lab.data.repository

import com.koriakin.zakhar.lab.data.remote.GameRemoteDataSource
import com.koriakin.zakhar.lab.domain.model.GameState
import com.koriakin.zakhar.lab.domain.repository.GameRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val remoteDataSource: GameRemoteDataSource
) : GameRepository {

    override val gameState: StateFlow<GameState> = remoteDataSource.gameState

    override fun connect(serverAddress: String, playerName: String) {
        remoteDataSource.connect(serverAddress, playerName)
    }

    override fun sendTap() {
        remoteDataSource.sendTap()
    }

    override fun disconnect() {
        remoteDataSource.disconnect()
    }
}
