package com.example.converterapp

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.converterapp.repository.FakeConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private lateinit var fakeRepo: FakeConversionRatesRepo


    @Rule
    @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = object :
        ActivityTestRule<MainActivity>(MainActivity::class.java, true, false) {}

    @Before
    fun setup() {

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestBaseApplication
        fakeRepo = app.fakeRepo
    }

    @Test
    fun doNothing() {

        fakeRepo.isSuccess = false

        activityTestRule.launchActivity(null)

        assert( true )
    }
}