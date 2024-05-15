package data.mapper

import composeApp.PositionDbModel
import model.PositionModel

class PositionMapper {

    fun toPositionModel(dbModel: PositionDbModel): PositionModel = PositionModel(
        page = dbModel.page.toInt(),
        row = dbModel.row.toInt(),
        col = dbModel.column.toInt(),
    )
}
