package kidinov.telegram.chart

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.model.ChartData
import kidinov.telegram.chart.model.Line
import kidinov.telegram.chart.model.Type
import kidinov.telegram.chart.util.px
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : Activity() {
    private val cvChart by lazy { findViewById<ChartView>(R.id.cvChart) }
    private val llButtons by lazy { findViewById<LinearLayout>(R.id.llButtons) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cvChart.post { parseJson() }
    }

    private fun parseJson() {
        val chartRead =
            BufferedReader(InputStreamReader(resources.openRawResource(R.raw.chart_data)))
        val chartData = chartRead.use {
            Json.parse(ChartData.serializer().list, it.readText())
        }

        generateButtons(chartData.map { Chart(chartDataToLines(it)) })
        llButtons.getChildAt(0).callOnClick()
    }

    private fun generateButtons(charts: List<Chart>) {
        charts.forEachIndexed { index, chart ->
            llButtons.addView(
                Button(this).apply {
                    text = index.toString()
                    setOnClickListener { cvChart.setChartData(chart) }
                    width = 48.px
                    height = 48.px
                }
            )
        }
    }

    private fun chartDataToLines(chartData: ChartData): List<Line> {
        val res = mutableListOf<Line>()
        val xColumn = chartData.columns.first { it[0] == Type.x.name }.drop(1).map { it.toLong() }
        chartData.types
            .filterNot { it.value == Type.x }
            .forEach { type ->
                res.add(
                    Line(
                        xColumn.zip(
                            chartData.columns.first { it[0] == type.key }.drop(1).map { it.toLong() }
                        ).toMap().toSortedMap(),
                        chartData.names.getValue(type.key),
                        Color.parseColor(chartData.colors[type.key])
                    )
                )
            }
        return res
    }
}
