package com.koriakin.zakhar.lab.data.model

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("score") val score: Int
)
