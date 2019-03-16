package kidinov.telegram.chart.util

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

}