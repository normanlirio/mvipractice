package com.teamnormz.firstmvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.teamnormz.firstmvi.R
import com.teamnormz.firstmvi.ui.DataStateListener
import com.teamnormz.firstmvi.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataStateListener {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showMainFragment()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                MainFragment(), "MainFragment")
            .commit()
    }

    override fun onDataStateChanged(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            showProgressBar(it.loading)
            it.message?.let {event ->
                event.getContentIfNotHandled()?.let {message ->
                    showToast(message)
                }
            }
        }
    }

    fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar(isVisible: Boolean) {
        if(isVisible) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}