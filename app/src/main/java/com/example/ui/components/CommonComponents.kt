package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStats
import com.example.data.model.ChampionInfo
import com.example.data.model.TaskStats
import com.example.ui.theme.*

@Composable
fun AppHeader(
    title: String,
    subtitle: String,
    dateDisplay: String,
    isViewingToday: Boolean,
    onGoToToday: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateDisplay,
                        fontSize = 13.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    if (!isViewingToday) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onGoToToday() },
                            color = TealDeep
                        ) {
                            Text(
                                text = "Go to Today",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )
            }
        }
    }
}

@Composable
fun RankBadge(rank: Int) {
    val (bg, border, contentColor, icon) = when (rank) {
        1 -> Quadruple(Color(0xFFFEF9C3), Color(0xFFFACC15), Color(0xFFB45309), Icons.Filled.EmojiEvents)
        2 -> Quadruple(Color(0xFFF1F5F9), Color(0xFF94A3B8), Color(0xFF475569), Icons.Filled.WorkspacePremium)
        3 -> Quadruple(Color(0xFFFFEDD5), Color(0xFFFB923C), Color(0xFFC2410C), Icons.Filled.MilitaryTech)
        else -> Quadruple(Color(0xFFF0FDFA), Color(0xFF5EEAD4), Color(0xFF0F766E), null)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Rank $rank",
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text(
            text = "#$rank",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun YesterdayChampionCard(
    champion: ChampionInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFFBEB))
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = "Champion",
                    tint = AmberAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "গতকালকের সেরা",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706)
                )
                Text(
                    text = champion.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }
        Surface(
            color = AmberAccent,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "${champion.points} pts",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun StatsGrid(
    stats: AppStats,
    isToday: Boolean,
    onOpenLeaderboard: (String, String) -> Unit,
    getRank: (String) -> Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Account Lifetime Points (Full width card)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onOpenLeaderboard("totalPoints", "সর্বমোট সেরা (লাইফটাইম)") },
            color = Color(0xFFE6FFFA),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF99F6E4)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    RankBadge(rank = getRank("totalPoints"))
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${stats.totalLifetimePoints}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TealDeep
                    )
                    Text(
                        text = "অ্যাকাউন্ট লাইফটাইম পয়েন্ট",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2x3 Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "নির্বাচিত দিনের পয়েন্ট",
                value = "${stats.selectedDayPoints}",
                color = TealPrimary,
                bgColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFFCCFBF1),
                rank = if (isToday) getRank("todayPoints") else null,
                onClick = { onOpenLeaderboard("todayPoints", "আজকের সেরা") }
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "গতকালকের পয়েন্ট",
                value = "${stats.yesterdayPoints}",
                color = IndigoPrimary,
                bgColor = Color(0xFFEEF2FF),
                borderColor = Color(0xFFE0E7FF),
                rank = getRank("yesterdayPoints"),
                onClick = { onOpenLeaderboard("yesterdayPoints", "গতকালকের সেরা") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "গত ৭ দিনের মোট পয়েন্ট",
                value = "${stats.totalPoints7Days}",
                color = Color(0xFF059669),
                bgColor = Color(0xFFECFDF5),
                borderColor = Color(0xFFA7F3D0),
                rank = getRank("totalPoints7Days"),
                onClick = { onOpenLeaderboard("totalPoints7Days", "সাপ্তাহিক সেরা (৭ দিন)") }
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "৩০ দিনের মোট পয়েন্ট",
                value = "${stats.totalPoints30Days}",
                color = Color(0xFF2563EB),
                bgColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                rank = getRank("totalPoints30Days"),
                onClick = { onOpenLeaderboard("totalPoints30Days", "মাসিক সেরা (৩০ দিন)") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "৩০ দিনের হাই স্কোর",
                value = "${stats.highScore30Days}",
                color = Color(0xFFD97706),
                bgColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A),
                rank = null,
                onClick = null
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "৭ দিনে ফরয সালাত",
                value = "${stats.fardCompleted7Days}/35",
                color = CoralRed,
                bgColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFECACA),
                rank = null,
                onClick = null
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color,
    bgColor: Color,
    borderColor: Color,
    rank: Int? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            if (rank != null) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    RankBadge(rank = rank)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TaskStatsRow(stats: TaskStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        val weekPct = if (stats.totalWeekDays > 0) (stats.week.toFloat() / stats.totalWeekDays) else 0f
        val weekColor = if (weekPct >= 0.5f) Color(0xFF16A34A) else CoralRed

        val monthPct = if (stats.totalMonthDays > 0) (stats.month.toFloat() / stats.totalMonthDays) else 0f
        val monthColor = if (monthPct >= 0.5f) Color(0xFF16A34A) else CoralRed

        val lifePct = if (stats.totalLifetimeDays > 0) (stats.lifetime.toFloat() / stats.totalLifetimeDays) else 0f
        val lifeColor = if (lifePct >= 0.5f) Color(0xFF16A34A) else CoralRed

        Text(
            text = "Week(${stats.week}/${stats.totalWeekDays})",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = weekColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Month(${stats.month}/${stats.totalMonthDays})",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = monthColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Lifetime(${stats.lifetime}/${stats.totalLifetimeDays})",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = lifeColor
        )
    }
}
