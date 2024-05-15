package viewmodel

import data.repository.AlbumRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AlbumModel
import model.DetailsModel
import model.StampModel
import model.ValueToShow

class AlbumViewModel {

    private val albumRepository = AlbumRepository()
    private val job = Job()
    private val viewModelScope = CoroutineScope(job)
    private val _valueToShow: MutableStateFlow<ValueToShow> = MutableStateFlow(ValueToShow.None)
    val album: StateFlow<AlbumModel> = albumRepository.album
    val valueToShow: StateFlow<ValueToShow> = _valueToShow
    val details: StateFlow<DetailsModel> = album
        .map(::mapToDetails)
        .stateIn(viewModelScope, SharingStarted.Eagerly, DetailsModel.Unselected)

    init {
        onLoadPage(0)
    }

    fun onValueToShow(valueToShow: ValueToShow) {
        _valueToShow.value = valueToShow
    }

    fun onStampSelected(stamp: StampModel) {
        albumRepository.onStampSelected(stamp)
    }

    fun onLoadPage(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getAlbum(page)
        }
    }

    private fun mapToDetails(album: AlbumModel): DetailsModel =
        (album as? AlbumModel.Data)?.let { albumData ->
            albumData.stamps.firstOrNull { it.selected }?.details
        } ?: DetailsModel.Unselected
}
