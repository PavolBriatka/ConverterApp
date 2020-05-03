package com.example.converterapp

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.converterapp.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {


    @Rule
    @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = object :
        ActivityTestRule<MainActivity>(MainActivity::class.java, true, false) {}

    @Test
    fun doNothing() {

        activityTestRule.launchActivity(null)

        assert( true )
    }
}