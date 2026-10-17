package com.example.data.repository

import android.content.Context
import com.example.data.HadithBank
import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class IbadatRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("ibadat_tracker_prefs", Context.MODE_PRIVATE)
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    companion object {
        val PRAYERS = listOf(
            PrayerItem(
                key = "fajr",
                name = "ফজর",
                type = "fard",
                rakat = "২",
                point = 10,
                details = "ফজর সালাতের ফযিলত অনেক। রাসুল (সা.) বলেছেন, 'যে ব্যক্তি ফজরের নামাজ জামাতের সাথে আদায় করল, সে যেন সারা রাত ইবাদত করল।' (সহীহ মুসলিম)"
            ),
            PrayerItem(
                key = "zuhr",
                name = "যোহর",
                type = "fard",
                rakat = "৪",
                point = 10,
                details = "যোহরের ওয়াক্ত শুরু হয় সূর্য পশ্চিমাকাশে ঢলে পড়ার পর থেকে। এটি দিনের মধ্যভাগের অত্যন্ত বরকতময় ও গুরুত্বপূর্ণ ইবাদত।"
            ),
            PrayerItem(
                key = "asr",
                name = "আছর",
                type = "fard",
                rakat = "৪",
                point = 10,
                details = "আছরের সালাত ছুটে গেলে রাসুল (সা.) এমনভাবে কষ্ট পেতেন যেন তাঁর পরিবার ও সম্পদ ধ্বংস হয়ে গেছে। কুরআনে একে 'মধ্যবর্তী সালাত' (সালাতুল উসতা) বলা হয়েছে।"
            ),
            PrayerItem(
                key = "maghrib",
                name = "মাগরিব",
                type = "fard",
                rakat = "৩",
                point = 10,
                details = "মাগরিবের সালাত সূর্য ডোবার পরপরই আদায় করা উত্তম। বিলম্ব করা মাকরুহ। মাগরিবের পর সন্ধ্যার মা'ছুর দু'আগুলো পাঠ করা সুন্নত।"
            ),
            PrayerItem(
                key = "isha",
                name = "এশা",
                type = "fard",
                rakat = "৪",
                point = 10,
                details = "এশার সালাত জামাতে আদায় করলে অর্ধেক রাত ইবাদতের সওয়াব পাওয়া যায়। আর এশা ও ফজর দুটিই আদায় করলে সারা রাত নফল পড়ার সওয়াব হয়। (সহীহ মুসলিম)"
            ),
            PrayerItem(
                key = "tahajjud",
                name = "তাহাজ্জুদ",
                type = "tahajjud_custom",
                rakat = "২-৮",
                point = 20,
                details = "তাহাজ্জুদ হলো মুমিনের সম্মানের প্রতীক। আল্লাহ তায়ালা শেষ রাতে প্রথম আসমানে নেমে আসেন এবং বান্দার দু'আ কবুল করেন। (বুখারী ও মুসলিম)"
            )
        )

        val NAWAFIL_PRAYERS = listOf(
            NafilPrayerItem(
                key = "ishraq",
                name = "ইশরাক (চাশত)",
                rakat = "২-৪",
                info = "সূর্যোদয়ের ১৫ মিনিট পর",
                details = "ফজরের পর সূর্যোদয় পর্যন্ত যিকির করে ২ রাকাত ইশরাক পড়লে একটি কবুল হজ্জ ও ওমরাহর সওয়াব পাওয়া যায়। (তিরমিযি)"
            ),
            NafilPrayerItem(
                key = "awwabin",
                name = "আউয়াবিন",
                rakat = "৬",
                info = "মাগরিব ও এশার মাঝে",
                details = "মাগরিবের পর ৬ রাকাত নফল নামাজকে আউয়াবিন বলা হয়। একে গাফিলদের সময়ের অনন্য ইবাদতও বলা হয়।"
            ),
            NafilPrayerItem(
                key = "istikhara",
                name = "সালাতুল ইস্তিখারা",
                rakat = "২",
                info = "সিদ্ধান্ত নিতে দ্বিধায়",
                details = "যখন কোনো বৈধ বিষয়ে সঠিক সিদ্ধান্ত নিতে দ্বিধায় ভোগেন, তখন সুন্নাত দু'আসহ এই সালাতটি আদায় করা হয়।"
            ),
            NafilPrayerItem(
                key = "mashjid",
                name = "তাহিয়্যাতুল মসজিদ",
                rakat = "২",
                info = "মসজিদে প্রবেশ করলে",
                details = "মসজিদে প্রবেশ করলেই বসার পূর্বে ২ রাকাত সালাত আদায় করা সুন্নাত। এটি হলো আল্লাহর ঘরকে অভিবাদন জানানো।"
            ),
            NafilPrayerItem(
                key = "oju",
                name = "সুন্নাতুল ওযু",
                rakat = "২",
                info = "ওযুর পর",
                details = "উত্তমরূপে ওযু করার পর একাগ্রচিত্তে এই ২ রাকাত সালাত আদায় করলে জান্নাত ওয়াজিব হতে পারে।"
            )
        )

        val SPECIAL_AMAL_TASKS = listOf(
            RoutineTaskItem(
                key = "daily_charity",
                name = "দান ও সদাকা",
                target = 1,
                point = 10,
                details = "রাসুল (সা.) বলেছেন, দান সম্পদ কমায় না। প্রতিদিন সাধ্যমতো দান করুন অথবা মানুষের প্রতি সদাচরণ ও উপকার করুন, এটিও সদাকা। (সহীহ মুসলিম)"
            )
        )

        val MORNING_TASKS = listOf(
            RoutineTaskItem(
                key = "morning_dhikr",
                name = "সারা দিন যিকির ও তওবা",
                target = 100000,
                point = 20,
                details = "আল্লাহর স্মরণ মুমিনের হৃদয়কে সর্বদা প্রশান্ত করে। দিনে যত বেশি সম্ভব ইস্তিগফার ও যিকির করা উচিত।"
            ),
            RoutineTaskItem(
                key = "morning_wake_up_dua",
                name = "ঘুম থেকে উঠার দু'আ",
                target = 1,
                point = 5,
                details = "হাদিসে এসেছে, ঘুম থেকে জেগে আল্লাহর প্রশংসা করা এবং ক্ষমা চাওয়া দু'আ কবুলের সুসংবাদ। (সহীহ বুখারী: ১১৫৪)"
            ),
            RoutineTaskItem(
                key = "allah_100_times",
                name = "সকালে ২ পেজ কুরআন পড়া",
                target = 1,
                point = 10,
                details = "দিনের শুভ সূচনা পবিত্র কুরআন তিলাওয়াত দিয়ে করলে সারাদিন অফুরন্ত বরকত থাকে।"
            ),
            RoutineTaskItem(
                key = "morning_3_qul_hashr",
                name = "৩ কুল ও সূরা হাশরের শেষ ৩ আয়াত",
                target = 2,
                point = 10,
                details = "সকাল ও সন্ধ্যায় সূরা ইখলাস, ফালাক্ব ও নাস ৩ বার করে পাঠ করলে সকল বালা-মুসিবত থেকে নিরাপত্তা মিলে।"
            ),
            RoutineTaskItem(
                key = "durud_sarif",
                name = "দরূদ শরীফ",
                target = 100,
                point = 10,
                details = "যে ব্যক্তি রাসুল (সা.)-এর ওপর একবার দরুদ পাঠ করে, আল্লাহ তার ওপর ১০টি রহমত নাযিল করেন। (সহীহ মুসলিম)"
            ),
            RoutineTaskItem(
                key = "big_nek",
                name = "সুবহা-নাল্লা-হি ওয়া বিহামদিহী, সুবহা-নাল্লা-হিল আযীম।",
                target = 100,
                point = 15,
                details = "সকালে ১০০ বার পড়লে কিয়ামতের দিন মিজানের পাল্লায় তা অনেক ভারী হবে এবং পাপ মোচন হবে।"
            ),
            RoutineTaskItem(
                key = "lahul_mulku",
                name = "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু...",
                target = 100,
                point = 20,
                details = "যে ব্যক্তি দিনে এটি ১০০ বার পড়বে, সে ১০টি গোলাম আযাদ করার সওয়াব পাবে এবং ১০০টি নেকি লেখা হবে।"
            )
        )

        val NIGHTLY_TASKS = listOf(
            RoutineTaskItem(
                key = "nightly_2_rakat_salah",
                name = "ঘুমানোর আগে ২ রাকাত সালাত",
                target = 1,
                point = 10,
                details = "পবিত্র ওযু করে ২ রাকাত নফল সালাত আদায় করে ঘুমানো সুন্নাত। ফেরেশতারা সারারাত তার জন্য দু'আ করতে থাকে।"
            ),
            RoutineTaskItem(
                key = "nightly_sleep_dua",
                name = "ঘুমের সব দু'আ",
                target = 1,
                point = 10,
                details = "দু'আ: আল্লাহুম্মা বিসমিকা আমূতু ওয়া আহ্ইয়া। সাথে তাসবীহে ফাতেমী (৩৩ সুবহানাল্লাহ, ৩৩ আলহামদুলিল্লাহ, ৩৪ আল্লাহু আকবার)।"
            ),
            RoutineTaskItem(
                key = "nightly_surah_baqarah",
                name = "সূরা বাকারার শেষ ২ আয়াত",
                target = 1,
                point = 5,
                details = "যে ব্যক্তি রাতে সূরা বাকারার শেষ দুই আয়াত (আ-মানার রসূলু...) তিলাওয়াত করবে, তা তার সকল অনিষ্ট থেকে বাঁচার জন্য যথেষ্ট হবে। (বুখারী)"
            ),
            RoutineTaskItem(
                key = "nightly_surah_mulk",
                name = "সূরা মুলক",
                target = 1,
                point = 15,
                details = "সূরা মুলক কবরের আযাব থেকে মুক্তি দানকারী সূরা। রাসুল (সা.) এটি তিলাওয়াত না করে ঘুমাতেন না।"
            ),
            RoutineTaskItem(
                key = "father_mother",
                name = "পিতা-মাতার জন্য দু'আ (৩ বার)",
                target = 3,
                point = 2,
                details = "'রব্বির হামহুমা কামা রব্বায়ানী সগীরা'। পিতা-মাতার জন্য দু'আ করা সুসন্তানের সর্বোত্তম আমল।"
            )
        )

        val QURAN_TASKS = listOf(
            RoutineTaskItem(
                key = "quran_mulk",
                name = "সূরা মুলক",
                target = 1,
                point = 10,
                details = "প্রতি রাতে সূরা মুলক তিলাওয়াত করা কবরের পরীক্ষা ও আযাব থেকে মুক্তির বড় উপায়।"
            ),
            RoutineTaskItem(
                key = "quran_ar_rahman",
                name = "সূরা আর-রহমান",
                target = 1,
                point = 10,
                details = "সূরা আর-রহমান হলো পবিত্র কুরআনের সৌন্দর্য ও অনুপম নিদর্শন।"
            ),
            RoutineTaskItem(
                key = "quran_yasin",
                name = "সূরা ইয়াছিন",
                target = 1,
                point = 10,
                details = "সূরা ইয়াসিন কুরআনের হৃদয়স্বরূপ। আন্তরিক তিলাওয়াতে পরম করুণাময়ের রহমত লাভ হয়।"
            ),
            RoutineTaskItem(
                key = "quran_kahf_20",
                name = "সূরা কাহফ (১০ আয়াত)",
                target = 1,
                point = 10,
                details = "জুমুআর দিনে বা নিয়মিত সূরা কাহফের প্রথম ও শেষ ১০ আয়াত তিলাওয়াত দাজ্জালের ফেতনা থেকে রক্ষা করে।"
            ),
            RoutineTaskItem(
                key = "quran_baqarah_10",
                name = "সূরা বাকারা (২০ আয়াত)",
                target = 1,
                point = 10,
                details = "যে ঘরে সূরা বাকারা তিলাওয়াত করা হয়, সেখান থেকে শয়তান বিতাড়িত হয়ে পালিয়ে যায়।"
            ),
            RoutineTaskItem(
                key = "quran_last_10_surah",
                name = "শেষ ১০ সূরা",
                target = 1,
                point = 10,
                details = "সালাতের জন্য ছোট সূরাগুলো বিশুদ্ধ ও শুদ্ধ তিলাওয়াতে মুখস্থ ও নিয়মিত চর্চা করা আবশ্যক।"
            )
        )

        val DHIKR_CHECKLIST_TASKS = listOf(
            RoutineTaskItem(
                key = "subhanallah_100",
                name = "সুবহানাল্লাহ (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "سُبْحَانَ ٱللَّٰهِ",
                details = "আল্লাহর পবিত্রতা ও মহিমা ঘোষণা করা। এটি মিজানের পাল্লায় অনেক বেশি ভারী।"
            ),
            RoutineTaskItem(
                key = "alhamdulillah_100",
                name = "আলহামদুলিল্লাহ (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "ٱلْحَمْدُ لِلَّٰهِ",
                details = "সমস্ত প্রশংসা আল্লাহ রাব্বুল আলামীনের জন্য। এটি সর্বোত্তম শোকর ও দু'আ।"
            ),
            RoutineTaskItem(
                key = "allahuakbar_100",
                name = "আল্লাহু আকবার (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "ٱللَّهُ أَكْبَرُ",
                details = "আল্লাহ সর্বশ্রেষ্ঠ ও মহান। অন্তরের তাকওয়া ও একাগ্রতা বৃদ্ধি করে।"
            ),
            RoutineTaskItem(
                key = "subhanallahi_100",
                name = "সুবহা-নাল্লা-হি ওয়া বিহামদিহী, সুবহা-নাল্লা-হিল আযীম। (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ",
                details = "যে ব্যক্তি দৈনিক ১০০ বার পাঠ করে, তার গুনাহ সমুদ্রের ফেনা পরিমাণ হলেও মাফ করে দেওয়া হয়।"
            ),
            RoutineTaskItem(
                key = "laailahaillallah_100",
                name = "লা ইলাহা ইল্লাল্লাহ (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "لَا إِلَٰهَ إِلَّا ٱللَّٰهُ",
                details = "সর্বশ্রেষ্ঠ কালিমাহ ও সর্বোত্তম যিকির। জান্নাতের উন্মুক্ত চাবিকাঠি।"
            ),
            RoutineTaskItem(
                key = "sayyidul_istighfar_1",
                name = "সাইয়্যিদুল ইস্তিগফার (সকাল-সন্ধ্যা)",
                target = 1,
                point = 5,
                arabic = "سَيِّدُ الِاسْتِغْفَارِ",
                details = "যে ব্যক্তি দিনে বা রাতে বিশ্বাসের সাথে এটি পড়ে মারা যাবে, সে জান্নাতি হবে। (সহীহ বুখারী)"
            ),
            RoutineTaskItem(
                key = "istighfirullah_100",
                name = "আস্তাগফিরুল্লাহ (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "أَسْتَغْفِرُ ٱللَّٰهَ",
                details = "আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি। ইস্তিগফার রিযিক বৃদ্ধি ও মুসিবত দূর করে।"
            ),
            RoutineTaskItem(
                key = "la_Hawla_100",
                name = "লা হাউলা ওয়া লা কুওয়াতা... (১০০ বার)",
                target = 1,
                point = 5,
                arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِٱللَّٰهِ",
                details = "জান্নাতের সুউচ্চ ভান্ডারসমূহের একটি অফুরন্ত রত্নভাণ্ডার।"
            )
        )
    }

    fun getTodayKey(): String {
        return isoFormat.format(Date())
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "ব্যবহারকারী") ?: "ব্যবহারকারী"
    }

    fun setUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getLifetimeStartDate(): String {
        val defaultDate = isoFormat.format(Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000))
        return prefs.getString("lifetime_start_date", defaultDate) ?: defaultDate
    }

    fun setLifetimeStartDate(date: String) {
        prefs.edit().putString("lifetime_start_date", date).apply()
    }

    fun isAutoHadithEnabled(): Boolean {
        return prefs.getBoolean("auto_hadith_enabled", true)
    }

    fun setAutoHadithEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("auto_hadith_enabled", enabled).apply()
    }

    fun getGeneralReminder(): GeneralReminderSetting {
        val enabled = prefs.getBoolean("gen_reminder_enabled", true)
        val interval = prefs.getInt("gen_reminder_interval", 15)
        return GeneralReminderSetting(enabled, interval)
    }

    fun setGeneralReminder(setting: GeneralReminderSetting) {
        prefs.edit()
            .putBoolean("gen_reminder_enabled", setting.enabled)
            .putInt("gen_reminder_interval", setting.interval)
            .apply()
    }

    // --- Task Logs Management ---
    fun getLog(category: String, dateKey: String): Map<String, Boolean> {
        val raw = prefs.getString("log_${category}_$dateKey", null) ?: return emptyMap()
        val result = mutableMapOf<String, Boolean>()
        try {
            val json = JSONObject(raw)
            json.keys().forEach { key ->
                result[key] = json.optBoolean(key, false)
            }
        } catch (_: Exception) {}
        return result
    }

    fun saveLog(category: String, dateKey: String, log: Map<String, Boolean>) {
        val json = JSONObject()
        log.forEach { (k, v) -> json.put(k, v) }
        prefs.edit().putString("log_${category}_$dateKey", json.toString()).apply()
    }

    fun toggleTask(category: String, dateKey: String, taskKey: String): Boolean {
        val current = getLog(category, dateKey).toMutableMap()
        val nextState = !(current[taskKey] ?: false)
        current[taskKey] = nextState
        saveLog(category, dateKey, current)
        return nextState
    }

    // --- Notes Management ---
    fun getDuaNotes(): List<DuaNote> {
        val raw = prefs.getString("user_dua_notes", null)
        val userNotes = mutableListOf<DuaNote>()
        if (!raw.isNullOrEmpty()) {
            try {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    userNotes.add(
                        DuaNote(
                            id = obj.getString("id"),
                            title = obj.optString("title", ""),
                            text = obj.getString("text"),
                            date = obj.getString("date"),
                            isAuto = false
                        )
                    )
                }
            } catch (_: Exception) {}
        }
        return userNotes + HadithBank.AUTO_LIBRARY_NOTES
    }

    fun addDuaNote(title: String, text: String): DuaNote {
        val id = UUID.randomUUID().toString()
        val dateStr = SimpleDateFormat("d MMMM yyyy", Locale("bn", "BD")).format(Date())
        val note = DuaNote(id = id, title = title, text = text, date = dateStr, isAuto = false)

        val existing = getUserNotesOnly().toMutableList()
        existing.add(0, note)
        saveUserNotesOnly(existing)
        return note
    }

    fun deleteDuaNote(id: String) {
        if (id.startsWith("auto_")) return
        val existing = getUserNotesOnly().filter { it.id != id }
        saveUserNotesOnly(existing)
    }

    private fun getUserNotesOnly(): List<DuaNote> {
        val raw = prefs.getString("user_dua_notes", null) ?: return emptyList()
        val userNotes = mutableListOf<DuaNote>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                userNotes.add(
                    DuaNote(
                        id = obj.getString("id"),
                        title = obj.optString("title", ""),
                        text = obj.getString("text"),
                        date = obj.getString("date"),
                        isAuto = false
                    )
                )
            }
        } catch (_: Exception) {}
        return userNotes
    }

    private fun saveUserNotesOnly(notes: List<DuaNote>) {
        val array = JSONArray()
        notes.forEach { n ->
            val obj = JSONObject()
            obj.put("id", n.id)
            obj.put("title", n.title)
            obj.put("text", n.text)
            obj.put("date", n.date)
            array.put(obj)
        }
        prefs.edit().putString("user_dua_notes", array.toString()).apply()
    }

    // --- Reminders Management ---
    fun getReminders(): Map<String, ReminderSetting> {
        val raw = prefs.getString("activity_reminders", null) ?: return emptyMap()
        val map = mutableMapOf<String, ReminderSetting>()
        try {
            val json = JSONObject(raw)
            json.keys().forEach { key ->
                val obj = json.getJSONObject(key)
                map[key] = ReminderSetting(
                    key = obj.getString("key"),
                    name = obj.getString("name"),
                    start = obj.getString("start"),
                    count = obj.getInt("count"),
                    interval = obj.getInt("interval")
                )
            }
        } catch (_: Exception) {}
        return map
    }

    fun saveReminder(setting: ReminderSetting) {
        val current = getReminders().toMutableMap()
        current[setting.key] = setting
        val json = JSONObject()
        current.forEach { (k, v) ->
            val obj = JSONObject()
            obj.put("key", v.key)
            obj.put("name", v.name)
            obj.put("start", v.start)
            obj.put("count", v.count)
            obj.put("interval", v.interval)
            json.put(k, obj)
        }
        prefs.edit().putString("activity_reminders", json.toString()).apply()
    }

    fun deleteReminder(activityKey: String) {
        val current = getReminders().toMutableMap()
        current.remove(activityKey)
        val json = JSONObject()
        current.forEach { (k, v) ->
            val obj = JSONObject()
            obj.put("key", v.key)
            obj.put("name", v.name)
            obj.put("start", v.start)
            obj.put("count", v.count)
            obj.put("interval", v.interval)
            json.put(k, obj)
        }
        prefs.edit().putString("activity_reminders", json.toString()).apply()
    }

    // --- Points Calculation ---
    fun calculatePoints(dateKey: String): Int {
        var points = 0
        val salah = getLog("salah", dateKey)
        val nawafil = getLog("nawafil", dateKey)
        val dhikr = getLog("dhikr", dateKey)
        val morning = getLog("morning", dateKey)
        val nightly = getLog("nightly", dateKey)
        val quran = getLog("quran", dateKey)
        val special = getLog("specialAmal", dateKey)

        // Salah
        PRAYERS.forEach { p ->
            if (salah[p.key] == true) {
                points += p.point
            }
            if (p.type == "fard" && salah["${p.key}_dua"] == true) {
                points += 5
            }
        }
        if (salah["fajr_morning_zikir"] == true) points += 3
        if (salah["maghrib_evening_zikir"] == true) points += 3

        // Nawafil
        NAWAFIL_PRAYERS.forEach { n ->
            if (nawafil[n.key] == true) points += n.point
        }

        // Dhikr
        DHIKR_CHECKLIST_TASKS.forEach { d ->
            if (dhikr[d.key] == true) points += d.point
        }

        // Special Amal
        SPECIAL_AMAL_TASKS.forEach { s ->
            if (special[s.key] == true) points += s.point
        }

        // Morning
        MORNING_TASKS.forEach { m ->
            if (morning[m.key] == true) points += m.point
        }

        // Nightly
        NIGHTLY_TASKS.forEach { n ->
            if (nightly[n.key] == true) points += n.point
        }

        // Quran
        QURAN_TASKS.forEach { q ->
            if (quran[q.key] == true) points += q.point
        }

        return points
    }

    fun getLifetimeDays(): Int {
        val startStr = getLifetimeStartDate()
        val startDate = try {
            isoFormat.parse(startStr) ?: Date()
        } catch (_: Exception) {
            Date()
        }
        val today = Date()
        val diff = today.time - startDate.time
        val days = TimeUnit.MILLISECONDS.toDays(diff).toInt() + 1
        return if (days < 1) 1 else days
    }

    fun getTaskCompletionStats(category: String, taskKey: String): TaskStats {
        val totalDays = getLifetimeDays()
        val cal = Calendar.getInstance()
        var weekCount = 0
        var monthCount = 0
        var lifetimeCount = 0

        for (i in 0 until totalDays.coerceAtMost(365)) {
            val d = Calendar.getInstance().apply {
                time = cal.time
                add(Calendar.DAY_OF_YEAR, -i)
            }
            val key = isoFormat.format(d.time)
            val log = getLog(category, key)
            if (log[taskKey] == true) {
                if (i < 7) weekCount++
                if (i < 30) monthCount++
                lifetimeCount++
            }
        }

        return TaskStats(
            week = weekCount,
            month = monthCount,
            lifetime = lifetimeCount,
            totalWeekDays = 7,
            totalMonthDays = 30,
            totalLifetimeDays = totalDays
        )
    }

    fun getAppStats(selectedDateKey: String): AppStats {
        val todayKey = getTodayKey()
        val todayPoints = calculatePoints(todayKey)
        val selectedPoints = calculatePoints(selectedDateKey)

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayKey = isoFormat.format(cal.time)
        val yesterdayPoints = calculatePoints(yesterdayKey)

        var total7 = 0
        var total30 = 0
        var highScore30 = 0
        var fardCompleted7 = 0

        val fardKeys = PRAYERS.filter { it.type == "fard" }.map { it.key }

        for (i in 0 until 30) {
            val checkCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
            val key = isoFormat.format(checkCal.time)
            val pts = calculatePoints(key)

            if (i < 7) {
                total7 += pts
                val salah = getLog("salah", key)
                fardKeys.forEach { fk ->
                    if (salah[fk] == true) fardCompleted7++
                }
            }

            total30 += pts
            if (pts > highScore30) highScore30 = pts
        }

        // Lifetime points
        var totalLifetime = 0
        val lifetimeDays = getLifetimeDays()
        for (i in 0 until lifetimeDays.coerceAtMost(365)) {
            val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
            val k = isoFormat.format(pastCal.time)
            totalLifetime += calculatePoints(k)
        }

        return AppStats(
            todayPoints = todayPoints,
            selectedDayPoints = selectedPoints,
            yesterdayPoints = yesterdayPoints,
            totalPoints7Days = total7,
            totalPoints30Days = total30,
            highScore30Days = highScore30,
            fardCompleted7Days = fardCompleted7,
            totalLifetimePoints = totalLifetime
        )
    }

    fun getYesterdayChampion(): ChampionInfo {
        // High simulated or actual champion for yesterday
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayKey = isoFormat.format(cal.time)
        val userYesterday = calculatePoints(yesterdayKey)
        
        return if (userYesterday >= 130) {
            ChampionInfo(getUserName(), userYesterday)
        } else {
            ChampionInfo("আহমাদুল্লাহ", 145)
        }
    }

    fun getLeaderboard(metricType: String): List<LeaderboardUser> {
        val userName = getUserName()
        val stats = getAppStats(getTodayKey())

        val userScore = when (metricType) {
            "todayPoints" -> stats.todayPoints
            "yesterdayPoints" -> stats.yesterdayPoints
            "totalPoints7Days" -> stats.totalPoints7Days
            "totalPoints30Days" -> stats.totalPoints30Days
            else -> stats.totalLifetimePoints
        }

        val baseContenders = when (metricType) {
            "todayPoints" -> listOf(
                "মাওলানা তারেক" to 140,
                "আব্দুল্লাহ আল-মামুন" to 125,
                "সালমান ফারিস" to 110,
                "রাশেদুল হাসান" to 95,
                "নাঈম আহমেদ" to 90,
                "ফারহান চৌধুরী" to 85,
                "তানভীর মাহমুদ" to 75,
                "জুবায়ের হোসেন" to 65
            )
            "yesterdayPoints" -> listOf(
                "আহমাদুল্লাহ" to 145,
                "মাহবুবুর রহমান" to 135,
                "মুস্তাফিজুর রহমান" to 120,
                "সাইফুল ইসলাম" to 115,
                "ইমরান খান" to 100,
                "সাকিব আল হাসান" to 95,
                "কামরুল হাসান" to 80,
                "আরিফুল হক" to 70
            )
            "totalPoints7Days" -> listOf(
                "আব্দুল্লাহ আল-মামুন" to 920,
                "মাওলানা তারেক" to 880,
                "মাহবুবুর রহমান" to 810,
                "সালমান ফারিস" to 740,
                "রাশেদুল হাসান" to 680,
                "মুস্তাফিজুর রহমান" to 620,
                "নাঈম আহমেদ" to 580,
                "সাইফুল ইসলাম" to 540
            )
            "totalPoints30Days" -> listOf(
                "আব্দুল্লাহ আল-মামুন" to 3850,
                "মাওলানা তারেক" to 3620,
                "আহমাদুল্লাহ" to 3490,
                "মাহবুবুর রহমান" to 3210,
                "সালমান ফারিস" to 2980,
                "রাশেদুল হাসান" to 2750,
                "মুস্তাফিজুর রহমান" to 2510,
                "সাইফুল ইসলাম" to 2340
            )
            else -> listOf(
                "আব্দুল্লাহ আল-মামুন" to 12450,
                "মাওলানা তারেক" to 11800,
                "আহমাদুল্লাহ" to 10950,
                "মাহবুবুর রহমান" to 9820,
                "সালমান ফারিস" to 8900,
                "রাশেদুল হাসান" to 8150,
                "মুস্তাফিজুর রহমান" to 7600,
                "সাইফুল ইসলাম" to 7100
            )
        }

        val allList = baseContenders.map { (name, pts) ->
            LeaderboardUser(UUID.randomUUID().toString(), name, pts, isCurrentUser = false)
        }.toMutableList()

        allList.add(LeaderboardUser("user_me", userName, userScore, isCurrentUser = true))
        allList.sortByDescending { it.points }

        return allList.mapIndexed { idx, item ->
            item.copy(rank = idx + 1)
        }
    }

    fun resetAllData() {
        prefs.edit().clear().apply()
    }
}
