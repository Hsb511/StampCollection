package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.AlbumModel

@Composable
fun Pager(
    albumModel: AlbumModel.Data,
    onChangePage: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        PagerButton(
            isShown = albumModel.currentPage > 0,
            onClick = { onChangePage(albumModel.currentPage - 1) },
            text = "←",
        )

        PagerButton(
            isShown = albumModel.currentPage < albumModel.stamps.maxOf { it.position.page } - 1,
            onClick = { onChangePage(albumModel.currentPage + 1) },
            text = "→",
        )
    }
}

@Composable
private fun PagerButton(
    isShown: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    if (isShown) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
            )
        ) {
            Text(text = text)
        }
    } else {
        Spacer(modifier = Modifier.width(23.dp))
    }
}
