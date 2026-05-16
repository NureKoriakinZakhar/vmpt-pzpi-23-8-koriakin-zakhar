package com.koriakin.zakhar.lab.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.koriakin.zakhar.lab.R
import com.koriakin.zakhar.lab.databinding.ActivityMainBinding
import com.koriakin.zakhar.lab.domain.model.ConnectionStatus
import com.koriakin.zakhar.lab.domain.model.GameState
import com.koriakin.zakhar.lab.presentation.adapter.LeaderboardAdapter
import com.koriakin.zakhar.lab.presentation.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupListeners()
        observeState()
    }

    private fun setupRecyclerView() {
        leaderboardAdapter = LeaderboardAdapter()
        binding.recyclerLeaderboard.apply {
            adapter = leaderboardAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = null
        }
    }

    private fun setupListeners() {
        binding.buttonConnect.setOnClickListener { onConnectClicked() }
        binding.buttonTap.setOnClickListener { viewModel.tap() }
        binding.buttonDisconnect.setOnClickListener { viewModel.disconnect() }
    }

    private fun onConnectClicked() {
        val address = binding.editServerAddress.text?.toString()?.trim().orEmpty()
        val name = binding.editPlayerName.text?.toString()?.trim().orEmpty()
        if (address.isEmpty()) {
            binding.inputServerAddress.error = getString(R.string.error_empty_address)
            return
        }
        if (name.isEmpty()) {
            binding.inputPlayerName.error = getString(R.string.error_empty_name)
            return
        }
        binding.inputServerAddress.error = null
        binding.inputPlayerName.error = null
        viewModel.connect(address, name)
    }

    private fun observeState() {
        scope.launch {
            viewModel.gameState.collect { state -> updateUi(state) }
        }
    }

    private fun updateUi(state: GameState) {
        when (state.connectionStatus) {
            ConnectionStatus.DISCONNECTED -> showDisconnectedPanel()
            ConnectionStatus.CONNECTING -> showLoadingPanel()
            ConnectionStatus.CONNECTED -> showGamePanel(state)
        }
    }

    private fun showDisconnectedPanel() {
        binding.panelConnect.visibility = View.VISIBLE
        binding.loadingIndicator.visibility = View.GONE
        binding.panelGame.visibility = View.GONE
    }

    private fun showLoadingPanel() {
        binding.panelConnect.visibility = View.GONE
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.panelGame.visibility = View.GONE
        binding.textStatus.text = getString(R.string.status_connecting)
    }

    private fun showGamePanel(state: GameState) {
        binding.panelConnect.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        binding.panelGame.visibility = View.VISIBLE

        binding.textStatus.text = getString(R.string.status_connected)
        binding.textPlayersCount.text = getString(R.string.label_players_count, state.leaderboard.size)
        binding.textMyScore.text = viewModel.getMyScore().toString()
        leaderboardAdapter.submitList(viewModel.buildLeaderboardItems())
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
