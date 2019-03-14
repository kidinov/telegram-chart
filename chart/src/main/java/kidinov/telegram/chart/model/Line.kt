package kidinov.telegram.chart.model

import java.util.SortedMap

data class Line(
    val coordinates: SortedMap<Long, Long>,
    val name: String,
    val color: Int
)