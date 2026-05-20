package com.luigisworkout.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.luigisworkout.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Helpers for opening a MuscleWiki example/demo for a given exercise.
 *
 * We build a search URL rather than hardcoding per-exercise slugs — musclewiki.com
 * doesn't expose a stable predictable slug pattern (`barbell-back-squat` vs
 * `barbell-bench-press` vs `goblet-squats` etc.), and the search page consistently
 * lands the user one click away from a video demo for any variant.
 */
object MuscleWiki {

    private const val BASE = "https://musclewiki.com/?s="

    /** Returns a MuscleWiki search URL for [exerciseName] (URL-encoded). */
    fun searchUrl(exerciseName: String): String =
        BASE + URLEncoder.encode(exerciseName.trim(), StandardCharsets.UTF_8.name())

    /**
     * Open the MuscleWiki search page for [exerciseName] in the user's browser.
     * Shows a toast if no browser is installed (very rare on real devices, but
     * possible on stripped-down emulators).
     */
    fun openExample(context: Context, exerciseName: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl(exerciseName)))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, R.string.no_browser_available, Toast.LENGTH_SHORT).show()
        }
    }
}
