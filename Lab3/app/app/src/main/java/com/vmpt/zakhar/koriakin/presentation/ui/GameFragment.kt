package com.vmpt.zakhar.koriakin.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.button.MaterialButton
import com.vmpt.zakhar.koriakin.R
import com.vmpt.zakhar.koriakin.databinding.FragmentGameBinding
import com.vmpt.zakhar.koriakin.presentation.model.CellMark
import com.vmpt.zakhar.koriakin.presentation.model.GameStatus
import com.vmpt.zakhar.koriakin.presentation.model.GameUiState
import com.vmpt.zakhar.koriakin.presentation.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        bindCellClicks()
        observeState()
        binding.btnNewRound.setOnClickListener {
            viewModel.onNewRoundClicked()
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        binding.toolbarGame.setupWithNavController(navController)
        binding.toolbarGame.setNavigationContentDescription(R.string.content_desc_navigate_up)
    }

    private fun bindCellClicks() {
        cellViews().forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.onCellClicked(index)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderBoard(state)
                }
            }
        }
    }

    private fun renderBoard(state: GameUiState) {
        val ctx = requireContext()
        cellViews().forEachIndexed { index, button ->
            applyCellAppearance(button, state.cells[index], ctx)
            val open = state.status == GameStatus.IN_PROGRESS && state.cells[index] == CellMark.EMPTY
            button.isEnabled = true
            button.isClickable = open
        }
        binding.textStatus.text = when (state.status) {
            GameStatus.IN_PROGRESS -> {
                if (state.nextMark == CellMark.X) {
                    getString(R.string.game_turn_x)
                } else {
                    getString(R.string.game_turn_o)
                }
            }
            GameStatus.X_WON -> getString(R.string.game_result_x)
            GameStatus.O_WON -> getString(R.string.game_result_o)
            GameStatus.DRAW -> getString(R.string.game_result_draw)
        }
    }

    private fun applyCellAppearance(button: MaterialButton, mark: CellMark, context: Context) {
        when (mark) {
            CellMark.EMPTY -> {
                button.text = ""
                button.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            }
            CellMark.X -> {
                button.text = context.getString(R.string.match_chip_x)
                button.setTextColor(ContextCompat.getColor(context, R.color.mark_x))
            }
            CellMark.O -> {
                button.text = context.getString(R.string.match_chip_o)
                button.setTextColor(ContextCompat.getColor(context, R.color.mark_o))
            }
        }
    }

    private fun cellViews(): List<MaterialButton> {
        return listOf(
            binding.cell0,
            binding.cell1,
            binding.cell2,
            binding.cell3,
            binding.cell4,
            binding.cell5,
            binding.cell6,
            binding.cell7,
            binding.cell8
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
