package data.repository

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import composeApp.AppDatabaseQueries
import data.HttpClientFactory
import data.mapper.DetailsMapper
import data.provideHttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import model.DetailsModel
import model.ImageModel
import model.SizeMM
import model.StampModel

class DetailsRepository(private val dbQueries: AppDatabaseQueries) {

    private val detailsMapper = DetailsMapper()
    private val httpClientFactory = HttpClientFactory()
    private val client = provideHttpClient(httpClientFactory)

    suspend fun getDetails(stamp: StampModel): DetailsModel = runCatching {
        if (dbQueries.selectDetailsById(stamp.index).executeAsOneOrNull() == null) {
            val bodyDocument = Ksoup.parse(html = client.get(stamp.link).bodyAsText())
            val (widthMM, heightMM) = bodyDocument.findInfoByLabel("Format max").toSizeMM()
            val (imageUrl, contentDescription) = bodyDocument.findImage()
            dbQueries.insertDetails(
                stampId = stamp.index,
                philatelix = bodyDocument.findInfoByLabel("Philatelix"),
                michel = bodyDocument.findInfoByLabel("Michel"),
                tellier = bodyDocument.findInfoByLabel("Tellier"),
                widthMM = widthMM.toString(),
                heightMM = heightMM.toString(),
                printingMethod = bodyDocument.findInfoByLabel("Impression"),
                drawer = bodyDocument.findInfoByLabel("Dessinateur"),
                imageUrl = imageUrl,
                imageContentDescription = contentDescription,

            )
        }
        detailsMapper.toDetailsModel(dbQueries.selectDetailsById(stamp.index).executeAsOne())
    }.getOrElse { throwable ->
        DetailsModel.Error(throwable.message ?: "Unknown error")
    }
}

private fun Document.findInfoByLabel(label: String): String = this
    .select("ul.mx-small")
    .select("li")
    .toList()
    .firstOrNull { it.select("span.timInfoLabel").text() == label }
    ?.select("span.timInfo")
    ?.text() ?: ""

private fun String.toSizeMM(): SizeMM {
    val (width, height) = this.split(" x ")
    return SizeMM(
        width = width.toInt(),
        height = height.split(" ").first().toInt(),
    )
}

private fun Document.findImage(): Pair<String, String> {
    val imgElement = this.select("a.mx-zoom").select("img")
    return Pair(imgElement.attr("alt"), imgElement.attr("src"),)
}
