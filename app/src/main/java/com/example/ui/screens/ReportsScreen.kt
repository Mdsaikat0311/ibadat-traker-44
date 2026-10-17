package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardUser
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(
    viewModel: IbadatViewModel,
    currentViewingDate: String
) {
    val isoFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val todayKey = remember { viewModel.repository.getTodayKey() }

    // Calendar month offset
    var calendarMonthOffset by remember { mutableIntStateOf(0) }

    val monthCalendar = remember(calendarMonthOffset) {
        Calendar.getInstance().apply {
            add(Calendar.MONTH, calendarMonthOffset)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val monthNamesBn = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    val viewMonth = monthCalendar.get(Calendar.MONTH)
    val viewYear = monthCalendar.get(Calendar.YEAR)
    val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun..6=Sat

    val dayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val isTodayViewing = currentViewingDate == todayKey

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("reports_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Date Picker Bar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ডেটা আপডেটের তারিখ নির্বাচন:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.moveDate(-1) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Prev", tint = TealPrimary)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF0FDFA),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF99F6E4)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = currentViewingDate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealDeep,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.moveDate(1) },
                            enabled = !isTodayViewing,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = if (!isTodayViewing) TealPrimary else Color.LightGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "তারিখ পরিবর্তন করে 'সালাত', 'আমল' বা 'যিকির' ট্যাবে যান।",
                        fontSize = 11.sp,
                        color = CoralRed,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 2. Monthly Heatmap Calendar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Month Navigation Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { calendarMonthOffset-- }
                                .padding(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Prev Month", tint = TealPrimary, modifier = Modifier.size(20.dp))
                            Text("Prev", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                        }

                        Text(
                            text = "${monthNamesBn[viewMonth]}, $viewYear",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { calendarMonthOffset++ }
                                .padding(4.dp)
                        ) {
                            Text("Next", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Month", tint = TealPrimary, modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Day of week header
                    Row(modifier = Modifier.fillMaxWidth()) {
                        dayLabels.forEach { label ->
                            Text(
                                text = label,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Days grid
                    val totalCells = firstDayOfWeek + daysInMonth
                    val rows = (totalCells + 6) / 7

                    for (r in 0 until rows) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            for (c in 0 until 7) {
                                val cellIndex = r * 7 + c
                                val dayNum = cellIndex - firstDayOfWeek + 1

                                if (cellIndex < firstDayOfWeek || dayNum > daysInMonth) {
                                    Spacer(modifier = Modifier.weight(1f))
                                } else {
                                    val cellDateCal = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, viewYear)
                                        set(Calendar.MONTH, viewMonth)
                                        set(Calendar.DAY_OF_MONTH, dayNum)
                                    }
                                    val cellDateStr = isoFormat.format(cellDateCal.time)
                                    val isCellToday = cellDateStr == todayKey
                                    val isCellSelected = cellDateStr == currentViewingDate
                                    val isFuture = cellDateStr > todayKey
                                    val cellPoints = if (!isFuture) viewModel.repository.calculatePoints(cellDateStr) else 0

                                    // Color logic
                                    val (cellBg, cellText) = when {
                                        isFuture -> Pair(Color(0xFFF1F5F9), Color(0xFFCBD5E1))
                                        cellPoints > 80 -> Pair(Color(0xFF15803D), Color.White)
                                        cellPoints > 40 -> Pair(Color(0xFF22C55E), Color.White)
                                        cellPoints > 15 -> Pair(Color(0xFF86EFAC), Color(0xFF14532D))
                                        cellPoints > 0 -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
                                        else -> Pair(Color(0xFFFAFAFA), Color(0xFF64748B))
                                    }

                                    val borderModifier = when {
                                        isCellSelected -> Modifier.border(2.dp, TealDeep, RoundedCornerShape(8.dp))
                                        isCellToday -> Modifier.border(2.dp, CoralRed, RoundedCornerShape(8.dp))
                                        else -> Modifier.border(0.5.dp, BorderLight, RoundedCornerShape(8.dp))
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(cellBg)
                                            .then(borderModifier)
                                            .clickable(enabled = !isFuture) {
                                                viewModel.selectDate(cellDateStr)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "$dayNum",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = cellText
                                            )
                                            if (cellPoints > 0 && !isFuture) {
                                                Text(
                                                    text = "+$cellPoints",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = cellText
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. 7-Day Fard Prayer Bar Chart
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.BarChart, contentDescription = null, tint = TealPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "গত ৭ দিনের সালাত",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Compute 7 days fard
                    val last7DaysData = remember(todayKey, currentViewingDate) {
                        val cal = Calendar.getInstance()
                        val list = mutableListOf<Triple<String, Int, Boolean>>()
                        for (i in 6 downTo 0) {
                            val d = Calendar.getInstance().apply {
                                time = cal.time
                                add(Calendar.DAY_OF_YEAR, -i)
                            }
                            val dateStr = isoFormat.format(d.time)
                            val dayShort = SimpleDateFormat("EEE", Locale.US).format(d.time)
                            val salah = viewModel.repository.getLog("salah", dateStr)
                            val fardCompleted = listOf("fajr", "zuhr", "asr", "maghrib", "isha").count { salah[it] == true }
                            list.add(Triple(dayShort, fardCompleted, dateStr == todayKey))
                        }
                        list
                    }

                    val totalFardEarned = last7DaysData.sumOf { it.second }
                    val totalPossible = 7 * 5
                    val percentage = (totalFardEarned.toFloat() / totalPossible * 100).toInt()

                    // Bars
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        last7DaysData.forEach { (label, count, isTodayBar) ->
                            val heightFraction = (count.toFloat() / 5f).coerceIn(0.08f, 1f)
                            val barColor = if (isTodayBar) CoralRed else TealPrimary

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = "$count",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = barColor
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(barColor)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$percentage% ফরয সালাত সম্পন্ন হয়েছে (গত ৭ দিনে)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealDeep,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 4. Five Leaderboard Cards
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(imageVector = Icons.Filled.EmojiEvents, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "সেরা পারফর্মার",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }

        // Leaderboard Cards:
        val cards = listOf(
            Triple("todayPoints", "আজকের সেরা", TealPrimary),
            Triple("yesterdayPoints", "গতকালকের সেরা", IndigoPrimary),
            Triple("totalPoints7Days", "সাপ্তাহিক সেরা (৭ দিন)", Color(0xFF2563EB)),
            Triple("totalPoints30Days", "মাসিক সেরা (৩০ দিন)", Color(0xFF7C3AED)),
            Triple("totalPoints", "সর্বমোট সেরা (লাইফটাইম)", AmberAccent)
        )

        items(cards.size) { idx ->
            val (metric, title, accentColor) = cards[idx]
            val top3 = remember(metric, currentViewingDate) {
                viewModel.getLeaderboardList(metric).take(3)
            }

            LeaderboardWidgetCard(
                title = title,
                metric = metric,
                accentColor = accentColor,
                topUsers = top3,
                onViewAll = { viewModel.openLeaderboard(metric, title) }
            )
        }
    }
}

@Composable
fun LeaderboardWidgetCard(
    title: String,
    metric: String,
    accentColor: Color,
    topUsers: List<LeaderboardUser>,
    onViewAll: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                topUsers.forEachIndexed { index, user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (index) {
                                            0 -> Color(0xFFFEF3C7)
                                            1 -> Color(0xFFF1F5F9)
                                            else -> Color(0xFFFFEDD5)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (index == 0) {
                                    Text("👑", fontSize = 11.sp)
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = user.name,
                                fontSize = 13.sp,
                                fontWeight = if (user.isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                                color = TextDark
                            )
                            if (user.isCurrentUser) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = TealPrimary,
                                    shape = RoundedCornerShape(3.dp)
                                ) {
                                    Text("আমি", fontSize = 9.sp, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
                        ) {
                            Text(
                                text = "${user.points} pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewAll() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সব দেখুন",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
