package data

import model.StampModel

actual class ExcelReader {
    actual fun read(): List<StampModel> {
        /* val inputStream = this::class.java.getResourceAsStream("collection.xls")
        val workbook = WorkbookFactory.create(inputStream)
        val workSheet = workbook.getSheetAt(0) */
        throw IllegalArgumentException("")
    }
}