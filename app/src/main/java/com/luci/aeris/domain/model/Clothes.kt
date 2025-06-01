import java.util.ArrayList

data class Clothes(
    val id: String = "",
    val photoPath: String? = null,
    val dateAdded: String = "",
    val type: String = "",
    val suitableWeather: List<String> = mutableListOf()
)
