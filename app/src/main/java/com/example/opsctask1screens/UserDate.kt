package com.example.opsctask1screens

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class UserDate(
    val id: String? = null,
    val username: String?= null,
    val surname: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirm: String? = null

)

data class BirdSpecies(
    val name: String,
    val description: String,
    val imageUrl: String
)
data class HotspotModel(
    val lat: Double,
    val lng: Double)

