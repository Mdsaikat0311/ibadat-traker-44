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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HadithBank
import com.example.data.model.AppStats
import com.example.data.model.GeneralReminderSetting
import com.example.ui.components.StatsGrid
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: IbadatViewModel,
    userName: String,
    appStats: AppStats,
    autoHadithEnabled: Boolean,
    generalReminder: GeneralReminderSetting,
    lifetimeStartDate: String,
    isViewingToday: Boolean
) {
    var showNameEditDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(userName) }

    var showDateEditDialog by remember { mutableStateOf(false) }
    var dateInput by remember { mutableStateOf(lifetimeStartDate) }

    var currentQuote by remember { mutableStateOf(HadithBank.getRandomQuote()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen")
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Profile
        item {
            Text(
                text = "আমার প্রোফাইল",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCCFBF1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.toString() ?: "U",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealDeep
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                nameInput = userName
                                showNameEditDialog = true
                            }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = userName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit name",
                            tint = TealPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    StatsGrid(
                        stats = appStats,
                        isToday = isViewingToday,
                        onOpenLeaderboard = { type, title ->
                            viewModel.openLeaderboard(type, title)
                        },
                        getRank = { metric -> viewModel.getMyRank(metric) }
                    )
                }
            }
        }

        // Section: Automatic Hadith Toggle
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "অটোমেটিক হাদিস",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                        Text(
                            text = "প্রতি ৩০ মিনিট পর পর (অনলাইন/অফলাইন)",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Switch(
                        checked = autoHadithEnabled,
                        onCheckedChange = { viewModel.toggleAutoHadith() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = IndigoPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }
        }

        // Section: Tauba & Zikir Reminder
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Outlined.NotificationsActive, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "তাওবা ও জিকির রিমাইন্ডার",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                            Text(
                                text = "নির্দিষ্ট সময় পর পর সতর্কবার্তা দেবে",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Switch(
                            checked = generalReminder.enabled,
                            onCheckedChange = { viewModel.toggleGeneralReminder() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AmberAccent,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সময় ব্যবধান:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )

                        var expanded by remember { mutableStateOf(false) }
                        val intervals = listOf(10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60)

                        Box {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { expanded = true },
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${generalReminder.interval} মিনিট",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = TextMuted
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                intervals.forEach { interval ->
                                    DropdownMenuItem(
                                        text = { Text("$interval মিনিট") },
                                        onClick = {
                                            viewModel.setGeneralInterval(interval)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Lifetime Point Count Start Date
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
                        Icon(imageVector = Icons.Outlined.Schedule, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "পয়েন্ট কাউন্ট শুরু",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    dateInput = lifetimeStartDate
                                    showDateEditDialog = true
                                }
                                .padding(vertical = 4.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = lifetimeStartDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                dateInput = lifetimeStartDate
                                showDateEditDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("পরিবর্তন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section: Daily Islamic Quote Inspiration
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF0FDFA),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF99F6E4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "কুরআনের বাণী ও হাদিস",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealDeep
                            )
                        }
                        IconButton(
                            onClick = { currentQuote = HadithBank.getRandomQuote() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "Next Quote", tint = TealPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentQuote,
                        fontSize = 13.sp,
                        color = Color(0xFF0F766E),
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Section: Reset App Data
        item {
            OutlinedButton(
                onClick = { viewModel.requestReset() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("সমস্ত ডেটা রিসেট করুন", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Name Edit Dialog
    if (showNameEditDialog) {
        AlertDialog(
            onDismissRequest = { showNameEditDialog = false },
            title = { Text("নাম পরিবর্তন করুন", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("আপনার নাম") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setUserName(nameInput)
                        showNameEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("সেভ করুন", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameEditDialog = false }) {
                    Text("বাতিল", color = TextMuted)
                }
            }
        )
    }

    // Date Edit Dialog
    if (showDateEditDialog) {
        AlertDialog(
            onDismissRequest = { showDateEditDialog = false },
            title = { Text("পয়েন্ট কাউন্ট শুরুর তারিখ", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("ফরম্যাট: YYYY-MM-DD") },
                    placeholder = { Text("2026-09-01") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setLifetimeStartDate(dateInput)
                        showDateEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("সেট করুন", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateEditDialog = false }) {
                    Text("বাতিল", color = TextMuted)
                }
            }
        )
    }
}
