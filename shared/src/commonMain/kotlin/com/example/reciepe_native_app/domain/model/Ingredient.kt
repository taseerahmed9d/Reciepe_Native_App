package com.example.reciepe_native_app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val name: String,
    val measure: String,
)
