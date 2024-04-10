package com.matiz22.steganography_app.presentation.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.matiz22.steganography_app.presentation.event.FileEvents

class FileViewModel : ViewModel() {
    var selectedImageUri by mutableStateOf<Uri?>(null)
    fun onEvent(fileEvent: FileEvents) {
        when (fileEvent) {
            is FileEvents.PickFile -> {
                selectedImageUri = fileEvent.uri
            }

            is FileEvents.UnpickFile -> {
                selectedImageUri = null
            }
        }
    }
}