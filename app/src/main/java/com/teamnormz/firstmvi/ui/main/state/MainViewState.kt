package com.teamnormz.firstmvi.ui.main.state

import com.teamnormz.firstmvi.model.BlogPost
import com.teamnormz.firstmvi.model.User

data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
) {
}