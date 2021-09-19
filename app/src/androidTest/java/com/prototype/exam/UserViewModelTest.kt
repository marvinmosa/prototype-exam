package com.prototype.exam

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.model.forecast.ForecastResponse
import com.prototype.exam.data.repository.UserRepositoryImpl
import com.prototype.exam.ui.main.viewModel.UserViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    lateinit var mainRepository: UserRepositoryImpl


    @Before
    fun setUp() {
        mainRepository = mock(UserRepositoryImpl::class.java)
        networkHelper = mock(NetworkHelper::class.java)
        response = mock(Response::class.java) as Response<ForecastResponse>
        apiForecastObserver = mock(Observer::class.java) as Observer<Result<List<ForecastItem>>>
    }

    lateinit var networkHelper: NetworkHelper

    private lateinit var apiForecastObserver: Observer<Result<List<ForecastItem>>>

    private lateinit var response: Response<ForecastResponse>

    @Test
    fun getForecastSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(ForecastResponse(1, emptyList<ForecastItem>())).`when`(response).body()
            doReturn(true).`when`(response).isSuccessful

            doReturn(response)
                .`when`(mainRepository)
                .getForecasts("1701668,1835848,3067696", "metric", "8beca4dc58974504cca73181ee8ca127")
            doReturn(true)
                .`when`(networkHelper)
                .isNetworkConnected()

            val forecastViewModel = UserViewModel(mainRepository, networkHelper)

            forecastViewModel.forecasts.observeForever(apiForecastObserver)
            verify(mainRepository).getForecasts("1701668,1835848,3067696", "metric", "8beca4dc58974504cca73181ee8ca127")
            verify(apiForecastObserver).onChanged(Result.loading(null))
            verify(apiForecastObserver).onChanged(Result.success(emptyList()))
            forecastViewModel.forecasts.removeObserver(apiForecastObserver)
        }
    }

    @Test
    fun getForecastNoInternet() {
        testCoroutineRule.runBlockingTest {
            doReturn(false)
                .`when`(networkHelper)
                .isNetworkConnected()

            val forecastViewModel = UserViewModel(mainRepository, networkHelper)
            forecastViewModel.forecasts.observeForever(apiForecastObserver)
            verify(apiForecastObserver).onChanged(Result.error(null, "No internet connection"))
            forecastViewModel.forecasts.removeObserver(apiForecastObserver)
        }
    }
}