package com.matiz22.steganography_app.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.matiz22.steganography_app.presentation.event.FileEvents
import com.matiz22.steganography_app.presentation.viewmodels.FileViewModel
import steganography_app.composeapp.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickingScreen(
    fileViewModel: FileViewModel = viewModel(),
) {
    val image = fileViewModel.selectedImageUri
    val decoded = fileViewModel.decodedMessage
    val message = fileViewModel.message

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            fileViewModel.onEvent(FileEvents.PickFile(uri = uri))
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Steganography App")
            })
        }
    ) {
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
                    model = image,
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
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                TextField(
                    value = message,
                    onValueChange = { string ->
                        fileViewModel.onEvent(FileEvents.UpdateMessage(string))
                    }
                )
                Button(
                    onClick = {
                        fileViewModel.onEvent(FileEvents.AddMessage)
                    }
                ){
                    Text(text = "Save message")
                }
            }
        }
    }
}