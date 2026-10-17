package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.IbadatRepository
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class IbadatViewModel(application: Application) : AndroidViewModel(application) {

    val repository = IbadatRepository(application)
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val _currentViewingDate = MutableStateFlow(repository.getTodayKey())
    val currentViewingDate: StateFlow<String> = _currentViewingDate.asStateFlow()

    private val _activeTab = MutableStateFlow("salah")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    private val _appStats = MutableStateFlow(repository.getAppStats(_currentViewingDate.value))
    val appStats: StateFlow<AppStats> = _appStats.asStateFlow()

    private val _yesterdayChampion = MutableStateFlow(repository.getYesterdayChampion())
    val yesterdayChampion: StateFlow<ChampionInfo> = _yesterdayChampion.asStateFlow()

    private val _salahLog = MutableStateFlow(repository.getLog("salah", _currentViewingDate.value))
    val salahLog: StateFlow<Map<String, Boolean>> = _salahLog.asStateFlow()

    private val _nawafilLog = MutableStateFlow(repository.getLog("nawafil", _currentViewingDate.value))
    val nawafilLog: StateFlow<Map<String, Boolean>> = _nawafilLog.asStateFlow()

    private val _dhikrLog = MutableStateFlow(repository.getLog("dhikr", _currentViewingDate.value))
    val dhikrLog: StateFlow<Map<String, Boolean>> = _dhikrLog.asStateFlow()

    private val _morningLog = MutableStateFlow(repository.getLog("morning", _currentViewingDate.value))
    val morningLog: StateFlow<Map<String, Boolean>> = _morningLog.asStateFlow()

    private val _nightlyLog = MutableStateFlow(repository.getLog("nightly", _currentViewingDate.value))
    val nightlyLog: StateFlow<Map<String, Boolean>> = _nightlyLog.asStateFlow()

    private val _quranLog = MutableStateFlow(repository.getLog("quran", _currentViewingDate.value))
    val quranLog: StateFlow<Map<String, Boolean>> = _quranLog.asStateFlow()

    private val _specialAmalLog = MutableStateFlow(repository.getLog("specialAmal", _currentViewingDate.value))
    val specialAmalLog: StateFlow<Map<String, Boolean>> = _specialAmalLog.asStateFlow()

    private val _duaNotes = MutableStateFlow(repository.getDuaNotes())
    val duaNotes: StateFlow<List<DuaNote>> = _duaNotes.asStateFlow()

    private val _reminders = MutableStateFlow(repository.getReminders())
    val reminders: StateFlow<Map<String, ReminderSetting>> = _reminders.asStateFlow()

    private val _userName = MutableStateFlow(repository.getUserName())
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _autoHadithEnabled = MutableStateFlow(repository.isAutoHadithEnabled())
    val autoHadithEnabled: StateFlow<Boolean> = _autoHadithEnabled.asStateFlow()

    private val _generalReminder = MutableStateFlow(repository.getGeneralReminder())
    val generalReminder: StateFlow<GeneralReminderSetting> = _generalReminder.asStateFlow()

    private val _lifetimeStartDate = MutableStateFlow(repository.getLifetimeStartDate())
    val lifetimeStartDate: StateFlow<String> = _lifetimeStartDate.asStateFlow()

    // Modals
    private val _detailsModalItem = MutableStateFlow<Pair<String, String>?>(null)
    val detailsModalItem: StateFlow<Pair<String, String>?> = _detailsModalItem.asStateFlow()

    private val _reminderModalItem = MutableStateFlow<Pair<String, String>?>(null)
    val reminderModalItem: StateFlow<Pair<String, String>?> = _reminderModalItem.asStateFlow()

    private val _noteModalItem = MutableStateFlow<DuaNote?>(null)
    val noteModalItem: StateFlow<DuaNote?> = _noteModalItem.asStateFlow()

    private val _leaderboardModalType = MutableStateFlow<Pair<String, String>?>(null)
    val leaderboardModalType: StateFlow<Pair<String, String>?> = _leaderboardModalType.asStateFlow()

    private val _alertMessage = MutableStateFlow<Pair<String, Boolean>?>(null)
    val alertMessage: StateFlow<Pair<String, Boolean>?> = _alertMessage.asStateFlow()

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        val dateKey = _currentViewingDate.value
        _salahLog.value = repository.getLog("salah", dateKey)
        _nawafilLog.value = repository.getLog("nawafil", dateKey)
        _dhikrLog.value = repository.getLog("dhikr", dateKey)
        _morningLog.value = repository.getLog("morning", dateKey)
        _nightlyLog.value = repository.getLog("nightly", dateKey)
        _quranLog.value = repository.getLog("quran", dateKey)
        _specialAmalLog.value = repository.getLog("specialAmal", dateKey)
        _appStats.value = repository.getAppStats(dateKey)
        _yesterdayChampion.value = repository.getYesterdayChampion()
        _duaNotes.value = repository.getDuaNotes()
        _reminders.value = repository.getReminders()
        _userName.value = repository.getUserName()
        _autoHadithEnabled.value = repository.isAutoHadithEnabled()
        _generalReminder.value = repository.getGeneralReminder()
        _lifetimeStartDate.value = repository.getLifetimeStartDate()
    }

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    fun isViewingToday(): Boolean {
        return _currentViewingDate.value == repository.getTodayKey()
    }

    fun selectDate(dateKey: String) {
        val today = repository.getTodayKey()
        if (dateKey > today) {
            showAlert("ভবিষ্যতের কোনো দিনের তথ্য দেখা বা আপডেট করা সম্ভব নয়।", isSuccess = false)
            return
        }
        _currentViewingDate.value = dateKey
        refreshAll()
    }

    fun moveDate(daysOffset: Int) {
        val current = isoFormat.parse(_currentViewingDate.value) ?: Date()
        val cal = Calendar.getInstance().apply {
            time = current
            add(Calendar.DAY_OF_YEAR, daysOffset)
        }
        val target = isoFormat.format(cal.time)
        val today = repository.getTodayKey()
        if (target > today) {
            showAlert("আগামীকালের ডেটা দেখা বা আপডেট করা সম্ভব নয়।", isSuccess = false)
            return
        }
        _currentViewingDate.value = target
        refreshAll()
    }

    fun goToToday() {
        _currentViewingDate.value = repository.getTodayKey()
        refreshAll()
    }

    fun toggleSalah(key: String) {
        val dateKey = _currentViewingDate.value
        if (dateKey > repository.getTodayKey()) return
        repository.toggleTask("salah", dateKey, key)
        refreshAll()
    }

    fun toggleNawafil(key: String) {
        val dateKey = _currentViewingDate.value
        if (dateKey > repository.getTodayKey()) return
        repository.toggleTask("nawafil", dateKey, key)
        refreshAll()
    }

    fun toggleDhikr(key: String) {
        val dateKey = _currentViewingDate.value
        if (dateKey > repository.getTodayKey()) return
        repository.toggleTask("dhikr", dateKey, key)
        refreshAll()
    }

    fun toggleRoutineTask(category: String, taskKey: String) {
        val dateKey = _currentViewingDate.value
        if (dateKey > repository.getTodayKey()) return
        repository.toggleTask(category, dateKey, taskKey)
        refreshAll()
    }

    fun getTaskStats(category: String, taskKey: String): TaskStats {
        return repository.getTaskCompletionStats(category, taskKey)
    }

    // Modal controls
    fun openDetails(name: String, details: String) {
        _detailsModalItem.value = Pair(name, details)
    }

    fun closeDetails() {
        _detailsModalItem.value = null
    }

    fun openReminder(key: String, name: String) {
        _reminderModalItem.value = Pair(key, name)
    }

    fun closeReminder() {
        _reminderModalItem.value = null
    }

    fun saveReminder(key: String, name: String, start: String, count: Int, interval: Int) {
        val setting = ReminderSetting(key, name, start, count, interval)
        repository.saveReminder(setting)
        _reminders.value = repository.getReminders()
        closeReminder()
        showAlert("$name-এর রিমাইন্ডার সফলভাবে সেট হয়েছে!", isSuccess = true)
    }

    fun deleteReminder(key: String, name: String) {
        repository.deleteReminder(key)
        _reminders.value = repository.getReminders()
        closeReminder()
        showAlert("$name-এর রিমাইন্ডার বন্ধ করা হয়েছে।", isSuccess = true)
    }

    fun openNoteDetail(note: DuaNote) {
        _noteModalItem.value = note
    }

    fun closeNoteDetail() {
        _noteModalItem.value = null
    }

    fun addNote(title: String, text: String) {
        if (text.isBlank()) {
            showAlert("অনুগ্রহ করে নোটের মূল বক্তব্য লিখুন।", isSuccess = false)
            return
        }
        val header = if (title.isBlank()) {
            SimpleDateFormat("d MMMM yyyy", Locale("bn", "BD")).format(Date())
        } else title
        repository.addDuaNote(header, text)
        _duaNotes.value = repository.getDuaNotes()
        showAlert("নোটটি সফলভাবে সংরক্ষিত হয়েছে!", isSuccess = true)
    }

    fun deleteNote(id: String) {
        repository.deleteDuaNote(id)
        _duaNotes.value = repository.getDuaNotes()
        closeNoteDetail()
        showAlert("নোট মুছে ফেলা হয়েছে।", isSuccess = true)
    }

    fun openLeaderboard(metricType: String, title: String) {
        _leaderboardModalType.value = Pair(metricType, title)
    }

    fun closeLeaderboard() {
        _leaderboardModalType.value = null
    }

    fun getLeaderboardList(metricType: String): List<LeaderboardUser> {
        return repository.getLeaderboard(metricType)
    }

    fun getMyRank(metricType: String): Int {
        val list = repository.getLeaderboard(metricType)
        return list.firstOrNull { it.isCurrentUser }?.rank ?: 1
    }

    fun toggleAutoHadith() {
        val next = !_autoHadithEnabled.value
        repository.setAutoHadithEnabled(next)
        _autoHadithEnabled.value = next
        if (next) {
            showAlert("চালু হয়েছে! প্রতি ৩০ মিনিট পর পর হাদিস ও নসিহত রিমাইন্ডার আসবে।", isSuccess = true)
        } else {
            showAlert("অটোমেটিক হাদিস রিমাইন্ডার বন্ধ করা হয়েছে।", isSuccess = false)
        }
    }

    fun toggleGeneralReminder() {
        val current = _generalReminder.value
        val next = current.copy(enabled = !current.enabled)
        repository.setGeneralReminder(next)
        _generalReminder.value = next
        if (next.enabled) {
            showAlert("চালু হয়েছে! প্রতি ${next.interval} মিনিট পর পর যিকির রিমাইন্ডার আসবে।", isSuccess = true)
        } else {
            showAlert("সাধারণ যিকির রিমাইন্ডার বন্ধ করা হয়েছে।", isSuccess = false)
        }
    }

    fun setGeneralInterval(interval: Int) {
        val current = _generalReminder.value
        val next = current.copy(interval = interval)
        repository.setGeneralReminder(next)
        _generalReminder.value = next
        showAlert("সময় ব্যবধান পরিবর্তন হয়েছে: $interval মিনিট পর পর।", isSuccess = true)
    }

    fun setLifetimeStartDate(dateStr: String) {
        val today = repository.getTodayKey()
        if (dateStr > today) {
            showAlert("ভবিষ্যতের কোনো তারিখ নির্বাচন করা যাবে না।", isSuccess = false)
            return
        }
        repository.setLifetimeStartDate(dateStr)
        _lifetimeStartDate.value = dateStr
        refreshAll()
        showAlert("লাইফটাইম পয়েন্ট কাউন্ট শুরু $dateStr তারিখ থেকে সেট করা হয়েছে।", isSuccess = true)
    }

    fun setUserName(name: String) {
        if (name.isNotBlank()) {
            repository.setUserName(name)
            _userName.value = name
            refreshAll()
            showAlert("প্রোফাইল নাম আপডেট হয়েছে!", isSuccess = true)
        }
    }

    fun requestReset() {
        _showResetDialog.value = true
    }

    fun cancelReset() {
        _showResetDialog.value = false
    }

    fun confirmReset() {
        _showResetDialog.value = false
        repository.resetAllData()
        _currentViewingDate.value = repository.getTodayKey()
        refreshAll()
        showAlert("অ্যাপের সকল পূর্ববর্তী ডেটা মুছে ফেলা হয়েছে।", isSuccess = true)
    }

    fun showAlert(message: String, isSuccess: Boolean) {
        _alertMessage.value = Pair(message, isSuccess)
    }

    fun dismissAlert() {
        _alertMessage.value = null
    }
}
