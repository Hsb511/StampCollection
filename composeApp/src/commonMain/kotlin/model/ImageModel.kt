package model

import androidx.compose.ui.graphics.ImageBitmap

sealed class ImageModel(open val contentDescription: String) {

    data class Local(
        override val contentDescription: String,
        val bitmap: ImageBitmap,
    ) : ImageModel(contentDescription)

    data class Remote(
        override val contentDescription: String,
        val uri: String,
    ) : ImageModel(contentDescription)
}
