package data.mapper

import data.extension.toImageBitmap
import model.ImageModel

class ImageMapper {

    fun toImageModel(data: ByteArray, contentDescription: String): ImageModel = ImageModel.Local(
        bitmap = data.toImageBitmap(),
        contentDescription = contentDescription,
    )

    fun toImageModel(url: String, contentDescription: String): ImageModel = ImageModel.Remote(
        uri = url,
        contentDescription = contentDescription,
    )
}
