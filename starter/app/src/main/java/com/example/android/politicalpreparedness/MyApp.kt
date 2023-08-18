package com.example.android.politicalpreparedness

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val myModule = module {
            viewModel {
                ElectionsViewModel(this@MyApp, get() as ElectionRepository)
            }
            viewModel {
                VoterInfoViewModel(this@MyApp, get(), get())
            }
            viewModel {
                RepresentativeViewModel(this@MyApp, get(), get())
            }
            single { ElectionRepository(get(), get()) }
            single { ElectionDatabase.getInstance(get()).electionDao }
            single { CivicsApi.retrofitService }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }

    }
}
