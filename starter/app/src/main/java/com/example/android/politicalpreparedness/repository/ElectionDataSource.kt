package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface ElectionDataSource {
    // Remote
    suspend fun getElections(): Result<ElectionResponse>

    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse>

    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>

    // Local
    fun getAllElections(): LiveData<List<Election>>

    suspend fun saveElection(election: Election)

    suspend fun getElection(id: Int): Election?

    suspend fun deleteElection(id: Int)

    suspend fun clearElectionTable()
}

