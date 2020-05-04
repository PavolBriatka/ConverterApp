package com.example.converterapp.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.converterapp.R
import com.example.converterapp.TestBaseApplication
import com.example.converterapp.mocks.FakeConnectivityObservable
import com.example.converterapp.mocks.FakeConversionRatesRepo
import com.example.converterapp.mocks.FakeConversionRatesRepo.Scenario
import com.example.converterapp.ui.main.MainActivity
import com.example.converterapp.ui.main.adapter.ConverterAdapter.CurrencyViewHolder
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.actionOnChild
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private lateinit var fakeConversionRatesRepo: FakeConversionRatesRepo
    private lateinit var fakeConnectivityObservable: FakeConnectivityObservable


    @Rule
    @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = object :
        ActivityTestRule<MainActivity>(MainActivity::class.java, true, false) {}

    @Before
    fun setup() {

        val app =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestBaseApplication
        fakeConversionRatesRepo = app.fakeConversionRatesRepo
        fakeConnectivityObservable = app.fakeConnectivityObservable
    }

    @Test
    fun openActivity_databaseEmpty_noNetwork_noDataScreenShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.EMPTY_DB_NO_INTERNET
        fakeConnectivityObservable.isAvailable = false
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.ll_no_data_screen)).check(matches(isDisplayed()))
        onView(withText(R.string.no_data_screen_message)).check(matches(isDisplayed()))
        onView(withText(R.string.response_error_message)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun openActivity_databaseEmpty_networkAvailable_networkError_noDataScreenShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.EMPTY_DB_INTERNET_NETWORK_ERROR
        fakeConnectivityObservable.isAvailable = true
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.ll_no_data_screen)).check(matches(isDisplayed()))
        onView(withText(R.string.no_data_screen_message)).check(matches(isDisplayed()))
        onView(withText(R.string.response_error_message)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun openActivity_databaseEmpty_networkAvailable_success_dataShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.EMPTY_DB_INTERNET_SUCCESS
        fakeConnectivityObservable.isAvailable = true
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.tv_currency_code,
                    0,
                    withText("EUR")
                )
            )
        )

        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    4,
                    withText("5")
                )
            )
        )
    }

    @Test
    fun openActivity_databaseNOTEmpty_noNetwork_databaseDataShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.DB_SUCCESS_NO_INTERNET
        fakeConnectivityObservable.isAvailable = false
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    4,
                    withText("6")
                )
            )
        )
        onView(withText(R.string.response_error_message)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun openActivity_databaseNOTEmpty_networkAvailable_networkError_databaseDataShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.DB_SUCCESS_INTERNET_NETWORK_ERROR
        fakeConnectivityObservable.isAvailable = true
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    4,
                    withText("6")
                )
            )
        )
        onView(withText(R.string.response_error_message)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun openActivity_databaseNOTEmpty_networkAvailable_success_networkDataShowed() {
        //Arrange
        fakeConversionRatesRepo.scenario = Scenario.DB_SUCCESS_INTERNET_SUCCESS
        fakeConnectivityObservable.isAvailable = true
        activityTestRule.launchActivity(null)
        //Act
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.tv_currency_code,
                    0,
                    withText("EUR")
                )
            )
        )

        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    4,
                    withText("5")
                )
            )
        )
    }

    @Test
    fun userInteraction_clickOnItem_itemMovedToTop() {
        //Arrange
        activityTestRule.launchActivity(null)
        //Act
        onView(withId(R.id.rv_currency_list)).perform(
            actionOnItemAtPosition<CurrencyViewHolder>(
                4,
                click()
            )
        )
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.tv_currency_code,
                    0,
                    withText("USD")
                )
            )
        )
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    0,
                    withText("5")
                )
            )
        )
    }

    @Test
    fun userInteraction_changeBaseCurrencyValue_otherValuesRecalculated() {
        //Arrange
        activityTestRule.launchActivity(null)
        //Act
        onView(withId(R.id.rv_currency_list)).perform(
            actionOnItemAtPosition<CurrencyViewHolder>(
                0,
                actionOnChild(
                    typeText("0"),
                    R.id.et_currency_value
                )
            )
        )
        Espresso.closeSoftKeyboard()
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    1,
                    withText("20")
                )
            )
        )
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    2,
                    withText("30")
                )
            )
        )
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    3,
                    withText("40")
                )
            )
        )
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    4,
                    withText("50")
                )
            )
        )
    }

    @Test
    fun userInteraction_startWithDecimalPointInEmptyField_zeroAddedInFrontOfDecimalPoint() {
        //Arrange
        activityTestRule.launchActivity(null)
        //Act
        onView(withId(R.id.rv_currency_list)).perform(
            actionOnItemAtPosition<CurrencyViewHolder>(
                0,
                actionOnChild(
                    replaceText("."),
                    R.id.et_currency_value
                )
            )
        )
        Espresso.closeSoftKeyboard()
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    0,
                    withText("0.")
                )
            )
        )
    }

    @Test
    fun userInteraction_startInputWithZeroFollowedByNumber_zeroInFrontRemoved() {
        //Arrange
        activityTestRule.launchActivity(null)
        //Act
        onView(withId(R.id.rv_currency_list)).perform(
            actionOnItemAtPosition<CurrencyViewHolder>(
                0,
                actionOnChild(
                    replaceText("05"),
                    R.id.et_currency_value
                )
            )
        )
        Espresso.closeSoftKeyboard()
        //Assert
        onView(withId(R.id.rv_currency_list)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.et_currency_value,
                    0,
                    withText("5")
                )
            )
        )
    }


}