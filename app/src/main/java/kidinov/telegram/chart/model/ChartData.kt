package kidinov.telegram.chart.model

import kotlinx.serialization.Serializable

@Serializable
data class ChartData(
    val columns: List<List<String>>,
    val colors: Map<String, String>,
    val names: Map<String, String>,
    val types: Map<String, Type>
)

enum class Type {
    line, x
}