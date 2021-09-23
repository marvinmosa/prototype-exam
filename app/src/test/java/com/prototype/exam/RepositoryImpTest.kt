package com.prototype.exam

import android.content.Context
import androidx.room.Room
import com.prototype.exam.data.api.ApiHelperImpl
import com.prototype.exam.data.api.ResultWrapper
import com.prototype.exam.data.db.AppDatabase
import com.prototype.exam.data.db.UserDao
import com.prototype.exam.data.model.Users
import com.prototype.exam.data.repository.UserRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class RepositoryImpTest {
    private lateinit var service: ApiHelperImpl
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase
    private lateinit var response: Response<Users>
    private lateinit var context: Context

    @get:Rule
    var coroutinesTestRule = TestCoroutineRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        context = mock(Context::class.java)
        service = mock(ApiHelperImpl::class.java)
        db = mock(AppDatabase::class.java)
        response = mock(Response::class.java) as Response<Users>
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()

        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }


    @Test
    fun `when getUsers is call and there is no ERROR the returns Success`() {
        testScope.launch {
            val repository = getRepositoryImpl()
            val mockList = Users()
            val result = ResultWrapper.Success(mockList)

            `when`(service.getUsers()).thenReturn(response)
            assertEquals(repository.getUsers(), result)
        }
    }

    fun getRepositoryImpl(): UserRepositoryImpl {
        return UserRepositoryImpl(
            service, userDao
        )
    }
}