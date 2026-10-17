package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
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
import com.example.data.model.RoutineTaskItem
import com.example.data.repository.IbadatRepository
import com.example.ui.components.TaskStatsRow
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel

@Composable
fun DhikrScreen(
    viewModel: IbadatViewModel,
    dhikrLog: Map<String, Boolean>,
    reminders: Map<String, Any>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dhikr_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "গুরুত্বপূর্ণ দৈনিক যিকির",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        items(IbadatRepository.DHIKR_CHECKLIST_TASKS) { task ->
            DhikrItemCard(
                task = task,
                isCompleted = dhikrLog[task.key] == true,
                hasReminder = reminders.containsKey(task.key),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun DhikrItemCard(
    task: RoutineTaskItem,
    isCompleted: Boolean,
    hasReminder: Boolean,
    viewModel: IbadatViewModel
) {
    val stats = viewModel.getTaskStats("dhikr", task.key)
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
                        tint = if (isCompleted) EmeraldPrimary else Color(0xFFCBD5E1),
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
                        color = EmeraldPrimary
                    )
                }

                if (task.arabic != null) {
                    Text(
                        text = task.arabic,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TealDeep,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Spacer(modifier = Modifier.width(10.dp))
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
                onCheckedChange = { viewModel.toggleDhikr(task.key) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE2E8F0)
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
