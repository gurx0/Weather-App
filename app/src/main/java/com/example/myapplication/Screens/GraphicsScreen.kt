package com.example.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.myapplication.VM.GraphicsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun PreviewChartScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        WeatherChartScreen(rememberNavController())
    }
}

@Composable
fun WeatherChartScreen(
    navController: NavController,
    viewModel: GraphicsViewModel = hiltViewModel()
) {
    val steps = 5
    val translucentWhite = Color.White.copy(alpha = 0.5f)
    val tr = Color.Transparent
    val grayGrid = Color.Gray.copy(alpha = 0.3f)

    val pointsData = listOf(
        Point(0f, 17f),
        Point(1f, 21f),
        Point(2f, 18f),
        Point(3f, 22f),
        Point(4f, 25f)
    )

    val minTemp = pointsData.minOf { it.y }
    val maxTemp = pointsData.maxOf { it.y }

    val tempRange = maxTemp - minTemp

    val formatter = DateTimeFormatter.ofPattern("dd")
    val today = LocalDate.now()

    val xAxisData = AxisData.Builder()
        .axisStepSize(73.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.size - 1)
        .labelData { i -> today.plusDays(i.toLong()).format(formatter) }
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .labelAndAxisLinePadding(10.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .axisLineColor(tr)
        .labelAndAxisLinePadding(10.dp)
        .axisLabelColor(Color.White)
        .labelData { i ->
            val stepValue = minTemp + (tempRange * i / steps)
            "${stepValue.toInt()}°"
        }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(color = Color.White, alpha = 0.4f),
                    intersectionPoint = IntersectionPoint(color = Color.White),
                    selectionHighlightPoint = SelectionHighlightPoint(color = Color.White, alpha = 0.4f),
                    shadowUnderLine = ShadowUnderLine(color = translucentWhite.copy(alpha = 0.2f)),
                    selectionHighlightPopUp = SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = grayGrid),
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.size(16.dp))
        Text(
            text = "Температура по дням",
            style = MaterialTheme.typography.titleMedium,
            color = translucentWhite,
            modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(tr)
                .clip(RoundedCornerShape(4.dp)),
            lineChartData = lineChartData
        )
    }
}