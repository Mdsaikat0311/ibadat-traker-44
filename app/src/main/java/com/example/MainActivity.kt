package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.IbadatViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                IbadatApp()
            }
        }
    }
}

data class NavItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun IbadatApp(viewModel: IbadatViewModel = viewModel()) {
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val currentViewingDate by viewModel.currentViewingDate.collectAsStateWithLifecycle()
    val appStats by viewModel.appStats.collectAsStateWithLifecycle()
    val yesterdayChampion by viewModel.yesterdayChampion.collectAsStateWithLifecycle()

    val salahLog by viewModel.salahLog.collectAsStateWithLifecycle()
    val nawafilLog by viewModel.nawafilLog.collectAsStateWithLifecycle()
    val dhikrLog by viewModel.dhikrLog.collectAsStateWithLifecycle()
    val morningLog by viewModel.morningLog.collectAsStateWithLifecycle()
    val nightlyLog by viewModel.nightlyLog.collectAsStateWithLifecycle()
    val quranLog by viewModel.quranLog.collectAsStateWithLifecycle()
    val specialAmalLog by viewModel.specialAmalLog.collectAsStateWithLifecycle()
    val duaNotes by viewModel.duaNotes.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val autoHadithEnabled by viewModel.autoHadithEnabled.collectAsStateWithLifecycle()
    val generalReminder by viewModel.generalReminder.collectAsStateWithLifecycle()
    val lifetimeStartDate by viewModel.lifetimeStartDate.collectAsStateWithLifecycle()

    // Modals
    val detailsModalItem by viewModel.detailsModalItem.collectAsStateWithLifecycle()
    val reminderModalItem by viewModel.reminderModalItem.collectAsStateWithLifecycle()
    val noteModalItem by viewModel.noteModalItem.collectAsStateWithLifecycle()
    val leaderboardModalType by viewModel.leaderboardModalType.collectAsStateWithLifecycle()
    val alertMessage by viewModel.alertMessage.collectAsStateWithLifecycle()
    val showResetDialog by viewModel.showResetDialog.collectAsStateWithLifecycle()

    // Back handler
    BackHandler(enabled = activeTab != "salah") {
        viewModel.setActiveTab("salah")
    }

    val isViewingToday = viewModel.isViewingToday()

    // Date display string
    val dateDisplay = remember(currentViewingDate, isViewingToday) {
        if (isViewingToday) {
            "আজকের দিন"
        } else {
            try {
                val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(currentViewingDate)
                if (parsed != null) {
                    SimpleDateFormat("d MMMM yyyy", Locale("bn", "BD")).format(parsed)
                } else currentViewingDate
            } catch (_: Exception) {
                currentViewingDate
            }
        }
    }

    // Dynamic header info
    val (headerTitle, headerSubtitle) = when (activeTab) {
        "salah" -> {
            val fardCompleted = listOf("fajr", "zuhr", "asr", "maghrib", "isha").count { salahLog[it] == true }
            Pair("সালাত ও তাহাজ্জুদ", "$fardCompleted/৫ ফরয সম্পন্ন")
        }
        "quran" -> Pair("দৈনিক আমল ও কুরআন", "আজকের পয়েন্ট: ${appStats.todayPoints}")
        "dhikr" -> {
            val dhikrCompleted = dhikrLog.values.count { it }
            Pair("যিকির চেকলিস্ট", "$dhikrCompleted/৮ সম্পন্ন")
        }
        "duaNotes" -> Pair("গুরুত্বপূর্ণ দু’আ/নোট", "${duaNotes.size} টি নোট")
        "reports" -> Pair("সারাংশ ও ইতিহাস", "মোট পয়েন্ট: ${appStats.totalLifetimePoints}")
        "settings" -> Pair("সেটিংস ও তথ্য", "অ্যাপ্লিকেশন নিয়ন্ত্রণ")
        else -> Pair("ইবাদত ট্র্যাকার", "আজকের পয়েন্ট: ${appStats.todayPoints}")
    }

