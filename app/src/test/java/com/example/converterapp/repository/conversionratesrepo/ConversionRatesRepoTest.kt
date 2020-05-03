package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.ResultBase.ErrorType.*
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.utils.currencyhelper.ICurrencyHelper
import com.example.converterapp.utils.databaseutil.IDatabaseUtil
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ConversionRatesRepoTest {

    @Mock
    lateinit var interactor: IConversionRatesInteractor

    @Mock
    lateinit var databaseUtil: IDatabaseUtil

    @Mock
    lateinit var currencyHelper: ICurrencyHelper

    @InjectMocks
    lateinit var repository: ConversionRatesRepo

    @Test
    fun fetchRates_correctArgumentPassedToInteractor() {
        //Arrange
        resultSuccess()
        //Act
        repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        argumentCaptor<String>().apply {
            verify(interactor).fetchConversionRates(capture())
            assert(this.lastValue == "EUR")
        }
    }

    @Test
    fun fetchRatesNetworkAvailable_responseSuccess_listOfRatesReturned() {
        //Arrange
        currencyHelperResponse()
        resultSuccess()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Success)
        result = result as ResultBase.Success
        assert(result.result.conversionRates.size == 4)
    }

    @Test
    fun fetchRatesNetworkAvailable_responseSuccessDataNull_networkErrorReceived() {
        //Arrange
        resultSuccessDataNull()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == NETWORK_ERROR)
    }

    @Test
    fun fetchRatesNetworkAvailable_responseSuccessBaseCurrencyNullOrEmpty_networkErrorReceived() {
        //Arrange
        resultSuccessBaseCurrencyNullOrEmpty()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == NETWORK_ERROR)
    }

    @Test
    fun fetchRatesNetworkAvailable_responseSuccessRatesNull_networkErrorReceived() {
        //Arrange
        resultSuccessRatesNull()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == NETWORK_ERROR)
    }

    @Test
    fun fetchRatesNetworkAvailable_responseNetworkError_networkErrorReceived() {
        //Arrange
        resultResponseError()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == NETWORK_ERROR)
    }

    @Test
    fun fetchRatesNetworkAvailable_success_dataSavedToDatabase() {
        //Arrange
        currencyHelperResponse()
        resultSuccess()
        //Act
        repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        argumentCaptor<ConversionRatesResult>().apply {
            verify(databaseUtil).convertAndSave(capture())
            assert(this.lastValue.conversionRates.size == 4)
        }
    }

    @Test
    fun fetchRatesNetworkAvailable_databaseQueriedFirst() {
        //Arrange
        currencyHelperResponse()
        resultSuccess()
        //Act
        repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        val order = inOrder(databaseUtil, interactor)
        order.verify(databaseUtil).retrieveAndConvert()
        order.verify(interactor).fetchConversionRates(anyString())
    }

    @Test
    fun fetchRatesNetworkAvailable_dataPresentInDatabase_successResultReceived() {
        //Arrange
        resultDbDataSuccess()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Success)
        result = result as ResultBase.Success
        assert(result.result.conversionRates.size == 1)
        assert(result.result.conversionRates["CZK"] != null)
    }

    @Test
    fun fetchRatesNetworkAvailable_dataNOTPresentInDatabase_databaseErrorResultReceived() {
        //Arrange
        resultDbDataEmpty()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = true).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == DATABASE_ERROR)
    }

    @Test
    fun fetchRatesNetworkNOTAvailable_networkRequestNotFired() {
        //Arrange
        resultDbDataSuccess()
        //Act
        repository.fetchConversionRates(isNetworkAvailable = false).blockingFirst()
        //Assert
        verifyZeroInteractions(interactor)
    }

    @Test
    fun fetchRatesNetworkNOTAvailable_dataPresentInDatabase_successResultReceived() {
        //Arrange
        resultDbDataSuccess()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = false).blockingFirst()
        //Assert
        assert(result is ResultBase.Success)
        result = result as ResultBase.Success
        assert(result.result.conversionRates.size == 1)
        assert(result.result.conversionRates["CZK"] != null)
    }

    @Test
    fun fetchRatesNetworkNOTAvailable_dataNOTPresentInDatabase_databaseErrorResultReceived() {
        //Arrange
        resultDbDataEmpty()
        //Act
        var result = repository.fetchConversionRates(isNetworkAvailable = false).blockingFirst()
        //Assert
        assert(result is ResultBase.Error)
        result = result as ResultBase.Error
        assert(result.errorType == DATABASE_ERROR)
    }

    //region Helpers
    private fun currencyHelperResponse() {
        Mockito.`when`(currencyHelper.fetchResources(anyString())).thenReturn(
            Pair("Euro", 1)
        )
    }

    private fun resultDbDataSuccess() {
        Mockito.`when`(databaseUtil.retrieveAndConvert()).thenReturn(
            ConversionRatesResult(
                conversionRates = mapOf(
                    "CZK" to Currency(
                        currencyCode = "CZK",
                        currencyName = "Koruna",
                        relativeRate = 25.45,
                        flagId = 6
                    )
                )
            )
        )
    }

    private fun resultDbDataEmpty() {
        Mockito.`when`(databaseUtil.retrieveAndConvert()).thenReturn(
            ConversionRatesResult(
                conversionRates = mapOf()
            )
        )
    }

    private fun resultSuccess() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Single.just(
                Response.success(
                    ConversionRatesResponseModel(
                        baseCurrency = "EUR",
                        rates = mutableMapOf("GBP" to 1.5, "CZK" to 25.45, "DKK" to 15.75)
                    )
                )
            )
        )
    }

    private fun resultSuccessDataNull() {
        val response: ConversionRatesResponseModel? = null
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Single.just(
                Response.success(response)
            )
        )
    }

    private fun resultSuccessBaseCurrencyNullOrEmpty() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Single.just(
                Response.success(
                    ConversionRatesResponseModel(
                        baseCurrency = null,
                        rates = mutableMapOf("GBP" to 1.5, "CZK" to 25.45, "DKK" to 15.75)
                    )
                )
            )
        )
    }

    private fun resultSuccessRatesNull() {
        Mockito.`when`(interactor.fetchConversionRates(anyString())).thenReturn(
            Single.just(
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
            Single.just(
                Response.error(
                    400, ResponseBody.create(
                        MediaType.parse("text"),
                        "Invalid base currency"
                    )
                )
            )
        )
    }
    //endregion
}