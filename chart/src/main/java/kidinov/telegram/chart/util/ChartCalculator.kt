package kidinov.telegram.chart.util

import kidinov.telegram.chart.model.Line
import kidinov.telegram.chart.model.NavigationControl

class ChartCalculator(private val placementCalculator: PlacementCalculator) {
    fun calcXMinMax(coordinates: List<Map<Long, Long>>): Pair<Long, Long> {
        if (coordinates.isEmpty()) return 0L to 0L

        var min = coordinates.first().keys.first()
        var max = coordinates.first().keys.first()
        coordinates.forEach {
            it.forEach { entry ->
                if (entry.key < min) min = entry.key
                if (entry.key > max) max = entry.key
            }
        }
        return min to max
    }

    fun calcYMinMax(coordinates: List<Map<Long, Long>>): Pair<Long, Long> {
        if (coordinates.isEmpty()) return 0L to 0L

        var min = coordinates.first().values.first()
        var max = coordinates.first().values.first()
        coordinates.forEach {
            it.forEach { entry ->
                if (entry.value < min) min = entry.value
                if (entry.value > max) max = entry.value
            }
        }
        return min to max
    }

    fun setRangeToShow(line: Line, navControl: NavigationControl) {
        var propLeft = navControl.left / navControl.viewRight
        var propRight = navControl.right / navControl.viewRight

        if (propLeft < 0F) propLeft = 0F
        if (propRight > 1F) propRight = 1F

        line.coordinatesArea.clear()

        line.coordinatesArea = line.coordinates.toList().subList(
            (propLeft * line.coordinates.size).toInt(),
            (propRight * line.coordinates.size).toInt()
        ).toMap().toSortedMap()
    }
}