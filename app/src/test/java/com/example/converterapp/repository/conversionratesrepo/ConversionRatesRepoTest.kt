package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class ConversionRatesRepoTest {

    @Mock
    lateinit var interactor: IConversionRatesInteractor

    @InjectMocks
    lateinit var repository: ConversionRatesRepo

    @Test
    fun fetchRates_correctArgumentPassedToInteractor() {
        //Arrange
        resultSuccess()
        //Act
        repository.fetchConversionRates()
        //Assert
        argumentCaptor<String>().apply {
            verify(interactor).fetchConversionRates(capture())
            assert(this.lastValue == "EUR")
        }
    }

    @Test
    fun fetchRates_responseSuccess_listOfRatesReturned() {
        //Arrange
        resultSuccess()
        //Act
        var result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Success)
        result = result as ResultBase.Success
        assert(result.result.conversionRates.size == 4)
    }

    @Test
    fun fetchRates_responseSuccess_baseCurrencyOnTopOfTheList() {
        //Arrange
        resultSuccess()
        //Act
        var result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Success)
        result = result as ResultBase.Success
        assert(result.result.conversionRates[0].currencyCode == "EUR")
        assert(result.result.conversionRates[0].relativeRate == 1.0)
    }

    @Test
    fun fetchRates_responseSuccessDataNull_errorReceived() {
        //Arrange
        resultSuccessDataNull()
        //Act
        val result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
    }

    @Test
    fun fetchRates_responseSuccessBaseCurrencyNullOrEmpty_errorReceived() {
        //Arrange
        resultSuccessBaseCurrencyNullOrEmpty()
        //Act
        val result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
    }

    @Test
    fun fetchRates_responseSuccessRatesNull_errorReceived() {
        //Arrange
        resultSuccessRatesNull()
        //Act
        val result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
    }

    @Test
    fun fetchRates_responseNetworkError_errorReceived() {
        //Arrange
        resultResponseError()
        //Act
        val result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
    }

    @Test(expected = UnknownHostException::class)
    fun fetchRates_errorThrown_errorReceived() {
        //Arrange
        resultErrorThrown()
        //Act
        val result = repository.fetchConversionRates().blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
    }

    //region Helpers
    private fun resultSuccess() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Observable.just(
                Response.success(
                    ConversionRatesResponseModel(
                        baseCurrency = "EUR",
                        rates = mapOf("GBP" to 1.5, "CZK" to 25.45, "DKK" to 15.75)
                    )
                )
            )
        )
    }

    private fun resultSuccessDataNull() {
        val response: ConversionRatesResponseModel? = null
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Observable.just(
                Response.success(response)
            )
        )
    }

    private fun resultSuccessBaseCurrencyNullOrEmpty() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Observable.just(
                Response.success(
                    ConversionRatesResponseModel(
                        baseCurrency = null,
                        rates = mapOf("GBP" to 1.5, "CZK" to 25.45, "DKK" to 15.75)
                    )
                )
            )
        )
    }

    private fun resultSuccessRatesNull() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Observable.just(
                Response.success(
                    ConversionRatesResponseModel(
                        baseCurrency = "EUR",
                        rates = null
                    )
                )
            )
        )
    }

    private fun resultResponseError() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Observable.just(
                Response.error(
                    400, ResponseBody.create(
                        MediaType.parse("text"),
                        "Invalid base currency"
                    )
                )
            )
        )
    }

    private fun resultErrorThrown() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).doAnswer {
            throw UnknownHostException()
        }
    }
    //endregion
}