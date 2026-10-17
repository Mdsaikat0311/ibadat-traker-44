package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStats
import com.example.data.model.ChampionInfo
import com.example.data.model.NafilPrayerItem
import com.example.data.model.PrayerItem
import com.example.data.repository.IbadatRepository
import com.example.ui.components.StatsGrid
import com.example.ui.components.TaskStatsRow
import com.example.ui.components.YesterdayChampionCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SalahScreen(
    viewModel: IbadatViewModel,
    appStats: AppStats,
    yesterdayChampion: ChampionInfo,
    salahLog: Map<String, Boolean>,
    nawafilLog: Map<String, Boolean>,
    reminders: Map<String, Any>,
    isViewingToday: Boolean
) {
    val bnDateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("bn", "BD"))
    val todayBnDate = bnDateFormat.format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("salah_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "সালাত ও ইবাদত",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = todayBnDate,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Yesterday Champion Card
        item {
            YesterdayChampionCard(
                champion = yesterdayChampion,
                onClick = {
                    viewModel.openLeaderboard("yesterdayPoints", "গতকালকের সেরা")
                }
            )
        }

        // 7 Stats Cards
        item {
            StatsGrid(
                stats = appStats,
                isToday = isViewingToday,
                onOpenLeaderboard = { type, title ->
                    viewModel.openLeaderboard(type, title)
                },
                getRank = { metric -> viewModel.getMyRank(metric) }
            )
        }

        // Section: Daily Fard Prayers
        item {
            Text(
                text = "দৈনিক ফরয সালাত",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(IbadatRepository.PRAYERS) { prayer ->
            PrayerCard(
                prayer = prayer,
                isCompleted = salahLog[prayer.key] == true,
                isDuaCompleted = salahLog["${prayer.key}_dua"] == true,
                isMorningZikirCompleted = salahLog["fajr_morning_zikir"] == true,
                isEveningZikirCompleted = salahLog["maghrib_evening_zikir"] == true,
                hasReminder = reminders.containsKey(prayer.key),
                viewModel = viewModel
            )
        }

        // Section: Nawafil Prayers
        item {
            Text(
                text = "নফল ইবাদত",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(IbadatRepository.NAWAFIL_PRAYERS) { nafil ->
            NawafilCard(
                nafil = nafil,
                isCompleted = nawafilLog[nafil.key] == true,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun PrayerCard(
    prayer: PrayerItem,
    isCompleted: Boolean,
    isDuaCompleted: Boolean,
    isMorningZikirCompleted: Boolean,
    isEveningZikirCompleted: Boolean,
    hasReminder: Boolean,
    viewModel: IbadatViewModel
) {
    val isTahajjud = prayer.key == "tahajjud"
    val mainColor = if (isTahajjud) IndigoPrimary else TealPrimary
    val containerBg = if (isCompleted) {
        if (isTahajjud) Color(0xFFEEF2FF) else Color(0xFFF0FDFA)
    } else Color.White
    val borderCol = if (isCompleted) {
        if (isTahajjud) Color(0xFFA5B4FC) else Color(0xFF99F6E4)
    } else BorderLight

    val stats = viewModel.getTaskStats("salah", prayer.key)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        color = containerBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderCol),
        shape = RoundedCornerShape(14.dp),
        shadowElevation = if (isCompleted) 1.dp else 0.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Main Prayer Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = if (isCompleted) mainColor else Color(0xFFCBD5E1),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prayer.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(+${prayer.point}p)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = mainColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        // Details Icon
                        Icon(
                            imageVector = Icons.Outlined.MenuBook,
                            contentDescription = "Details",
                            tint = TextMuted,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    viewModel.openDetails(prayer.name, prayer.details)
                                }
                        )

                        Spacer(modifier = Modifier.width(6.dp))
                        // Reminder Bell
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Reminder",
                            tint = if (hasReminder) TealPrimary else TextMuted,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    viewModel.openReminder(prayer.key, prayer.name)
                                }
                        )
                    }

                    TaskStatsRow(stats = stats)
                }

                // Switch
                Switch(
                    checked = isCompleted,
                    onCheckedChange = { viewModel.toggleSalah(prayer.key) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = mainColor,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Subtask: Du'a & Zikir (+5p) for Fard
            if (prayer.hasDua) {
                HorizontalDivider(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                val duaKey = "${prayer.key}_dua"
                val duaStats = viewModel.getTaskStats("salah", duaKey)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "দু’আ ও যিকির",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(+5p)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = "Dua Details",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        viewModel.openDetails(
                                            "${prayer.name} সালাত পরবর্তী দু’আ ও যিকির",
                                            "প্রতিটি ফরয সালাতের পর রাসুল (সা.) কিছু নির্দিষ্ট দু'আ ও যিকির পাঠ করতেন। যেমন: ৩ বার আস্তাগফিরুল্লাহ, আয়াতুল কুরসী ১ বার, সুবহানাল্লাহ ৩৩ বার, আলহামদুলিল্লাহ ৩৩ বার, আল্লাহু আকবার ৩৪ বার। এটি জান্নাত লাভের সহজ মাধ্যম।"
                                        )
                                    }
                            )
                        }
                        TaskStatsRow(stats = duaStats)
                    }

                    Switch(
                        checked = isDuaCompleted,
                        onCheckedChange = { viewModel.toggleSalah(duaKey) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TealPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Subtask: Fajr Morning Zikir (+3p)
            if (prayer.hasMorningZikir) {
                HorizontalDivider(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                val morningZikirStats = viewModel.getTaskStats("salah", "fajr_morning_zikir")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.WbSunny,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "সকাল বেলার যিকির",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(+3p)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = "Details",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        viewModel.openDetails(
                                            "সকাল বেলার মাসনুন যিকির",
                                            "ফজরের পর সূর্যোদয় পর্যন্ত সময়টি যিকির ও দু'আর জন্য অত্যন্ত বরকতময়। এসময় আয়াতুল কুরসি, ৩ কুল, সাইয়্যিদুল ইস্তিগফার এবং সকালের নির্দিষ্ট মাসনুন দু'আগুলো পাঠ করা সুন্নাত।"
                                        )
                                    }
                            )
                        }
                        TaskStatsRow(stats = morningZikirStats)
                    }

                    Switch(
                        checked = isMorningZikirCompleted,
                        onCheckedChange = { viewModel.toggleSalah("fajr_morning_zikir") },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AmberAccent,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Subtask: Maghrib Evening Zikir (+3p)
            if (prayer.hasEveningZikir) {
                HorizontalDivider(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                val eveningZikirStats = viewModel.getTaskStats("salah", "maghrib_evening_zikir")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.NightsStay,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "সন্ধ্যা বেলার যিকির",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(+3p)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = IndigoPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = "Details",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        viewModel.openDetails(
                                            "সন্ধ্যা বেলার মাসনুন যিকির",
                                            "আসরের পর থেকে মাগরিব পর্যন্ত বা মাগরিবের পরের সময়টি সন্ধ্যার যিকিরের জন্য নির্ধারিত। এসময় আমানুর রাসুলু, ৩ কুল এবং সন্ধ্যার মাসনুন দু'আগুলো পাঠ করা উচিত যা সারারাত নিরাপত্তার কারণ হয়।"
                                        )
                                    }
                            )
                        }
                        TaskStatsRow(stats = eveningZikirStats)
                    }

                    Switch(
                        checked = isEveningZikirCompleted,
                        onCheckedChange = { viewModel.toggleSalah("maghrib_evening_zikir") },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = IndigoPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NawafilCard(
    nafil: NafilPrayerItem,
    isCompleted: Boolean,
    viewModel: IbadatViewModel
) {
    val stats = viewModel.getTaskStats("nawafil", nafil.key)
    val containerBg = if (isCompleted) Color(0xFFEEF2FF) else Color.White
    val borderCol = if (isCompleted) Color(0xFFA5B4FC) else BorderLight

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        color = containerBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderCol),
        shape = RoundedCornerShape(14.dp),
        shadowElevation = if (isCompleted) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.WbTwilight,
                        contentDescription = null,
                        tint = if (isCompleted) IndigoPrimary else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = nafil.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(+${nafil.point}p)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = "Details",
                        tint = TextMuted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                viewModel.openDetails(nafil.name, nafil.details)
                            }
                    )
                }

                Text(
                    text = nafil.info,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )

                TaskStatsRow(stats = stats)
            }

            Switch(
                checked = isCompleted,
                onCheckedChange = { viewModel.toggleNawafil(nafil.key) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = IndigoPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE2E8F0)
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