    val navItems = remember {
        listOf(
            NavItem("salah", "সালাত", Icons.Outlined.Home, Icons.Filled.Home),
            NavItem("quran", "আমল", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
            NavItem("dhikr", "যিকির", Icons.Outlined.Bolt, Icons.Filled.Bolt),
            NavItem("duaNotes", "নোট", Icons.Outlined.StickyNote2, Icons.Filled.StickyNote2),
            NavItem("reports", "রিপোর্ট", Icons.Outlined.BarChart, Icons.Filled.BarChart),
            NavItem("settings", "সেটিংস", Icons.Outlined.Settings, Icons.Filled.Settings)
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        topBar = {
            AppHeader(
                title = headerTitle,
                subtitle = headerSubtitle,
                dateDisplay = dateDisplay,
                isViewingToday = isViewingToday,
                onGoToToday = { viewModel.goToToday() }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = Color.White,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEach { item ->
                        val isSelected = activeTab == item.id
                        val iconColor = if (isSelected) Color.White else TextMuted
                        val labelColor = if (isSelected) TealPrimary else TextMuted

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setActiveTab(item.id) }
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) TealPrimary else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.icon,
                                    contentDescription = item.label,
                                    tint = iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = labelColor
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundLight)
        ) {
            when (activeTab) {
                "salah" -> SalahScreen(
                    viewModel = viewModel,
                    appStats = appStats,
                    yesterdayChampion = yesterdayChampion,
                    salahLog = salahLog,
                    nawafilLog = nawafilLog,
                    reminders = reminders,
                    isViewingToday = isViewingToday
                )
                "quran" -> AmalScreen(
                    viewModel = viewModel,
                    appStats = appStats,
                    specialAmalLog = specialAmalLog,
                    morningLog = morningLog,
                    nightlyLog = nightlyLog,
                    quranLog = quranLog,
                    reminders = reminders
                )
                "dhikr" -> DhikrScreen(
                    viewModel = viewModel,
                    dhikrLog = dhikrLog,
                    reminders = reminders
                )
                "duaNotes" -> NotesScreen(
                    viewModel = viewModel,
                    duaNotes = duaNotes
                )
                "reports" -> ReportsScreen(
                    viewModel = viewModel,
                    currentViewingDate = currentViewingDate
                )
                "settings" -> SettingsScreen(
                    viewModel = viewModel,
                    userName = userName,
                    appStats = appStats,
                    autoHadithEnabled = autoHadithEnabled,
                    generalReminder = generalReminder,
                    lifetimeStartDate = lifetimeStartDate,
                    isViewingToday = isViewingToday
                )
            }
        }
    }

    // Modal Dialogs
    detailsModalItem?.let { (title, content) ->
        DetailsDialog(
            title = title,
            content = content,
            onDismiss = { viewModel.closeDetails() }
        )
    }

    reminderModalItem?.let { (key, name) ->
        val existing = reminders[key]
        ReminderDialog(
            activityKey = key,
            activityName = name,
            initialSetting = existing,
            onSave = { k, n, start, count, interval ->
                viewModel.saveReminder(k, n, start, count, interval)
            },
            onDelete = { k, n ->
                viewModel.deleteReminder(k, n)
            },
            onDismiss = { viewModel.closeReminder() }
        )
    }

    noteModalItem?.let { note ->
        NoteDetailDialog(
            note = note,
            onDelete = { id -> viewModel.deleteNote(id) },
            onDismiss = { viewModel.closeNoteDetail() }
        )
    }

    leaderboardModalType?.let { (metric, title) ->
        val list = remember(metric) { viewModel.getLeaderboardList(metric) }
        LeaderboardDialog(
            title = title,
            users = list,
            onDismiss = { viewModel.closeLeaderboard() }
        )
    }

    alertMessage?.let { (msg, isSuccess) ->
        CustomAlertDialog(
            title = if (isSuccess) "সফল!" else "সতর্কতা!",
            message = msg,
            isSuccess = isSuccess,
            onDismiss = { viewModel.dismissAlert() }
        )
    }

    if (showResetDialog) {
        CustomAlertDialog(
            title = "ডেটা মুছে ফেলা হবে!",
            message = "আপনি কি নিশ্চিত যে আপনি সমস্ত অ্যাপ্লিকেশন ডেটা মুছে ফেলতে চান? এই কাজটি ফিরিয়ে আনা যাবে না।",
            isSuccess = false,
            isConfirm = true,
            onConfirm = { viewModel.confirmReset() },
            onDismiss = { viewModel.cancelReset() }
        )
    }
}
