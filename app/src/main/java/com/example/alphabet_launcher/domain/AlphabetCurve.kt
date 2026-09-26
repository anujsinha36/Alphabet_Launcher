package com.example.alphabet_launcher.domain

import kotlin.math.exp
import kotlin.math.roundToInt

object AlphabetCurve {

    // Peak horizontal shift as a fraction of the screen width.
    const val MAX_SHIFT_FRACTION = 0.25f

    // Falloff width in letter pitches. Larger = a longer, gentler curve.
    const val FALLOFF_SIGMA = 5f

    /**
     * Horizontal shift for an item [distanceInLetters] away from the finger,
     * as a fraction of MAX_SHIFT. Returns 1.0 at the finger and ~0 beyond
     * ~2.5 * FALLOFF_SIGMA letters.
     */
    fun shiftFraction(distanceInLetters: Float): Float {
        val scaled = distanceInLetters / FALLOFF_SIGMA
        return exp(-(scaled * scaled))
    }

    /**
     * Which letter (0 = A, 25 = Z) sits at the finger. The bar measures
     * positions as an offset from the centre of 'A', in letter pitches;
     * rounding picks the nearest letter, clamped to A-Z so the star and the
     * dot at the ends still resolve to A and Z.
     */
    fun letterIndexFor(offsetFromFirstLetterInPitches: Float): Int =
        (offsetFromFirstLetterInPitches + 0.5f).roundToInt().coerceIn(0, LETTER_COUNT - 1)

    const val LETTER_COUNT = 26
}