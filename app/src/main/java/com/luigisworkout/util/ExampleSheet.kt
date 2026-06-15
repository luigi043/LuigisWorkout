package com.luigisworkout.util

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luigisworkout.R

/**
 * Shows the MuscleWiki example for an exercise inside the app, in a bottom sheet
 * with an embedded WebView, instead of bouncing the user out to the browser.
 *
 * Shared by the Workout plan and the Exercises library tab. The sheet loads the
 * MuscleWiki search page, then auto-jumps to the top exercise hit (see
 * [MuscleWiki.AUTO_OPEN_FIRST_RESULT_JS]); an "Open in browser" button is the
 * fallback if the page misbehaves.
 */
object ExampleSheet {

    fun show(context: Context, exerciseName: String) {
        val dialog = BottomSheetDialog(context)
        val sheetView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_exercise_example, null)

        val tvTitle: TextView = sheetView.findViewById(R.id.tvExampleTitle)
        val progress: ProgressBar = sheetView.findViewById(R.id.progressExample)
        val webView: WebView = sheetView.findViewById(R.id.webExample)
        val btnBrowser: Button = sheetView.findViewById(R.id.btnOpenInBrowser)

        tvTitle.text = exerciseName

        // Size the example area to ~75% of the screen so videos are watchable.
        webView.layoutParams = webView.layoutParams.apply {
            height = (context.resources.displayMetrics.heightPixels * 0.75f).toInt()
        }

        webView.settings.javaScriptEnabled = true // MuscleWiki's video player needs JS
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress.isVisible = false
                // Skip the results list: jump straight to the top exercise hit.
                if (MuscleWiki.isSearchUrl(url)) {
                    view?.evaluateJavascript(MuscleWiki.AUTO_OPEN_FIRST_RESULT_JS, null)
                }
            }
        }
        webView.loadUrl(MuscleWiki.searchUrl(exerciseName))

        btnBrowser.setOnClickListener {
            MuscleWiki.openExample(context, exerciseName)
            dialog.dismiss()
        }

        dialog.setContentView(sheetView)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        dialog.setOnDismissListener {
            // Release the WebView so video/audio stops and memory is freed.
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.destroy()
        }
        dialog.show()
    }
}
