package data.mapper

import composeApp.DetailsDbModel
import composeApp.PositionDbModel
import composeApp.StampDbModel
import data.extension.toBoolean
import model.StampModel

class StampMapper {

    private val imageMapper = ImageMapper()
    private val positionMapper = PositionMapper()
    private val detailsMapper = DetailsMapper()

    fun toStampModel(
        dbStamp: StampDbModel,
        positionDbModel: PositionDbModel,
        detailsDbModel: DetailsDbModel? = null,
    ): StampModel = StampModel(
        index = dbStamp.id,
        id = dbStamp.tellier,
        image = imageMapper.toImageModel(dbStamp.image, dbStamp.title),
        year = dbStamp.year.toInt(),
        quotation = dbStamp.quotation?.toFloat(),
        position = positionMapper.toPositionModel(positionDbModel),
        link = dbStamp.link,
        selected = dbStamp.selected.toBoolean(),
        details = detailsMapper.toDetailsModel(detailsDbModel),
    )
}
