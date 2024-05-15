package model

sealed class AlbumModel {
    sealed class Loading : AlbumModel() {
        data object Database: Loading()
        data class RemoteData(val percent: Int): Loading()
    }

    data class Data(
        val stamps: List<StampModel>,
        val currentPage: Int = 0,
    ) : AlbumModel()
}
