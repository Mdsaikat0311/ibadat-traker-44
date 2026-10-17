package com.example.data.model

data class PrayerItem(
    val key: String,
    val name: String,
    val type: String, // "fard" or "tahajjud_custom"
    val rakat: String,
    val details: String,
    val point: Int,
    val hasDua: Boolean = (type == "fard"),
    val hasMorningZikir: Boolean = (key == "fajr"),
    val hasEveningZikir: Boolean = (key == "maghrib")
)

data class NafilPrayerItem(
    val key: String,
    val name: String,
    val rakat: String,
    val info: String,
    val details: String,
    val point: Int = 10
)

data class RoutineTaskItem(
    val key: String,
    val name: String,
    val target: Int,
    val point: Int,
    val details: String,
    val arabic: String? = null
)

data class DuaNote(
    val id: String,
    val title: String,
    val text: String,
    val date: String,
    val isAuto: Boolean = false
)

data class ReminderSetting(
    val key: String,
    val name: String,
    val start: String, // "06:00"
    val count: Int,    // 1-10
    val interval: Int  // minutes (10, 15, etc.)
)

data class GeneralReminderSetting(
    val enabled: Boolean = true,
    val interval: Int = 15 // minutes
)

data class TaskStats(
    val week: Int,
    val month: Int,
    val lifetime: Int,
    val totalWeekDays: Int = 7,
    val totalMonthDays: Int = 30,
    val totalLifetimeDays: Int
)

data class LeaderboardUser(
    val id: String,
    val name: String,
    val points: Int,
    val rank: Int = 0,
    val isCurrentUser: Boolean = false
)

data class AppStats(
    val todayPoints: Int = 0,
    val selectedDayPoints: Int = 0,
    val yesterdayPoints: Int = 0,
    val totalPoints7Days: Int = 0,
    val totalPoints30Days: Int = 0,
    val highScore30Days: Int = 0,
    val fardCompleted7Days: Int = 0,
    val totalLifetimePoints: Int = 0
)

data class ChampionInfo(
    val name: String,
    val points: Int
)
