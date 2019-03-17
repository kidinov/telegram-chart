package kidinov.telegram.chart.model

data class NavigationControl(
    var left: Float,
    var right: Float,

    val viewTop: Float,
    val viewBottom: Float,
    val viewRight: Float,
    val viewLeft: Float,
    val width: Float
)