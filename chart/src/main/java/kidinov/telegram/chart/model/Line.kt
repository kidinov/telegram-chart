package kidinov.telegram.chart.model

data class Line(
    val coordinates: Map<Long, Int>,
    val name: String,
    val color: Int
)