package com.prototype.exam

import com.prototype.exam.data.api.ErrorManagerHelperImpl
import com.prototype.exam.data.api.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class ErrorManagerHelperImplTest {
    @get:Rule
    val coroutinesTestRule = TestCoroutineRule()

    @Test
    fun `when results returns successfully then it should emit the result as success`() {
        coroutinesTestRule.runBlockingTest {
            val test = getNetwork()
            val lambdaResult = true
            val result = test.dataCall { lambdaResult }
            assertEquals(ResultWrapper.Success(lambdaResult), result)
        }
    }

    @Test
    fun `when results throws IOException then it should emit the result as NetworkError`() {
        coroutinesTestRule.runBlockingTest {
            val test = getNetwork()
            val result = test.dataCall() { throw IOException() }
            assertEquals(ResultWrapper.NetworkError, result)
        }
    }

    @Test
    fun `when results throws unknown exception then it should emit GenericError`() {
        coroutinesTestRule.runBlockingTest {
            val test = getNetwork()
            val result = test.dataCall() {
                throw IllegalStateException()
            }
            assertEquals(ResultWrapper.GenericError(), result)
        }
    }

    private fun getNetwork() : ErrorManagerHelperImpl {
        return ErrorManagerHelperImpl()
    }
}