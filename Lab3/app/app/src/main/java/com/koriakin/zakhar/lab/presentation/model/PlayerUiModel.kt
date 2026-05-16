package com.koriakin.zakhar.lab.presentation.model

data class PlayerUiModel(
    val id: String,
    val rank: Int,
    val name: String,
    val score: Int,
    val isMe: Boolean
)
