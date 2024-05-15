package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.DetailsModel
import model.ImageModel

@Composable
fun Image(model: ImageModel, modifier: Modifier = Modifier) {
    when(model) {
        is ImageModel.Local -> Image(
            bitmap = model.bitmap,
            contentDescription = model.contentDescription,
            modifier = modifier,
        )
        is ImageModel.Remote -> KamelImage(
            resource = asyncPainterResource(model.uri),
            contentDescription = model.contentDescription,
            onLoading = { percent ->
                Loading("${(percent * 100).toInt()} %")
            },
            modifier = modifier,
        )
    }
}
