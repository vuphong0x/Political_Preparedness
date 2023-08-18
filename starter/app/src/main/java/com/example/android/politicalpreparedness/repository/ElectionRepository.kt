package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
    private val service: CivicsApiService,
    private val electionDao: ElectionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    // [START] Remote Data
    override suspend fun getElections(): Result<ElectionResponse> {
        return withContext(dispatcher) {
            try {
                Result.Success(service.getElections())
            } catch (ex: Exception) {
                Result.Error(ex.message)
            }
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> {
        return withContext(dispatcher) {
            try {
                Result.Success(service.getVoterInfo(address, electionId))
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> {
        return withContext(dispatcher) {
            try {
                Result.Success(service.getRepresentatives(address))
            } catch (ex: Exception) {
                Result.Error(ex.message)
            }
        }
    }
    // [END] Remote Data


    // [START] Local Data
    override suspend fun getElection(id: Int): Election? {
        return withContext(dispatcher) {
            electionDao.getElection(id)
        }
    }

    override fun getAllElections(): LiveData<List<Election>> {
        return electionDao.getAllElections()
    }

    override suspend fun saveElection(election: Election) {
        withContext(dispatcher) {
            electionDao.insertElection(election)
        }
    }

    override suspend fun clearElectionTable() {
        withContext(dispatcher) {
            electionDao.clearElectionTable()
        }
    }

    override suspend fun deleteElection(id: Int) {
        withContext(dispatcher) {
            electionDao.deleteElection(id)
        }
    }
    // [END] Local Data
}
