package view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import model.DetailsModel
import model.StampModel
import model.ValueToShow
import view.provider.LocalMeasureUnit

@Composable
fun Stamp(
    model: StampModel,
    valueToShow: ValueToShow,
    onStampSelected: (StampModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val offset = animateDpAsState(targetValue = if (model.selected) (-23).dp else 0.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset(y = offset.value)
            .clickable {
                onStampSelected(model)
            },
    ) {
        Image(
            model = model.image,
            modifier = model.details.toSizeModifier(),
        )
        ValueToShow(valueToShow = valueToShow, stamp = model)
    }
}

@Composable
fun ValueToShow(valueToShow: ValueToShow, stamp: StampModel) {
    val label = when (valueToShow) {
        ValueToShow.Id -> stamp.id
        ValueToShow.Year -> stamp.year.toString()
        ValueToShow.Quotation -> "${stamp.quotation} €"
        ValueToShow.None -> null
    }
    label?.let {
        Text(
            text = label,
            modifier = Modifier.background(Color.LightGray.copy(alpha = 0.23f))
        )
    }
}

@Composable
private fun DetailsModel.toSizeModifier(): Modifier = when(this) {
    is DetailsModel.Data -> {
        val unitCm = LocalMeasureUnit.current.cm
        Modifier.size(
            width = this.sizeMM.width / 10 * unitCm,
            height = this.sizeMM.height / 10 * unitCm,
        )
    }
    else -> Modifier
}
