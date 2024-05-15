package view.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenSize(): ScreenSize {
    val density = LocalDensity.current
    val containerSize = LocalWindowInfo.current.containerSize

    return ScreenSize(
        width = with(density) { containerSize.width.toDp() },
        height = with(density) { containerSize.height.toDp() },
    )
}
