package com.example.converterapp.repository.interactor

import com.example.converterapp.webservice.EndpointDefinition
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesInteractor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConversionRatesInteractorTest {

    @Mock
    lateinit var endpointDefinition: EndpointDefinition

    @InjectMocks
    lateinit var interactor: ConversionRatesInteractor

    @Test
    fun fetchRates_correctEndpointCalled() {
        //Arrange
        //Act
        interactor.fetchConversionRates("EUR")
        //Assert
        argumentCaptor<String>().apply {
            verify(endpointDefinition).fetchConversionRates(capture())
            assert(this.lastValue == "EUR")
        }
    }
}