package com.matiz22.steganography_app.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matiz22.steganography_app.presentation.event.FileEvents
import com.matiz22.steganography_app.presentation.viewmodels.FileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickingScreen(
    fileViewModel: FileViewModel = viewModel(),
    actions: (FileEvents) -> Unit
) {
    val image = fileViewModel.selectedImageUri
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            actions(FileEvents.PickFile(uri = uri))
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(title = {

            })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {

        }
    }
}