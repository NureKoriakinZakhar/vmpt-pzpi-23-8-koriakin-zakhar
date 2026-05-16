package com.koriakin.zakhar.lab.data.remote

import com.google.gson.Gson
import com.koriakin.zakhar.lab.data.model.ServerMessage
import com.koriakin.zakhar.lab.domain.model.ConnectionStatus
import com.koriakin.zakhar.lab.domain.model.GameState
import com.koriakin.zakhar.lab.domain.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRemoteDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null
    private var activeListenerId = 0

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun connect(serverAddress: String, playerName: String) {
        webSocket?.close(1000, null)
        webSocket = null
        val listenerId = ++activeListenerId
        _gameState.value = GameState(connectionStatus = ConnectionStatus.CONNECTING)
        val request = Request.Builder().url("ws://$serverAddress").build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                ws.send(gson.toJson(mapOf("type" to "join", "name" to playerName)))
            }

            override fun onMessage(ws: WebSocket, text: String) {
                if (activeListenerId != listenerId) return
                val message = gson.fromJson(text, ServerMessage::class.java)
                when (message.type) {
                    "joined" -> _gameState.value = _gameState.value.copy(
                        connectionStatus = ConnectionStatus.CONNECTED,
                        myPlayerId = message.id
                    )
                    "leaderboard" -> {
                        val players = message.players?.map {
                            Player(id = it.id, name = it.name, score = it.score)
                        } ?: emptyList()
                        _gameState.value = _gameState.value.copy(leaderboard = players)
                    }
                }
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                if (activeListenerId != listenerId) return
                _gameState.value = GameState(connectionStatus = ConnectionStatus.DISCONNECTED)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                if (activeListenerId != listenerId) return
                _gameState.value = GameState(connectionStatus = ConnectionStatus.DISCONNECTED)
            }
        })
    }

    fun sendTap() {
        webSocket?.send(gson.toJson(mapOf("type" to "tap")))
    }

    fun disconnect() {
        activeListenerId++
        webSocket?.close(1000, null)
        webSocket = null
        _gameState.value = GameState(connectionStatus = ConnectionStatus.DISCONNECTED)
    }
}
