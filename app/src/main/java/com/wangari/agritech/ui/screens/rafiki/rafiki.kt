package com.wangari.agritech.ui.screens.rafiki

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.edge.aicore.DownloadConfig
import com.google.ai.edge.aicore.GenerationConfig
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.edge.aicore.DownloadCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GeminiNanoClient(private val context: Context) {
    private val downloadCallback = object : DownloadCallback {
         fun onProgress(progress: Float) {
            Log.d("GeminiNano", "Download progress: ${progress * 100}%")
        }

         fun onCompleted() {
            Log.d("GeminiNano", "Model download completed!")
        }

        fun onError(error: Throwable) {
            Log.e("GeminiNano", "Model download failed", error)
        }
    }

    private val generativeModel: GenerativeModel by lazy {
        val downloadConfig = DownloadConfig(downloadCallback)
        
        val builder = GenerationConfig.Builder()
        builder.context = context
        builder.temperature = 0.2f
        builder.maxOutputTokens = 256
        val config = builder.build()

        GenerativeModel(
            config,
            downloadConfig
        )
    }

    suspend fun generateContent(prompt: String): String? {
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text
        } catch (e: Exception) {
            Log.e("GeminiNanoClient", "Error generating content", e)
            throw e
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RafikiScreen(
    onNavigateBack: () -> Unit,
    viewModel: RafikiViewModel = hiltViewModel() // Get ViewModel
) {
    // Collect the AI response state
    val aiResponse by viewModel.response.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var userPrompt by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rafiki AI Assistant") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Error message display
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            
            Text(
                text = aiResponse,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            )

            // Input Field
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                label = { Text("Ask Rafiki...") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // Button to trigger the AI call
            Button(
                onClick = { viewModel.generateContent(userPrompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = !isLoading && userPrompt.isNotBlank()
            ) {
                if (isLoading) {
                    Text("Generating...")
                } else {
                    Text("Generate Response")
                }
            }
        }
    }
}
@HiltViewModel
class RafikiViewModel @Inject constructor(
    private val geminiClient: GeminiNanoClient
) : ViewModel() {
    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun generateContent(prompt: String) {
        if (prompt.isBlank()) {
            _error.value = "Please enter a prompt"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _response.value = "Thinking..."
                
                val result = geminiClient.generateContent(prompt)
                _response.value = result ?: "No response received"
            } catch (e: Exception) {
                Log.e("RafikiViewModel", "Error generating content", e)
                val errorMessage = when {
                    e.message?.contains("BINDING_FAILURE") == true || 
                    e.message?.contains("CONNECTION_ERROR") == true ->
                        "AI Core service is not available. Please ensure Google AI Core is installed and your device supports it."
                    e.message?.contains("Context is required") == true ->
                        "Configuration error. Please restart the app."
                    else ->
                        "Error: ${e.message ?: "Unknown error occurred"}"
                }
                _error.value = errorMessage
                _response.value = "Sorry, I encountered an error. $errorMessage"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
@Module
@InstallIn(SingletonComponent::class)
object RafikiModule{
    @Provides
    @Singleton
    fun provideGeminiNanoClient(@ApplicationContext context: Context): GeminiNanoClient {
       return GeminiNanoClient(context)
    }
}