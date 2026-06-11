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
     * Injected into the search-results page so the embedded example viewer lands
     * directly on the exercise instead of the results list.
     *
     * Two strategies, first one to succeed wins:
     *  1. DOM: poll for the first result link to an exercise page
     *     (`/{equipment}/{male|female}/{muscle}/{slug}` — 4+ path segments).
     *  2. API fallback (kicks in after ~2.4s): same-origin call to
     *     `/newapi/exercise/exercises/` and build the page URL from the
     *     category + primary muscle + slug of the top hit.
     * If neither finds anything (e.g. no results), the search page stays up.
     */
    val AUTO_OPEN_FIRST_RESULT_JS = """
        (function() {
          if (window.__lwAutoOpened) return;
          var done = false;
          function go(url) {
            if (!done && url) { done = true; window.__lwAutoOpened = true; location.href = url; }
          }
          function findLink() {
            var as = document.querySelectorAll('a[href*="/male/"], a[href*="/female/"]');
            for (var i = 0; i < as.length; i++) {
              var p = as[i].pathname || '';
              if (p.split('/').filter(Boolean).length >= 4) return as[i].href;
            }
            return null;
          }
          var tries = 0;
          var t = setInterval(function() {
            tries++;
            var link = findLink();
            if (link) { clearInterval(t); go(link); return; }
            if (tries === 6) {
              var q = new URLSearchParams(location.search).get('s') || '';
              fetch('/newapi/exercise/exercises/?limit=1&status=Published&name=' + encodeURIComponent(q), { headers: { Accept: 'application/json' } })
                .then(function(r) { return r.json(); })
                .then(function(j) {
                  var ex = (j.results || [])[0];
                  if (!ex || !ex.slug) return;
                  var cat = ((ex.category && (ex.category.url_name || ex.category.name)) || '')
                    .toString().toLowerCase().replace(/\s+/g, '-');
                  var mus = ((ex.muscles_primary && ex.muscles_primary[0] &&
                    (ex.muscles_primary[0].url_name || ex.muscles_primary[0].name)) || '')
                    .toString().toLowerCase().replace(/\s+/g, '-');
                  if (cat && mus) { clearInterval(t); go('/' + cat + '/male/' + mus + '/' + ex.slug); }
                })
                .catch(function() {});
            }
            if (tries > 20) clearInterval(t);
          }, 400);
        })();
    """.trimIndent()

    /** True when [url] is the MuscleWiki search-results page. */
    fun isSearchUrl(url: String?): Boolean =
        url != null && url.contains("musclewiki.com") && (url.contains("?s=") || url.contains("/search"))

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
