package com.prototype.exam

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.prototype.exam.data.api.ResultWrapper
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users
import com.prototype.exam.data.repository.UserRepositoryImpl
import com.prototype.exam.ui.main.viewModel.UserViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import retrofit2.Response


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var networkHelper: NetworkHelper
    private lateinit var repository: UserRepositoryImpl
    private lateinit var apiUserObserver: Observer<Result<List<User>>>
    private lateinit var response: Response<Users>


    @get:Rule
    var coroutinesTestRule = TestCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = mock(UserRepositoryImpl::class.java)
        networkHelper = mock(NetworkHelper::class.java)
        apiUserObserver = mock(Observer::class.java) as Observer<Result<List<User>>>
        response =  mock(Response::class.java) as Response<Users>
    }

    @Test
    fun `when get User from repository is call and return a NETWORK ERROR`() =
        coroutinesTestRule.runBlockingTest {
            val SUT = spy(getUserViewModel())
            val result = ResultWrapper.NetworkError
            val error = MutableLiveData<Boolean>()
            val expected = true
            error.value = expected
            `when`(repository.getUsers()).thenReturn(result)
            SUT.fetchUsers()
            verify(SUT, times(1))
            assertEquals(error.value, expected)
        }

    @Test
    fun `when get Users from repository is call and return a GenericError ERROR`() =
        coroutinesTestRule.runBlockingTest {
            val SUT = spy(getUserViewModel())
            val result = ResultWrapper.GenericError(999)
            val error = MutableLiveData<Boolean>()
            val expected = true
            error.value = expected

            `when`(repository.getUsers()).thenReturn(result)
            SUT.fetchUsers()
            verify(SUT, times(1))
            assertEquals(error.value, expected)
        }

    @Test
    fun `when get Users is call and return is success`() {
        coroutinesTestRule.runBlockingTest {
            val mockList = Users()
            val result = ResultWrapper.Success(mockList)
            `when`(repository.getUsers()).thenReturn(result)

            doReturn(true)
                .`when`(networkHelper)
                .isNetworkConnected()

            val userViewModel = UserViewModel(repository, networkHelper)

            userViewModel.users.observeForever(apiUserObserver)
            verify(repository).getUsers()
            verify(apiUserObserver).onChanged(Result.loading(null))
            verify(apiUserObserver).onChanged(Result.success(emptyList()))
            userViewModel.users.removeObserver(apiUserObserver)
        }
    }

    @Test
    fun `when the user has no connection then return No internet connect`() =
        coroutinesTestRule.runBlockingTest {
            doReturn(false)
                .`when`(networkHelper)
                .isNetworkConnected()

            val userViewModel = getUserViewModel()
            userViewModel.users.observeForever(apiUserObserver)
            verify(apiUserObserver).onChanged(Result.error(null, "No internet connection"))
            userViewModel.users.removeObserver(apiUserObserver)
        }

    private fun getUserViewModel(): UserViewModel {
        return UserViewModel(
            repository, networkHelper
        )
    }
}