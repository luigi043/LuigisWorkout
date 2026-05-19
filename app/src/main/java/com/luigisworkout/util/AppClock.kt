package com.luigisworkout.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Centralized date/time helper. Single source of truth for "today" so we don't capture
 * a stale date at construction time and miss midnight rollovers.
 *
 * Uses java.time.LocalDate (API 26+, matches our minSdk) — thread-safe, allocation-free
 * compared to SimpleDateFormat.
 */
object AppClock {

    private val isoFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val zone: ZoneId get() = ZoneId.systemDefault()

    /** "yyyy-MM-dd" — the canonical date string used in every Room entity. */
    fun todayString(): String = LocalDate.now(zone).format(isoFormatter)

    fun dateXDaysAgoString(days: Int): String =
        LocalDate.now(zone).minusDays(days.toLong()).format(isoFormatter)

    /** Long human-readable header for greetings, e.g. "Tuesday, May 19". */
    fun longDateHeader(locale: Locale = Locale.ENGLISH): String =
        DateTimeFormatter.ofPattern("EEEE, MMMM d", locale).format(LocalDate.now(zone))
}

/**
 * LiveData<String> that emits the current ISO date and re-emits whenever the system
 * reports midnight, a manual time change, or a timezone change.
 *
 * Hand this to ViewModels and use [androidx.lifecycle.Transformations.switchMap] for
 * any "today" query — that way the underlying Room query is re-issued on date rollover
 * instead of forever querying yesterday.
 */
class TodayDateLiveData(context: Context) : LiveData<String>() {

    private val appContext = context.applicationContext

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            val next = AppClock.todayString()
            if (next != value) postValue(next)
        }
    }

    override fun onActive() {
        value = AppClock.todayString()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        // Date-change broadcasts are system-protected; no exported flag needed.
        appContext.registerReceiver(receiver, filter)
    }

    override fun onInactive() {
        runCatching { appContext.unregisterReceiver(receiver) }
    }
}
