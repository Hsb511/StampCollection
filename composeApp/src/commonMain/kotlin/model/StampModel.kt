package model

data class StampModel(
    val index: Long,
    val id: String,
    val image: ImageModel,
    val year: Int,
    val quotation: Float?,
    val position: PositionModel,
    val link: String,
    val selected: Boolean = false,
    val details: DetailsModel = DetailsModel.Loading,
) {
    override fun equals(other: Any?): Boolean = other is StampModel &&
            index == other.index && id == other.id && year == other.year &&
            quotation == other.quotation && position == other.position && link == other.link &&
            selected == other.selected
}
