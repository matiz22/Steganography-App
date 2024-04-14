package com.matiz22.steganography_app.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.matiz22.steganography_app.presentation.event.FileEvents
import com.matiz22.steganography_app.presentation.viewmodels.FileViewModel
import util.resolveImageExtension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickingScreen(
    fileViewModel: FileViewModel = viewModel(),
) {
    val image = fileViewModel.selectedImage
    val decoded = fileViewModel.decodedMessage
    val message = fileViewModel.message
    val context = LocalContext.current
    val channel = fileViewModel.resultsChannel
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(channel) {
        channel.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    fileViewModel.onEvent(
                        FileEvents.PickFile(
                            bytes = input.readBytes(), type = resolveImageExtension(
                                context.contentResolver.getType(uri) ?: ""
                            )
                        )
                    )
                }
            }
        })

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Steganography App")
        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.size(400.dp),
                    model = image?.photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(text = "Decoded message: $decoded")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Button(onClick = {
                    photoPicker.launch("image/*")
                }) {
                    Text(text = "Pick")
                }
                OutlinedButton(onClick = {
                    fileViewModel.onEvent(FileEvents.UnpickFile)
                }) {
                    Text(text = "Unpick")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = message, onValueChange = { string ->
                    fileViewModel.onEvent(FileEvents.UpdateMessage(string))
                })
                Button(onClick = {
                    fileViewModel.onEvent(FileEvents.AddMessage)
                }) {
                    Text(text = "Add message")
                }
                Button(onClick = {
                    fileViewModel.onEvent(FileEvents.SaveToStorage)
                }) {
                    Text(text = "Save message")
                }
            }
        }
    }
}