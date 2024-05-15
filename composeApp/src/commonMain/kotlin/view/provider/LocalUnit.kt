package view.provider

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class MeasureUnit(val cm: Dp = 0.dp)

val LocalMeasureUnit = compositionLocalOf { MeasureUnit() }
