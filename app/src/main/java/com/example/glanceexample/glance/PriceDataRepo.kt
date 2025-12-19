package com.example.glanceexample.glance

import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PriceDataRepo {
    var ticker = "GOOGL"
    private var previousPrice = 0f
    var change = 0
    var changePercent = 0f
    private var _currentPrice = MutableStateFlow(0f)
    val currentPrice: StateFlow<Float> get() = _currentPrice
    var isInitialized = false

    fun initialize() {
        val initialPrice = Random.nextInt(20, 35) + Random.nextFloat()
        _currentPrice.value = initialPrice
        previousPrice = initialPrice
        change = 0
        changePercent = 0f
        isInitialized = true
    }

    fun update() {
        if (!isInitialized) {
            initialize()
            return
        }

        previousPrice = _currentPrice.value
        val newPrice = Random.nextInt(20, 35) + Random.nextFloat()
        _currentPrice.value = newPrice

        if (previousPrice != 0f) {
            changePercent = ((newPrice - previousPrice) / previousPrice) * 100
        } else {
            changePercent = 0f
        }

        change = when {
            changePercent > 0.01f -> 1
            changePercent < -0.01f -> -1
            else -> 0
        }
    }
}