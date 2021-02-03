package com.teamnormz.firstmvi.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.teamnormz.firstmvi.R
import com.teamnormz.firstmvi.model.BlogPost
import com.teamnormz.firstmvi.model.User
import com.teamnormz.firstmvi.ui.DataStateListener
import com.teamnormz.firstmvi.ui.main.state.MainStateEvent
import com.teamnormz.firstmvi.ui.main.state.MainStateEvent.*
import com.teamnormz.firstmvi.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException

class MainFragment : Fragment(), BlogPostRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel

    lateinit var dataStateHandler: DataStateListener

    lateinit var blogListAdapter: BlogPostRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }!! ?: throw Exception("Invalid Acitivity")

        subscribeObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogPostRecyclerAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateHandler.onDataStateChanged(dataState)

            dataState.data?.let { mainViewState ->
                mainViewState.getContentIfNotHandled()?.let { event->
                    event.blogPosts?.let { blogPost ->
                        //set blogposts data
                        viewModel.setBlogListData(blogPost)
                    }

                    event.user?.let { user ->
                        //set user data
                        viewModel.setUser(user)
                    }
                }

            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {
                println("DEBUG: Setting blog posts to Recyclerview: $it")
                blogListAdapter.submitList(it)
            }

            viewState.user?.let {
                println("DEBUG: Setting user data: $it")
                setUserProperties(it)
            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username

        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }
    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener

        } catch (cce: ClassCastException) {
            println("DEBUG: $context must implemenet DataStateListener")
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: $position")
        println("DEBUG: $item")
    }
}