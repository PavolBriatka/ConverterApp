package com.example.converterapp.ui.viewmodel

import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import org.junit.Before

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Mock
    lateinit var repository: IConversionRatesRepo

    @InjectMocks
    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
    }


}