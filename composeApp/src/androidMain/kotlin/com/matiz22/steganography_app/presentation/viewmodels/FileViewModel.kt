package com.matiz22.steganography_app.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import com.matiz22.steganography_app.presentation.event.FileEvents
import domain.model.Photo
import domain.model.PhotoType

class FileViewModel : ViewModel() {
    var selectedImage by mutableStateOf<Photo?>(null)
    var decodedMessage by mutableStateOf<String>("")
    var message by mutableStateOf<String>("")

    fun onEvent(fileEvent: FileEvents) {
        when (fileEvent) {
            is FileEvents.PickFile -> {
                selectedImage = Photo(
                    message = "",
                    photo = fileEvent.bytes,
                    photoType = PhotoType.JPG
                )
            }

            is FileEvents.UnpickFile -> {
                selectedImage = null
            }

            is FileEvents.UpdateMessage -> {
                message = fileEvent.message
            }

            is FileEvents.AddMessage -> {

            }
        }
    }
}