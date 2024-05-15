package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.DetailsModel
import view.provider.LocalScreenSize

@Composable
fun Details(detailsModel: DetailsModel) {
    Box(modifier = Modifier.padding(16.dp)) {
        when (detailsModel) {
            is DetailsModel.Unselected -> Unit
            is DetailsModel.Error -> Text(text = detailsModel.message)
            is DetailsModel.Loading -> CircularProgressIndicator(modifier = Modifier.size(69.dp).align(Alignment.Center))
            is DetailsModel.Data -> DetailsData(detailsModel)
        }
    }
}

@Composable
private fun DetailsData(detailsData: DetailsModel.Data) {

    DynamicContainer {
        val screenSize = LocalScreenSize.current
        val imageModifier = if (screenSize.width / screenSize.height > 2f / 3f) {
            Modifier.height(169.dp)
        } else {
            Modifier.width(169.dp)
        }

        Image(
            model = detailsData.image,
            modifier = imageModifier
                .padding(bottom = 16.dp, end = 16.dp)
        )

        Column {
            Text(text = "Philatelix : ${detailsData.philatelix}")
            Text(text = "Michel : ${detailsData.michel}")
            Text(text = "Tellier : ${detailsData.tellier}")
            Text(text = "largeur x hauteur: ${detailsData.sizeMM.width} x ${detailsData.sizeMM.height} mm")
            Text(text = "Impression : ${detailsData.printingMethod}")
            Text(text = "Dessinateur : ${detailsData.drawer}")
        }
    }
}
