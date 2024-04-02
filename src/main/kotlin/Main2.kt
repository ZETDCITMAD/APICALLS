package com.example.api

import kotlinx.coroutines.*
import kotlin.random.Random

// Simulating fetching weather data for a city
suspend fun fetchWeather(city: String, cache: MutableMap<String, Int>): Int {
    return cache[city] ?: run {
        val temperature = Random.nextInt(-20, 40) // Simulating temperature between -20°C to 40°C
        delay(Random.nextLong(1000, 3000)) // Simulating network delay
        cache[city] = temperature // Cache the fetched temperature
        temperature
    }
}

fun main() {
    val cities = listOf("New York", "London", "Tokyo", "Sydney")
    val temperatureCache = mutableMapOf<String, Int>()

    val job = GlobalScope.launch {
        val temperatures = cities.map { city ->
            async { fetchWeather(city, temperatureCache) }
        }.awaitAll()

        val averageTemperature = temperatures.average()
        println("Average temperature: $averageTemperature°C")

        // Print cached values
        println("Cached values:")
        temperatureCache.forEach { (city, temperature) ->
            println("$city: $temperature°C")
        }
    }

    // Ensure the main function doesn't exit before coroutine finishes
    runBlocking {
        job.join()
    }
}