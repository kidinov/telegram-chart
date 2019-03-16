package kidinov.telegram.chart.model

data class Button(
    val lineName: String,
    val cx: Float,
    val cy: Float,
    val rad: Int,
    val color: Int,
    var checked: Boolean
)