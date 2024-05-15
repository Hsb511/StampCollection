package data.mapper

import composeApp.DetailsDbModel
import model.DetailsModel
import model.SizeMM

class DetailsMapper {

    private val imageMapper = ImageMapper()

    fun toDetailsModel(dbModel: DetailsDbModel?): DetailsModel = dbModel?.let {
        DetailsModel.Data(
            philatelix = dbModel.philatelix,
            michel = dbModel.michel,
            tellier = dbModel.tellier,
            sizeMM = SizeMM(width = 0, height = 0),
            printingMethod = dbModel.printingMethod,
            drawer = dbModel.drawer,
            image = imageMapper.toImageModel(dbModel.imageUrl, dbModel.imageContentDescription),
        )
    } ?: DetailsModel.Unselected
}