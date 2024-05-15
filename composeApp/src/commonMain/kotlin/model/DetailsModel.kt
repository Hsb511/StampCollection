package model

sealed class DetailsModel {
    data object Unselected: DetailsModel()
    data object Loading: DetailsModel()
    data class Error(val message: String): DetailsModel()
    data class Data(
        val philatelix: String,
        val michel: String,
        val tellier: String,
        val sizeMM: SizeMM,
        val printingMethod: String,
        val drawer: String,
        val image: ImageModel,
    ): DetailsModel()
}
