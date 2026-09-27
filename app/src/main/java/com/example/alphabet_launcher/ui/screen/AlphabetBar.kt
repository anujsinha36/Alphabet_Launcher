package com.example.alphabet_launcher.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alphabet_launcher.domain.AlphabetCurve
import com.example.alphabet_launcher.ui.theme.BubbleBackground
import com.example.alphabet_launcher.ui.theme.TextPrimary
import kotlinx.coroutines.flow.distinctUntilChanged
private const val ITEM_COUNT = 28
private val VERTICAL_PAD = 32.dp
private val BUBBLE_SIZE = 52.dp
private val BUBBLE_GAP = 6.dp


@Composable
fun AlphabetBar(
    maxShiftPx: Float,
    onSelectedLetterChange: (Char?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var touchY by remember { mutableFloatStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    var barHeightPx by remember { mutableIntStateOf(0) }
    var barWidthPx by remember { mutableIntStateOf(0) }
    var currentLetter by remember { mutableStateOf<Char?>(null) }

    val curveAmount = remember { Animatable(0f) }
    val density = LocalDensity.current
    val verticalPadPx = with(density) { VERTICAL_PAD.toPx() }
    val bubbleSizePx = with(density) { BUBBLE_SIZE.toPx() }
    val bubbleGapPx = with(density) { BUBBLE_GAP.toPx() }

    val textMeasurer = rememberTextMeasurer(cacheSize = ITEM_COUNT)
    val itemLabels = remember { listOf("☆") + ('A'..'Z').map(Char::toString) + listOf("○") }
    val itemLayouts = remember(textMeasurer) {
        val style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        itemLabels.map { label -> textMeasurer.measure(label, style) }
    }

    // Report the letter under the finger upward, only when it changes.
    LaunchedEffect(barHeightPx) {
        snapshotFlow {
            if (dragging && barHeightPx > 0) {
                letterIndexForTouch(touchY, barHeightPx.toFloat(), verticalPadPx)
            } else {
                null
            }
        }
            .distinctUntilChanged()
            .collect { index ->
                currentLetter = index?.let { 'A' + it }
                onSelectedLetterChange(currentLetter)
            }
    }

    // Bend in quickly on touch, straighten out after release.
    LaunchedEffect(dragging) {
        val target = if (dragging) 1f else 0f
        val duration = if (dragging) 150 else 250
        curveAmount.animateTo(target, tween(duration, easing = FastOutSlowInEasing))
    }

    Box(
        modifier
            .onSizeChanged {
                barHeightPx = it.height
                barWidthPx = it.width
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    down.consume()
                    touchY = down.position.y
                    dragging = true
                    try {
                        drag(down.id) { change ->
                            touchY = change.position.y
                            change.consume()
                        }
                    } finally {
                        dragging = false
                    }
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            if (size.height <= 0f) return@Canvas
            val pitch = (size.height - 2 * verticalPadPx) / (ITEM_COUNT - 1)
            val amount = curveAmount.value
            val centerX = size.width / 2f

            itemLayouts.forEachIndexed { index, layout ->
                val itemCenterY = verticalPadPx + index * pitch
                val distanceInPitches = (itemCenterY - touchY) / pitch
                val shift = maxShiftPx * AlphabetCurve.shiftFraction(distanceInPitches) * amount
                drawText(
                    textLayoutResult = layout,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = centerX - layout.size.width / 2f - shift,
                        y = itemCenterY - layout.size.height / 2f,
                    ),
                )
            }
        }

        AnimatedVisibility(
            visible = dragging,
            enter = fadeIn(tween(90)),
            exit = fadeOut(tween(140)),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Box(
                Modifier
                    .graphicsLayer {
                        // Sit just left of the curve's deepest point: the letter
                        // under the finger is shifted by the full maxShift.
                        translationX = barWidthPx / 2f - maxShiftPx - bubbleGapPx - bubbleSizePx
                        translationY = (touchY - bubbleSizePx / 2f)
                            .coerceIn(0f, barHeightPx - bubbleSizePx)
                    }
                    .size(BUBBLE_SIZE)
                    .clip(CircleShape)
                    .background(BubbleBackground),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = (currentLetter ?: 'A').toString(),
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

/**
 * Maps a touch position to a letter index (0 = A), or null when the finger
 * is not on the bar. Positions are measured as an offset from the centre of
 * 'A' in letter pitches; rounding picks the nearest letter and clamping makes
 * the star and dot zones resolve to A and Z.
 */
private fun letterIndexForTouch(touchY: Float, barHeightPx: Float, verticalPadPx: Float): Int {
    val pitch = (barHeightPx - 2 * verticalPadPx) / (ITEM_COUNT - 1)
    val offsetFromFirstLetterInPitches = (touchY - (verticalPadPx + pitch)) / pitch
    return AlphabetCurve.letterIndexFor(offsetFromFirstLetterInPitches)
}
