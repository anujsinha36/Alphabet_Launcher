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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alphabet_launcher.data.AppInfo
import com.example.alphabet_launcher.ui.components.AppRow
import com.example.alphabet_launcher.ui.theme.TextPrimary
import com.example.alphabet_launcher.ui.theme.TextSecondary

@Composable
fun LetterContent(
    letter: Char,
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(Modifier.height(150.dp))
        Text(
            text = letter.toString(),
            modifier = Modifier.padding(start = 28.dp),
            color = TextPrimary,
            fontSize = 38.sp,
            fontWeight = FontWeight.Normal,
        )
        Spacer(Modifier.height(16.dp))
        if (apps.isEmpty()) {
            Text(
                text = "No Apps",
                modifier = Modifier.padding(start = 28.dp),
                color = TextSecondary,
                fontSize = 18.sp,
            )
        } else {
            LazyColumn {
                items(apps, key = { it.packageName }) { app ->
                    AppRow(app = app, onClick = { onAppClick(app) })
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun LetterContentPreview() {
    val sampleBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    val sampleApps = listOf(
        AppInfo(label = "Calculator", packageName = "com.android.calculator2", icon = sampleBitmap),
        AppInfo(label = "Calendar", packageName = "com.android.calendar", icon = sampleBitmap),
        AppInfo(label = "Camera", packageName = "com.android.camera", icon = sampleBitmap),
        AppInfo(label = "Chrome", packageName = "com.android.chrome", icon = sampleBitmap),
        AppInfo(label = "Clock", packageName = "com.android.deskclock", icon = sampleBitmap)
    )

    LetterContent(
        letter = 'C',
        apps = sampleApps,
        onAppClick = {}
    )
}
