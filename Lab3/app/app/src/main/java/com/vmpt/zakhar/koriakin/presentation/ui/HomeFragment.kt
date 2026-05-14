package com.vmpt.zakhar.koriakin.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vmpt.zakhar.koriakin.R
import com.vmpt.zakhar.koriakin.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wireActions()
    }

    private fun wireActions() {
        binding.btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_game)
        }
        binding.btnLeaderboard.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_leaderboard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
