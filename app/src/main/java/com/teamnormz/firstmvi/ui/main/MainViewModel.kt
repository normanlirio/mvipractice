package com.teamnormz.firstmvi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamnormz.firstmvi.model.BlogPost
import com.teamnormz.firstmvi.model.User
import com.teamnormz.firstmvi.repository.Repository
import com.teamnormz.firstmvi.ui.main.state.MainStateEvent
import com.teamnormz.firstmvi.ui.main.state.MainStateEvent.*
import com.teamnormz.firstmvi.ui.main.state.MainViewState
import com.teamnormz.firstmvi.util.AbsentLiveData
import com.teamnormz.firstmvi.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState : MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
    get() = _viewState

    val dataState : LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        return when(stateEvent) {
           is GetBlogPostsEvent -> {
              Repository.getBlogPosts()
           }
           is GetUserEvent -> {
              Repository.getUser(stateEvent.userId)
           }
           is None -> {
               AbsentLiveData.create()
           }
       }
    }


    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update  = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User) {
        val update  = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }


    fun getCurrentViewStateOrNew() : MainViewState {
        val value  = viewState.value?.let {
            it
        }?: MainViewState()

        return  value

    }

    fun setStateEvent(event : MainStateEvent) {
        _stateEvent.value = event
    }

}