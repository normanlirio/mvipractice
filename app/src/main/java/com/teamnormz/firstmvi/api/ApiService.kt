package com.teamnormz.firstmvi.api

import androidx.lifecycle.LiveData
import com.teamnormz.firstmvi.model.BlogPost
import com.teamnormz.firstmvi.model.User
import com.teamnormz.firstmvi.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ) : LiveData<GenericApiResponse<User>>

    @GET("placeholder/blogs")
    fun getBlogPosts() : LiveData<GenericApiResponse<List<BlogPost>>>
}