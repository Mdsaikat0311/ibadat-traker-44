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
import com.example.data.model.RoutineTaskItem
import com.example.data.repository.IbadatRepository
import com.example.ui.components.TaskStatsRow
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel

@Composable
fun AmalScreen(
    viewModel: IbadatViewModel,
    appStats: AppStats,
    specialAmalLog: Map<String, Boolean>,
    morningLog: Map<String, Boolean>,
    nightlyLog: Map<String, Boolean>,
    quranLog: Map<String, Boolean>,
    reminders: Map<String, Any>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("amal_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top 2 Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${appStats.selectedDayPoints}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                        Text(
                            text = "নির্বাচিত দিনের পয়েন্ট",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${appStats.totalLifetimePoints}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "মোট অর্জিত পয়েন্ট",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // 1. বিশেষ আমল (দান ও সদাকা)
        item {
            Text(
                text = "বিশেষ আমল",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(IbadatRepository.SPECIAL_AMAL_TASKS) { task ->
            RoutineTaskCard(
                category = "specialAmal",
                task = task,
                isCompleted = specialAmalLog[task.key] == true,
                hasReminder = reminders.containsKey(task.key),
                accentColor = EmeraldPrimary,
                viewModel = viewModel
            )
        }

        // 2. সকাল-সন্ধ্যার রুটিন
        item {
            Text(
                text = "সকাল-সন্ধ্যার রুটিন",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(IbadatRepository.MORNING_TASKS) { task ->
            RoutineTaskCard(
                category = "morning",
                task = task,
                isCompleted = morningLog[task.key] == true,
                hasReminder = reminders.containsKey(task.key),
                accentColor = EmeraldPrimary,
                viewModel = viewModel
            )
        }

        // 3. রাতের রুটিন
        item {
            Text(
                text = "রাতের রুটিন",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(IbadatRepository.NIGHTLY_TASKS) { task ->
            RoutineTaskCard(
                category = "nightly",
                task = task,
                isCompleted = nightlyLog[task.key] == true,
                hasReminder = reminders.containsKey(task.key),
                accentColor = IndigoPrimary,
                viewModel = viewModel
            )
        }

        // 4. কুরআন রুটিন
        item {
            Text(
                text = "কুরআন রুটিন",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(IbadatRepository.QURAN_TASKS) { task ->
            RoutineTaskCard(
                category = "quran",
                task = task,
                isCompleted = quranLog[task.key] == true,
                hasReminder = reminders.containsKey(task.key),
                accentColor = TealPrimary,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun RoutineTaskCard(
    category: String,
    task: RoutineTaskItem,
    isCompleted: Boolean,
    hasReminder: Boolean,
    accentColor: Color,
    viewModel: IbadatViewModel
) {
    val stats = viewModel.getTaskStats(category, task.key)
    val containerBg = if (isCompleted) Color(0xFFECFDF5) else Color.White
    val borderCol = if (isCompleted) Color(0xFFA7F3D0) else BorderLight

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
                        imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = if (isCompleted) accentColor else Color(0xFFCBD5E1),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = task.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(+${task.point}p)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "টার্গেট: ${task.target} বার",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = "Details",
                        tint = TextMuted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                viewModel.openDetails(task.name, task.details)
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Reminder",
                        tint = if (hasReminder) TealPrimary else TextMuted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                viewModel.openReminder(task.key, task.name)
                            }
                    )
                }

                TaskStatsRow(stats = stats)
            }

            Switch(
                checked = isCompleted,
                onCheckedChange = { viewModel.toggleRoutineTask(category, task.key) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = accentColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE2E8F0)
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
