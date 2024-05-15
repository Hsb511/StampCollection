package view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import model.ValueToShow

@Composable
fun ValueSelectionRow(
    selectedValue: ValueToShow,
    onValueToShow: (ValueToShow) -> Unit,
) {
    DynamicContainer {
        ValueToShow.entries.map { valueToShow ->
            ValueSelection(
                valueToShow = valueToShow,
                selectedValue = selectedValue,
                onValueToShow = onValueToShow
            )
        }
    }
}

@Composable
private fun ValueSelection(
    selectedValue: ValueToShow,
    valueToShow: ValueToShow,
    onValueToShow: (ValueToShow) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedValue == valueToShow,
            onClick = { onValueToShow(valueToShow) },
        )
        Text(text = valueToShow.label)
    }
}
