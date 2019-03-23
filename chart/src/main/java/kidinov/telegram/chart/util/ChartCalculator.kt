package kidinov.telegram.chart.util

import kidinov.telegram.chart.model.Line
import kidinov.telegram.chart.model.NavigationControl

class ChartCalculator {
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
        var propLeft = navControl.left / navControl.width
        var propRight = navControl.right / navControl.width

        if (propLeft < 0F) propLeft = 0F
        if (propRight > 1F) propRight = 1F

        line.coordinatesArea.clear()

        val leftIndex = propLeft * line.coordinates.size
        val rightIndex = propRight * line.coordinates.size

        val listOfCoordinates = line.coordinates.toList()

        val subList =
            listOfCoordinates.subList(leftIndex.toInt(), rightIndex.toInt()).toMutableList()

        val leftBorder = if (subList.size >= 2)
            calcBorder(leftIndex - leftIndex.toInt(), 0, subList) else
            subList[0]

        val rightBorder = if (subList.size >= 2)
            calcBorder(rightIndex - rightIndex.toInt(), subList.lastIndex - 1, subList)
        else subList[subList.lastIndex]

        subList[0] = leftBorder
        subList[subList.lastIndex] = rightBorder

        line.coordinatesArea = subList.toMap().toSortedMap()
    }

    private fun calcBorder(
        prop: Float,
        index: Int,
        subList: List<Pair<Long, Long>>
    ): Pair<Long, Long> {
        val x1 = subList[index].first
        val y1 = subList[index].second
        val x2 = subList[index + 1].first
        val y2 = subList[index + 1].second

        val x = (x1 + (x2 - x1).toFloat() * prop).toLong()
        val y = calcYFromOnLine(x1, y1, x2, y2, x)
        return x to y
    }

    private fun calcYFromOnLine(x1: Long, y1: Long, x2: Long, y2: Long, x: Long): Long {
        if (x2 == x1) return (y2 - y1) / 2
        return (y1 + ((y2 - y1)) * (x - x1) / (x2 - x1).toFloat()).toLong()
    }
}