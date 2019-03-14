package kidinov.telegram.chart

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.model.ChartData
import kidinov.telegram.chart.model.Line
import kidinov.telegram.chart.model.Type
import kotlinx.android.synthetic.main.activity_main.cvChart
import kotlinx.android.synthetic.main.activity_main.glButtons
import kotlinx.android.synthetic.main.activity_main.pbProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : Activity() {
    private val uiScope = CoroutineScope(Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseJson()
    }

    private fun parseJson() {
        runInBackground(
            {
                val chartRead = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.chart_data)))
                val chartData = chartRead.use {
                    Json.parse(ChartData.serializer().list, it.readText())
                }

                chartData.map { Chart(chartDataToLines(it)) }
            },
            { charts ->
                generateButtons(charts)
                glButtons.getChildAt(0).callOnClick()
            }
        )
    }

    private fun <T> runInBackground(bcg: () -> T, ui: (T) -> Unit) {
        uiScope.launch {
            pbProgress.visibility = View.VISIBLE
            val res = withContext(Dispatchers.Default) { bcg() }
            pbProgress.visibility = View.GONE
            ui(res)
        }
    }

    private fun generateButtons(charts: List<Chart>) {
        charts.forEachIndexed { index, chart ->
            glButtons.addView(
                Button(this).apply {
                    text = index.toString()
                    setOnClickListener { cvChart.setChartData(chart) }
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
