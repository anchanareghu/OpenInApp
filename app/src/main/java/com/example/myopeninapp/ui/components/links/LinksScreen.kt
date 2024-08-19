package com.example.myopeninapp.ui.components.links

import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myopeninapp.R
import com.example.myopeninapp.data.model.data.LinksData
import com.example.myopeninapp.data.model.data.Link
import com.example.myopeninapp.data.model.data.RecentLink
import com.example.myopeninapp.data.model.data.TopLink
import com.example.myopeninapp.repository.LinksRepository
import com.example.myopeninapp.ui.theme.Blue
import com.example.myopeninapp.ui.theme.LighterGray
import com.example.myopeninapp.ui.theme.Red
import com.example.myopeninapp.ui.theme.Violet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import utils.Resource
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun LinksScreen() {
    val dashboardViewModel: LinksViewmodel =
        viewModel(factory = LinksViewModelFactory(repository = LinksRepository()))
    val dashboardData by dashboardViewModel.dashboardData.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
    ) {
        val (topBar, contentSection) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Blue)
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = "Dashboard",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )

                IconButton(
                    onClick = { /* Handle settings click */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Color.White.copy(alpha = 0.2f)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)

                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(LighterGray)
                .constrainAs(contentSection) {
                    top.linkTo(topBar.bottom, margin = (-40).dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Greeting()
                when (dashboardData) {
                    is Resource.Loading, is Resource.Error -> {
                        LoadingAndErrorPlaceholder()
                    }

                    is Resource.Success -> {
                        val data = dashboardData.data
                        if (data != null) {
                            LineChartViewComposable(data.data.overall_url_chart)
                            QuickInfoCards(listOf(data))
                            AnalyticsButton()
                            TabsSection(data)

                            Spacer(modifier = Modifier.height(8.dp))

                            Contact(
                                icon = painterResource(id = R.drawable.whatsapp),
                                text = "Talk with us"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Contact(
                                icon = painterResource(id = R.drawable.question_mark),
                                text = "Frequently Asked Questions"
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingAndErrorPlaceholder() {
    Spacer(modifier = Modifier.height(16.dp))
    repeat(6) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (it % 2 == 0) 250.dp else 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val shimmerAlpha by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            start = Offset(shimmerAlpha, 0f),
            end = Offset(shimmerAlpha + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}
@Composable
fun LineChartViewComposable(
    overallUrlChart: Map<String, Int>?
) {
    val errorState = remember { mutableStateOf<String?>(null) }
    val lineData = remember { mutableStateOf<LineData?>(null) }

    LaunchedEffect(overallUrlChart) {
        try {
            if (overallUrlChart.isNullOrEmpty()) {
                errorState.value = "No Data Available"
                lineData.value = null
            } else {
                lineData.value = generateLineData(overallUrlChart)
                errorState.value = null
            }
        } catch (e: Exception) {
            errorState.value = "Error processing chart data"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(250.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        if (lineData.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorState.value ?: "Unknown error",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Overview", color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .background(LighterGray)
                            .border(
                                width = 0.5.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        if (overallUrlChart != null) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "${overallUrlChart.keys.minOrNull()} - ${overallUrlChart.keys.maxOrNull()}",
                                color = Color.Gray
                            )
                        }
                    }
                }
                if (lineData.value != null) {
                    AndroidView(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(250.dp),
                        factory = { context ->
                            LineChart(context).apply {
                                this.data = lineData.value

                                this.xAxis.apply {
                                    position = XAxis.XAxisPosition.BOTTOM
                                    setDrawGridLines(true)
                                    setDrawAxisLine(true)
                                    valueFormatter = HourValueFormatter()
                                    axisMinimum = 0f
                                    axisMaximum = lineData.value?.xMax ?: 0f
                                }

                                this.axisRight.isEnabled = false
                                this.axisLeft.apply {
                                    setDrawGridLines(true)
                                    setDrawAxisLine(true)
                                    axisMinimum = 0f
                                    axisMaximum = lineData.value?.yMax ?: 0f
                                }

                                this.description.isEnabled = false
                                this.legend.isEnabled = false

                                setDrawGridBackground(false)
                                setBackgroundColor(Color.Transparent.toArgb())
                                setPadding(0, 0, 0, 0)

                                this.invalidate()
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun generateLineData(chartResponse: Map<String, Int>?): LineData {
    val lineList = ArrayList<Entry>()
    val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
    val calendar = Calendar.getInstance()

    val startDate = chartResponse?.keys?.minOrNull()?.let {
        try {
            dateFormat.parse(it)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    chartResponse?.forEach { (key, value) ->
        try {
            val date = dateFormat.parse(key)
            date?.let {
                calendar.time = date
                val hoursSinceStart =
                    TimeUnit.MILLISECONDS.toHours(date.time - (startDate?.time ?: 0)).toFloat()
                lineList.add(Entry(hoursSinceStart, value.toFloat()))
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    lineList.sortBy { it.x }

    val lineDataSet = LineDataSet(lineList, "Data Set").apply {
        setDrawFilled(true)
        setDrawCircles(false)
        lineWidth = 2f
        color = Blue.toArgb()
        fillDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Blue.toArgb(), Color.Transparent.toArgb())
        )
        mode = LineDataSet.Mode.LINEAR
        setDrawHorizontalHighlightIndicator(false)
        setDrawVerticalHighlightIndicator(false)
        setDrawValues(false)
        setDrawHighlightIndicators(false)
        setDrawCircles(false)
    }

    return LineData(lineDataSet)
}

class HourValueFormatter() : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val hour = (value.toInt() % 24)
        return String.format("%02d:00", hour)
    }
}


@Composable
fun QuickInfoCards(
    dataList: List<LinksData>
) {
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        items(dataList) { data ->
            QuickInfoCard(
                icon = painterResource(id = R.drawable.click),
                tint = Violet,
                title = data.total_clicks.toString(),
                subtitle = "Total Clicks"
            )
            QuickInfoCard(
                icon = painterResource(id = R.drawable.location),
                tint = Blue,
                title = data.top_location,
                subtitle = "Top Location"
            )
            QuickInfoCard(
                icon = painterResource(id = R.drawable.web),
                tint = Red,
                title = data.top_source,
                subtitle = "Top Source"
            )
            QuickInfoCard(
                icon = painterResource(id = R.drawable.links),
                tint = Color.Blue,
                title = data.total_links.toString(),
                subtitle = "Total Links"
            )
            QuickInfoCard(
                icon = painterResource(id = R.drawable.click),
                tint = Color.Magenta,
                title = data.today_clicks.toString(),
                subtitle = "Today's Clicks"
            )
        }
    }
}

@Composable
fun QuickInfoCard(icon: Painter, tint: Color, title: String, subtitle: String) {
    Card(
        modifier = Modifier.padding(end = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = icon,
                tint = tint,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .background(tint.copy(alpha = 0.1f), CircleShape)
                    .padding(8.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
    }
}


@Composable
fun AnalyticsButton() {
    Button(
        onClick = { /* TODO: Navigate to Analytics Screen */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.analytics),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp)
        )
        Text(text = "View Analytics")
    }
}

@Composable
fun TabsSection(dashboardData: LinksData) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Top Links", "Recent Links")

    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                indicator = {},
                divider = {},
                modifier = Modifier.weight(1f)
            ) {
                tabs.forEachIndexed { index, text ->
                    Tab(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selectedTab == index) Blue else Color.Transparent),
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(text, style = MaterialTheme.typography.titleMedium) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Gray,
                    )
                }
            }

            IconButton(
                onClick = { /* TODO: Handle search click */ },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        when (selectedTab) {
            0 -> LinkList(dashboardData.data.top_links)
            1 -> LinkList(dashboardData.data.recent_links)
        }
    }
}

@Composable
fun LinkList(links: List<Link>) {
    var showAll by remember { mutableStateOf(false) }

    val visibleLinks = if (showAll) links else links.take(4)

    Column {
        visibleLinks.forEach { link ->
            LinkListItem(
                name = link.title,
                time = link.times_ago,
                url = if (link is TopLink) link.web_link else if (link is RecentLink) link.web_link else "",
                clicks = link.total_clicks
            )
        }

        if (links.size >= 5) {

            Button(
                onClick = {
                    showAll = !showAll
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.links),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 16.dp)
                )
                Text(text = "View All Links")
            }
        }
    }

}

@Composable
fun LinkListItem(name: String, time: String, url: String, clicks: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.amazon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name, style = MaterialTheme.typography.titleMedium,
                        color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = clicks.toString(),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Clicks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            val stroke = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            val context = LocalContext.current
            val clipboardManager = LocalClipboardManager.current
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Blue.copy(alpha = 0.12f))
                    .clickable {
                        clipboardManager.setText(AnnotatedString(url))
                        Toast
                            .makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .drawBehind {
                        drawRoundRect(color = Blue, style = stroke)
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = url,
                    color = Blue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .widthIn(max = 200.dp)
                        .align(Alignment.CenterStart)
                )
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.copy),
                        tint = Blue,
                        contentDescription = "copy",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting() {
    val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greetingText = when (currentTime) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Column {
        Text(
            text = greetingText,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Ajay Manva 👋",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun Contact(icon: Painter, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Blue.copy(alpha = 0.12f)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    LinksScreen()
}

@Preview(showBackground = true)
@Composable
fun LineChartViewPreview() {
    val urlChartResponse = mapOf(
        "00:00" to 1,
        "01:00" to 0,
        "02:00" to 0,
        "03:00" to 0,
        "04:00" to 0,
        "05:00" to 0,
        "06:00" to 0,
        "07:00" to 0,
        "08:00" to 0,
        "09:00" to 0,
        "10:00" to 0,
        "11:00" to 0,
        "12:00" to 0,
        "13:00" to 0,
        "14:00" to 0,
        "15:00" to 0,
        "16:00" to 0,
        "17:00" to 0,
        "18:00" to 0,
        "19:00" to 0,
        "20:00" to 0,
        "21:00" to 0,
        "22:00" to 0,
        "23:00" to 0
    )
    LineChartViewComposable(urlChartResponse)
}
