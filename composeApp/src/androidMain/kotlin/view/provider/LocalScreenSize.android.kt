package view.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenSize(): ScreenSize {
    val localConfiguration = LocalConfiguration.current
    return ScreenSize(
        width = localConfiguration.screenWidthDp.dp,
        height = localConfiguration.screenHeightDp.dp,
    )
}
