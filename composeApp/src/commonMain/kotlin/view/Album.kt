package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import model.AlbumModel
import model.StampModel
import model.ValueToShow
import view.provider.LocalMeasureUnit
import view.provider.MeasureUnit

@Composable
fun Album(
    albumModel: AlbumModel,
    valueToShow: ValueToShow,
    onChangePage: (Int) -> Unit,
    onStampSelected: (StampModel) -> Unit,
) {
    when (albumModel) {
        is AlbumModel.Loading -> AlbumLoading(albumModel)
        is AlbumModel.Data -> AlbumData(albumModel, valueToShow, onChangePage, onStampSelected)
    }
}

@Composable
private fun AlbumLoading(albumModel: AlbumModel.Loading) {
    val message = when (albumModel) {
        is AlbumModel.Loading.Database -> "Transfert de votre collection Excel en BDD locale"
        is AlbumModel.Loading.RemoteData -> "Récupération des données de timbre \n ${albumModel.percent} %"
    }
    Loading(
        message = message,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(ratio = 20f / 30f),
    )
}

@Composable
private fun AlbumData(
    albumModel: AlbumModel.Data,
    valueToShow: ValueToShow,
    onChangePage: (Int) -> Unit,
    onStampSelected: (StampModel) -> Unit,
) {
    var unitCm by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    CompositionLocalProvider(LocalMeasureUnit provides MeasureUnit(cm = unitCm)) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .aspectRatio(ratio = 20f / 30f)
                .background(color = backgroundColor)
                .onSizeChanged { size ->
                    unitCm = with(density) { size.width.toDp() } / 20f
                }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = smallPadding * unitCm,
                        top = topPadding * unitCm,
                        end = smallPadding * unitCm,
                        bottom = smallPadding * unitCm,
                    )
            ) {
                List(9) { index ->
                    StampRow(
                        stamps = albumModel.stamps
                            .filter { it.position.page == albumModel.currentPage + 1 && it.position.row == index + 1 }
                            .sortedBy { it.position.col },
                        valueToShow = valueToShow,
                        onStampSelected = onStampSelected,
                    )
                }
            }
            Pager(
                albumModel = albumModel,
                onChangePage = onChangePage,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            )
        }
    }
}

private const val smallPadding = 0.7f
private const val topPadding = 3.3f
private val backgroundColor = Color(0xFF232323)
