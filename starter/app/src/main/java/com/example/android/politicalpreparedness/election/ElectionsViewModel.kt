package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(
    app: Application,
    private val electionRepository: ElectionRepository
) : BaseViewModel(app) {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: MutableLiveData<List<Election>>
        get() = _upcomingElections

    val savedElections = electionRepository.getAllElections()

    init {
        getUpcomingElections()
    }

    private fun getUpcomingElections() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                val result = electionRepository.getElections()
                showLoading.value = false
                when (result) {
                    is Result.Success<ElectionResponse> -> {
                        _upcomingElections.value = result.data.elections
                    }

                    is Result.Error -> {
                        showSnackBarInt.value = R.string.can_not_get_election
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
            }
        }
    }

    fun navigateToVoterInfoScreen(election: Election) {
        navigationCommand.value = NavigationCommand.To(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

    companion object {
        private val TAG = ElectionsViewModel::class.java.simpleName
    }
}
