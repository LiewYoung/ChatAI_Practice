package top.liewyoung.aiwechat.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.ui.theme.AIWeChatTheme
import top.liewyoung.aiwechat.viewmodel.SettingsUiState
import top.liewyoung.aiwechat.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as AIWeChatApplication).container.provideSettingsViewModelFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsScreenContent(
        onBackClicked = onBackClicked,
        uiState = uiState,
        onSaveSettings = viewModel::saveSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    onBackClicked: () -> Unit,
    uiState: SettingsUiState,
    onSaveSettings: (String, String, String) -> Unit
) {
    var apiKey by remember { mutableStateOf(uiState.apiKey) }
    var baseUrl by remember { mutableStateOf(uiState.baseUrl) }
    var model by remember { mutableStateOf(uiState.model) }

    LaunchedEffect(uiState) {
        apiKey = uiState.apiKey
        baseUrl = uiState.baseUrl
        model = uiState.model
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = baseUrl,
                onValueChange = { baseUrl = it },
                label = { Text("Base URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Model") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Button(
                onClick = { onSaveSettings(apiKey, baseUrl, model) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    AIWeChatTheme {
        SettingsScreenContent(
            onBackClicked = {},
            uiState = SettingsUiState(apiKey = "preview_key", baseUrl = "preview_url", model = "gpt-4"),
            onSaveSettings = { _, _, _ -> }
        )
    }
}
