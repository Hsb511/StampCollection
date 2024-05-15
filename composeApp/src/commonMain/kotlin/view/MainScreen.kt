package view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import model.AlbumModel
import model.DetailsModel
import model.ValueToShow
import view.provider.LocalScreenSize
import view.provider.getScreenSize
import viewmodel.AlbumViewModel

@Composable
fun MainScreen() {
    MaterialTheme {
        val albumVM = AlbumViewModel()
        val coroutineScope = rememberCoroutineScope()

        val album: MutableState<AlbumModel> = remember { mutableStateOf(AlbumModel.Loading.Database) }
        val valueToShow: MutableState<ValueToShow> = remember { mutableStateOf(ValueToShow.None) }
        val details: MutableState<DetailsModel> = remember { mutableStateOf(DetailsModel.Unselected) }
        coroutineScope.launch {
            albumVM.details.collect { details.value = it }
        }
        coroutineScope.launch {
            albumVM.album.collect { album.value = it }
        }
        coroutineScope.launch {
            albumVM.valueToShow.collect { valueToShow.value = it }
        }

        MainScreenContainer {
            Album(
                albumModel = album.value,
                valueToShow = valueToShow.value,
                onChangePage = albumVM::onLoadPage,
                onStampSelected = albumVM::onStampSelected,
            )
            Column {
                ValueSelectionRow(
                    selectedValue = valueToShow.value,
                    onValueToShow = albumVM::onValueToShow,
                )
                Details(detailsModel = details.value)
            }
        }
    }
}

@Composable
private fun MainScreenContainer(content: @Composable () -> Unit) {
    val screenSize = getScreenSize()
    CompositionLocalProvider(LocalScreenSize provides screenSize) {
        val scrollState = rememberScrollState()
        if (screenSize.width / screenSize.height < 2f / 3f) {
            Column(modifier = Modifier.verticalScroll(scrollState)) { content() }
        } else {
            Row(modifier = Modifier.horizontalScroll(scrollState)) { content() }
        }
    }
}
