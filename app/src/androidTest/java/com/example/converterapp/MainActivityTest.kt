package com.example.converterapp

import androidx.test.rule.ActivityTestRule
import com.example.converterapp.ui.main.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {

    @get:Rule var activityRule =
        ActivityTestRule(MainActivity::class.java, false, false)
}