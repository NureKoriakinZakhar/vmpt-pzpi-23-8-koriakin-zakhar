package com.vmpt.zakhar.koriakin.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vmpt.zakhar.koriakin.R
import com.vmpt.zakhar.koriakin.databinding.FragmentLeaderboardBinding
import com.vmpt.zakhar.koriakin.presentation.adapter.MatchHistoryAdapter
import com.vmpt.zakhar.koriakin.presentation.viewmodel.LeaderboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeaderboardViewModel by viewModels()

    private val adapter = MatchHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
        observeHistory()
    }

    private fun setupToolbar() {
        val navController = findNavController()
        binding.toolbarLeaderboard.setupWithNavController(navController)
        binding.toolbarLeaderboard.setNavigationContentDescription(R.string.content_desc_navigate_up)
    }

    private fun setupList() {
        binding.recyclerMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMatches.adapter = adapter
        binding.recyclerMatches.itemAnimator = null
    }

    private fun observeHistory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.history.collect { rows ->
                    adapter.submitList(rows)
                    val empty = rows.isEmpty()
                    binding.textEmpty.isVisible = empty
                    binding.recyclerMatches.isVisible = !empty
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
