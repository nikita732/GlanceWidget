package com.example.glanceexample.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.*
import java.time.Duration

class StockAppWidget : GlanceAppWidget() {

    companion object {
        private var job: Job? = null
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        if (job == null) {
            job = startUpdateJob(Duration.ofSeconds(20).toMillis(), context)
        }

        provideContent {
            GlanceTheme {
                GlanceContent()
            }
        }
    }
}

@Composable
fun GlanceContent() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(text = "Stock widget")
        Text(text = "Ticker: ${PriceDataRepo.ticker}")
        Text(text = "Price: ${PriceDataRepo.currentPrice}$")
    }
}

private fun startUpdateJob(
    timeInterval: Long,
    context: Context
): Job {
    return CoroutineScope(Dispatchers.Default).launch {
        while (isActive) {
            PriceDataRepo.update()
            StockAppWidget().updateAll(context)
            delay(timeInterval)
        }
    }
}
