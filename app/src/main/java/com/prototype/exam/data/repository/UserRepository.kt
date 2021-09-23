package com.prototype.exam.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users
import retrofit2.Response
import java.io.IOException

interface UserRepository {
    suspend fun getUsers(): Response<Users>

    fun getLocalUsers(): LiveData<List<User>>
    fun addUsers(list: List<User>)
    fun addUser(user: User)
    fun getUser(id: Int): User
    suspend fun getUsers2(): MutableList<User>?

    suspend fun <T : Any> safeApiCall(call : suspend()-> Response<T>, error : String) :  T?{
        val result = apiOutput(call, error)
        var output : T? = null
        when(result){
            is Output.Success ->
                output = result.output
            is Output.Error -> Log.e("Error", "The $error and the ${result.exception}")
        }
        return output

    }
    private suspend fun<T : Any> apiOutput(call: suspend()-> Response<T> , error: String) : Output<T>{
        val response = call.invoke()
        return if (response.isSuccessful)
            Output.Success(response.body()!!)
        else
            Output.Error(IOException("OOps .. Something went wrong due to  $error"))
    }
}

sealed class Output<out T : Any>{
    data class Success<out T : Any>(val output : T) : Output<T>()
    data class Error(val exception: Exception)  : Output<Nothing>()
}