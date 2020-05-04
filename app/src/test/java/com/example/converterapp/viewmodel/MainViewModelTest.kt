package com.example.converterapp.viewmodel

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.ResultBase.ErrorType.DATABASE_ERROR
import com.example.converterapp.repository.ResultBase.ErrorType.NETWORK_ERROR
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
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

    private lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {

        testScheduler = TestScheduler()
    }

    @Test
    fun setUserInput_userInputSubjectUpdated() {
        //Arrange
        //Act
        viewModel.updateUserInput(Pair("EUR", "10"))
        //Assert
        assert(viewModel.getUserInput().blockingFirst().first == "EUR")
        assert(viewModel.getUserInput().blockingFirst().second == "10")
    }

    @Test
    fun fetchRatesNetworkAvailable_success_dataSubjectUpdated() {
        //Arrange
        responseSuccess()
        //Act
        viewModel.fetchCurrencyRates(true)
        //Assert
        val result = viewModel.getCurrencyData().blockingFirst()
        assert(result.size == 2)
        assert(result["EUR"] != null)
        assert(result["GBP"] != null)
    }

    @Test
    fun fetchRatesNetworkAvailable_networkError_errorSubjectTrue() {
        //Arrange
        responseNetworkAvailableNetworkError()
        //Act
        viewModel.fetchCurrencyRates(true)
        //Assert
        assert(viewModel.getErrorNotification().blockingFirst() == true)
    }

    @Test
    fun fetchRatesNetworkAvailable_databaseError_errorSubjectFalse() {
        //Arrange
        responseNetworkAvailableDatabaseError()
        //Act
        viewModel.fetchCurrencyRates(true)
        //Assert
        assert(viewModel.getErrorNotification().blockingFirst() == false)
    }

    @Test
    fun fetchRatesNetworkNOTAvailable_databaseError_errorSubjectTrue() {
        //Arrange
        responseNetworkAvailableDatabaseError()
        //Act
        viewModel.fetchCurrencyRates(false)
        //Assert
        assert(viewModel.getErrorNotification().blockingFirst() == true)
    }

    @Test
    fun fetchRatesNetworkNOTAvailable_databaseDataPresent_dataSubjectUpdated() {
        //Arrange
        responseSuccess()
        //Act
        viewModel.fetchCurrencyRates(false)
        //Assert
        val result = viewModel.getCurrencyData().blockingFirst()
        assert(result.size == 2)
        assert(result["EUR"] != null)
        assert(result["GBP"] != null)
    }

    @Test
    fun getCurrencyData_userInputEmpty_zeroValuesCalculated() {
        //Arrange
        responseSuccess()
        viewModel.updateUserInput(Pair("EUR", ""))
        //act
        viewModel.fetchCurrencyRates(true)
        val result = viewModel.getCurrencyData().blockingFirst()
        //assert
        assert(result.size == 2)
        assert(result["EUR"]?.relativeRate == 0.0)
        assert(result["GBP"]?.relativeRate == 0.0)
    }

    @Test
    fun getCurrencyData_userInputNOTEmpty_correctValuesCalculated() {
        //Arrange
        responseSuccess()
        viewModel.updateUserInput(Pair("EUR", "2"))
        //act
        viewModel.fetchCurrencyRates(true)
        val result = viewModel.getCurrencyData().blockingFirst()
        //assert
        assert(result.size == 2)
        assert(result["EUR"]?.relativeRate == 2.0)
        assert(result["GBP"]?.relativeRate == 3.0)
    }

    //region Helpers
    private fun responseSuccess() {
        Mockito.`when`(repository.fetchConversionRates(anyString(), anyBoolean())).thenReturn(
            Observable.just(
                ResultBase.Success(
                    ConversionRatesResult(
                        conversionRates = mapOf(
                            "EUR" to Currency("EUR", "Euro", 1.0, 1),
                            "GBP" to Currency("GBP", "Pound", 1.5, 2)
                        )
                    )
                )
            )
        )
    }

    private fun responseNetworkAvailableNetworkError() {
        Mockito.`when`(repository.fetchConversionRates(anyString(), anyBoolean())).thenReturn(
            Observable.just(ResultBase.Error(NETWORK_ERROR))
        )
    }

    private fun responseNetworkAvailableDatabaseError() {
        Mockito.`when`(repository.fetchConversionRates(anyString(), anyBoolean())).thenReturn(
            Observable.just(ResultBase.Error(DATABASE_ERROR))
        )
    }
    //endregion
}