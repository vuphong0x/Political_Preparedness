package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : BaseFragment() {

    override val viewModel: VoterInfoViewModel by viewModel()
    private val args: VoterInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val address = "${args.argDivision.state}, ${args.argDivision.country}"
        val electionId = args.argElectionId
        viewModel.fetchVoterInfo(address, electionId)
        viewModel.checkIsFollowing(electionId)

        viewModel.voterInfo.observe(viewLifecycleOwner, Observer { voterInfo ->
            voterInfo.state?.get(0)?.electionAdministrationBody?.let { administrationBody ->
                binding.stateLocations.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(administrationBody.votingLocationFinderUrl)
                    startActivity(intent)
                }

                binding.stateBallot.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(administrationBody.ballotInfoUrl)
                    startActivity(intent)
                }
            }

        })
        return binding.root
    }
}
