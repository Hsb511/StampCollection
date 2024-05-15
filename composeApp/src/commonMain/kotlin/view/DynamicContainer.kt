package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import view.provider.LocalScreenSize

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DynamicContainer(content: @Composable () -> Unit) {
    val screenSize = LocalScreenSize.current
    if (screenSize.width / screenSize.height > 2f / 3f) {
        Column { content() }
    } else {
        FlowRow { content() }
    }
}
