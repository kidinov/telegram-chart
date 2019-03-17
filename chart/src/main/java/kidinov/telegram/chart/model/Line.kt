package kidinov.telegram.chart.model

import java.util.SortedMap

data class Line(
    val coordinates: SortedMap<Long, Long>,
    val name: String,
    val color: Int,
    var toRender: Boolean = true,

    // canvas coordinates
    var coordinatesArea: SortedMap<Long, Long> = coordinates.toSortedMap()
)