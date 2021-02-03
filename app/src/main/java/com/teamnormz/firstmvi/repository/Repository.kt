package com.teamnormz.firstmvi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.teamnormz.firstmvi.api.RetrofitBuilder
import com.teamnormz.firstmvi.model.BlogPost
import com.teamnormz.firstmvi.model.User
import com.teamnormz.firstmvi.ui.main.state.MainViewState
import com.teamnormz.firstmvi.util.*

object Repository {

    fun getBlogPosts() : LiveData<DataState<MainViewState>> {
       return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
           override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
              result.value = DataState.data(
                  data = MainViewState(
                      blogPosts = response.body
                  )
              )
           }

           override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
              return RetrofitBuilder.apiService.getBlogPosts()
           }

       }.asLiveData()
    }


    fun getUser(userId: String) : LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }
}

