package com.example.myapplication.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.AiService
import com.example.myapplication.data.ChatMessage
import com.example.myapplication.data.SettingsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    aiService: AiService,
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val token by settingsRepository.githubToken.collectAsState(initial = null)
    val aiEnabled by settingsRepository.aiEnabled.collectAsState(initial = true)
    
    var showTokenDialog by remember { mutableStateOf(false) }
    var userMessage by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val listState = rememberLazyListState()
    var isTyping by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MediPlus AI", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showTokenDialog = true }) {
                        Icon(Icons.Default.Key, contentDescription = "Set API Key")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        if (!aiEnabled) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("AI Features are disabled", fontWeight = FontWeight.Bold)
                    Text("You can enable them in the Settings tab.", color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AiWelcomeMessage()
                    }

                    items(messages) { message ->
                        ChatBubble(message)
                    }

                    if (isTyping) {
                        item {
                            TypingIndicator()
                        }
                    }
                }

                ChatInputArea(
                    message = userMessage,
                    onMessageChange = { userMessage = it },
                    onSend = {
                        if (token == null) {
                            showTokenDialog = true
                        } else if (userMessage.isNotBlank()) {
                            val prompt = userMessage
                            messages.add(ChatMessage("user", prompt))
                            userMessage = ""
                            isTyping = true
                            
                            scope.launch {
                                val context = messages.toList()
                                aiService.getCompletion(context).fold(
                                    onSuccess = { response ->
                                        messages.add(ChatMessage("assistant", response))
                                    },
                                    onFailure = { error ->
                                        messages.add(ChatMessage("assistant", "Error: ${error.message}"))
                                    }
                                )
                                isTyping = false
                            }
                        }
                    }
                )
            }
        }

        if (showTokenDialog) {
            TokenDialog(
                onDismiss = { showTokenDialog = false },
                onSave = { newToken ->
                    scope.launch {
                        settingsRepository.saveGithubToken(newToken)
                        showTokenDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.role == "user"
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val containerColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    val shape = if (isUser) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            val textContent = message.content.filter { it.type == "text" }
                .joinToString("\n") { it.text ?: "" }
            
            MarkdownText(
                text = textContent,
                color = textColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun MarkdownText(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = color,
        fontSize = 15.sp,
        modifier = modifier
    )
}

@Composable
fun AiWelcomeMessage() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Healthy Living Suggestions", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("I can help you with healthy eating, meditation tips, or understanding your wellness trends. How can I assist you today?")
        }
    }
}

@Composable
fun ChatInputArea(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                placeholder = { Text("Ask about healthy living...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = onSend,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun TokenDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var tokenText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("GitHub API Token") },
        text = {
            Column {
                Text("Please enter your GitHub Token for GitHub Models AI access.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tokenText,
                    onValueChange = { tokenText = it },
                    label = { Text("Token") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(tokenText) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("AI is thinking...", fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
    }
}
