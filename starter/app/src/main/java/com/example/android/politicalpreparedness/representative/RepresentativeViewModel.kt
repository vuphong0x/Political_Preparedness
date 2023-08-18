package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val app: Application,
    private val repository: ElectionRepository
) : BaseViewModel(app) {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    fun getRepresentatives() {
        showLoading.value = true
        viewModelScope.launch {
            val result = repository.getRepresentatives(getCurrentAddress().toFormattedString())
            showLoading.value = false
            when (result) {
                is Result.Success -> {
                    _representatives.value = result.data.offices.flatMap { office ->
                        office.getRepresentatives(result.data.officials)
                    }
                }

                is Result.Error -> {
                    showSnackBar.value = result.message
                    _representatives.value = emptyList()
                }
            }
        }
    }

    private fun getCurrentAddress() = Address(
        addressLine1.value.toString(),
        addressLine2.value.toString(),
        city.value.toString(),
        state.value.toString(),
        zip.value.toString()
    )

    fun updateAddress(address: Address) {
        addressLine1.value = address.line1
        addressLine2.value = address.line2 ?: ""
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

    fun updateState(newState: String) {
        state.value = newState
    }

}
