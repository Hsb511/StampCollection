package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import model.StampModel
import model.ValueToShow
import view.provider.LocalMeasureUnit

@Composable
fun StampRow(
    stamps: List<StampModel>,
    valueToShow: ValueToShow,
    onStampSelected: (StampModel) -> Unit,
) {
    val unitCm = LocalMeasureUnit.current.cm

    Column {
        Box {
            StampList(
                stamps = stamps,
                valueToShow = valueToShow,
                onStampSelected = onStampSelected,
            )
            Surface(
                color = overlayColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(stampRowHeight * unitCm)
                    .align(Alignment.BottomCenter)
            ) { }
        }
        Divider(
            thickness = darkLineHeight * unitCm,
            color = darkLineColor,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomMargin * unitCm)
                .background(overlayColor)
        )
    }
}

@Composable
private fun StampList(
    stamps: List<StampModel>,
    valueToShow: ValueToShow,
    onStampSelected: (StampModel) -> Unit,
) {
    val density = LocalDensity.current
    val rowWidth = remember { mutableStateOf(0.dp) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                rowWidth.value = with(density) { size.width.toDp() }
            }
    ) {
        stamps.maxOfOrNull { it.position.col }?.let { colCount ->
            List(colCount) { index ->
                val stamp = stamps.firstOrNull { it.position.col - 1 == index }
                if (stamp != null) {
                    val widthModifier = Modifier.width((rowWidth.value) / colCount - 2.dp)
                    Stamp(stamp, valueToShow, onStampSelected, widthModifier)
                } else {
                    Spacer(modifier = Modifier.weight(weight = 1f / 9, fill = false))
                }
            }
        }
    }
}

private const val bottomMargin = 0.15f
private const val stampRowHeight = 1.8f
private const val darkLineHeight = 0.1f
private val overlayColor = Color(0x46929292)
private val darkLineColor = Color(0xFF111111)
