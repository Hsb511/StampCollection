package data

import composeApp.PositionDbModel
import composeApp.StampDbModel
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory

actual class ExcelReader {
    actual fun read(): List<Pair<StampDbModel, PositionDbModel>> {
        val inputStream = this::class.java.getResourceAsStream("../collection.xls")
        val workbook = WorkbookFactory.create(inputStream)
        val workSheet = workbook.getSheetAt(0)
        val images = workbook.allPictures
        return (1..workSheet.lastRowNum)
            .map { rowNum -> workSheet.getRow(rowNum) }
            .map { row ->
                StampDbModel(
                    id = row.rowNum - 1L,
                    tellier = mapId(row.getCell(2)),
                    image = images[row.rowNum - 1].data,
                    title = row.getCell(1).stringCellValue,
                    year = row.getCell(5).numericCellValue.toLong(),
                    quotation = row.getCell(4).numericCellValue.toFloat().toString(),
                    positionId = -1L,
                    link = row.getCell(11).hyperlink.address,
                    selected = 0,
                ) to mapPosition(row.getCell(10).stringCellValue)
            }
    }

    private fun mapId(cell: Cell): String = when (cell.cellType) {
        CellType.NUMERIC -> cell.numericCellValue.toInt().toString()
        CellType.STRING -> cell.stringCellValue
        else -> throw IllegalArgumentException("Not a proper cell type")
    }

    private fun mapPosition(rawCellContent: String): PositionDbModel {
        val (rawPage, rawRow, rawCol) = rawCellContent.split(", ")
        return PositionDbModel(
            id = -1,
            page = cleanRawValue(rawPage, "P"),
            row = cleanRawValue(rawRow, "L"),
            column = cleanRawValue(rawCol, "C"),
        )
    }

    private fun cleanRawValue(rawValue: String, delimiter: String): Long =
        rawValue.split(delimiter).last().toLong()
}
