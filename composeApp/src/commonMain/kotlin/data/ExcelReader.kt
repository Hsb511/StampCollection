package data

import composeApp.PositionDbModel
import composeApp.StampDbModel

expect class ExcelReader() {
    fun read(): List<Pair<StampDbModel, PositionDbModel>>
}