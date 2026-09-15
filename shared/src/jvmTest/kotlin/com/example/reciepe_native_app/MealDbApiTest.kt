package com.example.reciepe_native_app

import com.example.reciepe_native_app.data.remote.MealDbApi
import com.example.reciepe_native_app.data.remote.createHttpClient
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class MealDbApiTest {
    @Test
    fun searchWithEmptyQueryReturnsMeals() = runBlocking {
        val api = MealDbApi(createHttpClient())
        val meals = api.searchMeals("")
        assertTrue(meals.isNotEmpty(), "TheMealDB empty search should return a default meal list")
    }
}
