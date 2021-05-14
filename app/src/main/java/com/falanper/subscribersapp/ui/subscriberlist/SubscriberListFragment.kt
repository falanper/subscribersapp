package com.falanper.subscribersapp.ui.subscriberlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.falanper.subscribersapp.R
import com.falanper.subscribersapp.data.db.AppDatabase
import com.falanper.subscribersapp.data.db.dao.SubscriberDAO
import com.falanper.subscribersapp.extension.navigateWithAnimations
import com.falanper.subscribersapp.repository.DatabaseDataSource
import com.falanper.subscribersapp.repository.SubscriberRepository
import kotlinx.android.synthetic.main.subscriber_list_fragment.*

class SubscriberListFragment : Fragment(R.layout.subscriber_list_fragment) {

    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDatabase.getInstance(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) { allSubscribers ->
            val subscriberListAdapter = SubscriberListAdapter(allSubscribers).apply {
                onItemClick = { subscriber ->

                    val directions =
                        SubscriberListFragmentDirections.actionSubscriberListFragmentToSubscriberFragment(
                            subscriber
                        )
                    findNavController().navigateWithAnimations(directions)

                }
            }
            recycler_subscribers.run {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
                // layoutManager = LinearLayoutManager(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    private fun configureViewListeners() {
        fabAddSubscriber.setOnClickListener {
            findNavController().navigateWithAnimations(
                R.id.action_subscriberListFragment_to_subscriberFragment
            )
        }
    }
}