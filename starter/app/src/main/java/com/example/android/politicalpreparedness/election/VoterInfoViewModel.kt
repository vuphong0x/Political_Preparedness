package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val app: Application,
    private val dataSource: ElectionDao,
    private val repository: ElectionRepository
) : BaseViewModel(app) {

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    private val _isFollow = MutableLiveData<Boolean>()
    val isFollow: LiveData<Boolean>
        get() = _isFollow

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private var electionId: Int? = null

    fun fetchVoterInfo(address: String, electionId: Int) {
        this.electionId = electionId
        viewModelScope.launch {
            val result = repository.getVoterInfo(address, electionId)
            when (result) {
                is Result.Success -> {
                    _voterInfo.value = result.data
                }

                is Result.Error -> {
                    showToast.value = result.message
                }
            }
        }
    }

    fun toggleFavoriteElection() {
        viewModelScope.launch {
            if (isFollow.value == true) {
                deleteVoterInfo()
            } else {
                insertVoterInfo()
            }
        }
    }

    private fun insertVoterInfo() {
        viewModelScope.launch {
            _isFollow.value = true
            voterInfo.value?.let { dataSource.insertElection(it.election) }
        }
    }

    private fun deleteVoterInfo() {
        _isFollow.value = false
        viewModelScope.launch {
            voterInfo.value?.election?.id?.let { dataSource.deleteElection(it) }
        }
    }

    fun checkIsFollowing(electionId: Int) {
        viewModelScope.launch {
            _isFollow.value = dataSource.getElection(electionId) != null
        }
    }

}
