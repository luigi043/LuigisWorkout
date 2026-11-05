package com.luigisworkout

import android.content.Intent
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.luigisworkout.ui.logmeal.LogMealActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealLoggingTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun drinkRecipeKeepsItsTypeAndRejectsZeroQuantity() {
        val intent = Intent(context, LogMealActivity::class.java).apply {
            putExtra("food_name", "Sparkling water")
            putExtra("meal_type", "drink")
            putExtra("calories", 0)
        }
        ActivityScenario.launch<LogMealActivity>(intent).use { scenario ->
            onView(withId(R.id.spinnerMealType)).check(matches(withSpinnerText(containsString("Drinks"))))
            onView(withId(R.id.etFoodName)).check(matches(withText("Sparkling water")))
            onView(withId(R.id.etQuantity)).perform(scrollTo(), replaceText("0"), closeSoftKeyboard())
            onView(withId(R.id.btnSaveMeal)).perform(scrollTo(), click())
            scenario.onActivity { activity ->
                assertEquals(
                    context.getString(R.string.quantity_invalid),
                    activity.findViewById<EditText>(R.id.etQuantity).error?.toString()
                )
            }
        }
    }
}
