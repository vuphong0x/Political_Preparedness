package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : BaseFragment() {

    override val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val electionAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoScreen(election)
        })
        val savedListAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.navigateToVoterInfoScreen(election)
        })

        binding.rvUpcomingElections.adapter = electionAdapter
        binding.rvSavedElections.adapter = savedListAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            electionAdapter.submitList(it)
        })
        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedListAdapter.submitList(it)
        })

        return binding.root
    }
}
