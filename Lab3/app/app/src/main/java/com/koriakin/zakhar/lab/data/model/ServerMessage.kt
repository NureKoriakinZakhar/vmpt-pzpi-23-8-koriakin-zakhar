package com.koriakin.zakhar.lab.data.model

import com.google.gson.annotations.SerializedName

data class ServerMessage(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String? = null,
    @SerializedName("players") val players: List<PlayerDto>? = null
)
