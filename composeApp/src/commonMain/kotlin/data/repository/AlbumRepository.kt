package data.repository

import composeApp.AppDatabase
import data.DatabaseDriverFactory
import data.ExcelReader
import data.mapper.StampMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.AlbumModel
import model.DetailsModel
import model.StampModel

class AlbumRepository {
    private val sqlDriver = DatabaseDriverFactory.createDriver()
    private val dbQueries = AppDatabase(sqlDriver).appDatabaseQueries

    private val detailsRepository = DetailsRepository(dbQueries)
    private val stampMapper = StampMapper()

    private val _album: MutableStateFlow<AlbumModel> = MutableStateFlow(AlbumModel.Loading.Database)
    val album: StateFlow<AlbumModel> = _album

    init {
        AppDatabase.Schema.create(sqlDriver)
    }

    suspend fun getAlbum(page: Int) {
        val currentAlbum = _album.value
        val stamps =
            if (currentAlbum is AlbumModel.Data) currentAlbum.stamps else initialiseAlbumDb()
        _album.value = AlbumModel.Loading.RemoteData(0)
        var index = 0
        val stampsWithSize = stamps.map { stamp ->
            if (shouldLoadDetails(stamp, page)) {
                val stampsCountInPage = stamps.count { shouldLoadDetails(it, page) }
                println("HUGO $index / $stampsCountInPage")
                _album.value = AlbumModel.Loading.RemoteData(index * 100 / stampsCountInPage)
                val details = detailsRepository.getDetails(stamp)
                index++
                stamp.copy(details = details)
            } else {
                stamp
            }
        }
        _album.value = AlbumModel.Data(stamps = stampsWithSize, currentPage = page)
    }

    fun onStampSelected(stamp: StampModel) {
        val currentAlbum = _album.value
        if (currentAlbum is AlbumModel.Data) {
            val updatedStamps = currentAlbum.stamps.map {
                it.copy(selected = if (it == stamp) !it.selected else false)
            }
            _album.value = currentAlbum.copy(stamps = updatedStamps)
        }
    }

    private fun initialiseAlbumDb(): List<StampModel> {
        if (dbQueries.selectAllStamps().executeAsList().isEmpty()) {
            val stamps = ExcelReader().read()
            stamps.forEach { (stamp, position) ->
                dbQueries.insertPosition(
                    page = position.page,
                    row = position.row,
                    column = position.column,
                )
                val positionId = dbQueries.selectLastPositionId().executeAsOne()
                dbQueries.insertStamps(
                    id = stamp.id,
                    tellier = stamp.tellier,
                    image = stamp.image,
                    title = stamp.title,
                    year = stamp.year,
                    quotation = stamp.quotation,
                    positionId = positionId,
                    link = stamp.link,
                    selected = stamp.selected,
                )
            }

        }
        return dbQueries.selectAllStamps().executeAsList().map { dbStamp ->
            val dbPosition = dbQueries.selectPositionById(dbStamp.positionId).executeAsOne()
            val dbDetails = dbQueries.selectDetailsById(dbStamp.id).executeAsOneOrNull()
            stampMapper.toStampModel(dbStamp, dbPosition, dbDetails)
        }
    }

    private fun shouldLoadDetails(stamp: StampModel, page: Int): Boolean =
        stamp.position.page - 1 == page && stamp.details !is DetailsModel.Data
}