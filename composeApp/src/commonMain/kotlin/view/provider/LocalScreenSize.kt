package view.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenSize(val width: Dp = 0.dp, val height: Dp = 0.dp)

val LocalScreenSize = compositionLocalOf { ScreenSize() }

@Composable expect fun getScreenSize(): ScreenSize
