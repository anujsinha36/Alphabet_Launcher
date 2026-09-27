package com.example.alphabet_launcher.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alphabet_launcher.data.AppInfo
import com.example.alphabet_launcher.ui.components.AppRow
import com.example.alphabet_launcher.ui.theme.TextPrimary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeContent(
    favourites: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(Modifier.height(130.dp))
        ClockHeader(Modifier.padding(start = 28.dp))
        Spacer(Modifier.height(24.dp))
        LazyColumn {
            items(favourites, key = { it.packageName }) { app ->
                AppRow(app = app, onClick = { onAppClick(app) })
            }
        }
    }
}

@Composable
private fun ClockHeader(modifier: Modifier = Modifier) {
    var now by remember { mutableStateOf(Date()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("EEE d MMM", Locale.getDefault()) }

    // Refresh on the next minute boundary, forever while this screen is shown.
    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            val calendar = Calendar.getInstance()
            val delayMs = (60 - calendar.get(Calendar.SECOND)) * 1000L -
                    calendar.get(Calendar.MILLISECOND)
            delay(delayMs)
        }
    }

    Column(modifier = modifier) {
        Text(
            text = timeFormat.format(now),
            color = TextPrimary,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = dateFormat.format(now),
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun HomeContentPreview() {
    val sampleBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    val dummyFavourites = listOf(
        AppInfo(label = "Phone", packageName = "com.android.phone", icon = sampleBitmap),
        AppInfo(label = "Messages", packageName = "com.android.mms", icon = sampleBitmap),
        AppInfo(label = "Chrome", packageName = "com.android.chrome", icon = sampleBitmap),
        AppInfo(label = "Camera", packageName = "com.android.camera", icon = sampleBitmap),
        AppInfo(label = "Clock", packageName = "com.android.deskclock", icon = sampleBitmap)
    )

    HomeContent(
        favourites = dummyFavourites,
        onAppClick = {}
    )
}