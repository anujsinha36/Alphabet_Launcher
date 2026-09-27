package com.example.alphabet_launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alphabet_launcher.data.AppInfo
import com.example.alphabet_launcher.ui.theme.TextPrimary

@Composable
fun AppRow(app: AppInfo, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 28.dp, end = 76.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = app.icon.asImageBitmap(),
            contentDescription = app.label,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        Spacer(Modifier.width(22.dp))
        Text(text = app.label, color = TextPrimary, fontSize = 17.sp)
    }
}