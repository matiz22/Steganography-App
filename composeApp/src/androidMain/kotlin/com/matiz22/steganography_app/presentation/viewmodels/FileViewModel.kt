package com.matiz22.steganography_app.presentation.viewmodels

import FileProcessingRepositoryImpl
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matiz22.steganography_app.presentation.event.FileEvents
import domain.model.Photo
import domain.model.PhotoType
import domain.repository.FileProcessingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.resolveImageExtension
import java.io.File
import java.io.FileOutputStream

class FileViewModel : ViewModel() {
    var selectedImage by mutableStateOf<Photo?>(null)
    var decodedMessage by mutableStateOf<String?>("")
    var message by mutableStateOf<String>("")

    private val fileProcessingRepository: FileProcessingRepository = FileProcessingRepositoryImpl()
    private val _resultsChannel = Channel<String>()
    val resultsChannel = _resultsChannel.receiveAsFlow()
    fun onEvent(fileEvent: FileEvents) {
        when (fileEvent) {
            is FileEvents.PickFile -> {
                selectedImage = Photo(
                    message = "", photo = fileEvent.bytes, photoType = fileEvent.type
                )
                viewModelScope.launch {
                    val message = fileProcessingRepository.readMessage(photo = selectedImage!!)
                    selectedImage = selectedImage?.copy(message = message!!)
                }

            }

            is FileEvents.UnpickFile -> {
                selectedImage = null
            }

            is FileEvents.UpdateMessage -> {
                message = fileEvent.message
            }

            is FileEvents.AddMessage -> {
                if (selectedImage != null) {
                    val photo = selectedImage!!.copy(message = message)
                    viewModelScope.launch {
                        selectedImage = fileProcessingRepository.addMessage(photo = photo)
                        if (selectedImage == null) {
                            _resultsChannel.send("File not supported")
                        } else {
                            _resultsChannel.send("Message added")
                        }
                    }
                }
            }

            is FileEvents.SaveToStorage -> {
                if (selectedImage != null) {
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            val file = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                "${System.currentTimeMillis()}.${selectedImage?.photoType?.name}"
                            )
                            FileOutputStream(file).use {
                                it.write(selectedImage?.photo)
                            }
                        }
                        withContext(Dispatchers.Main) {
                            _resultsChannel.send("File saved to picture folder")
                        }
                    }


                } else {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            _resultsChannel.send("File not selected")
                        }
                    }
                }
            }
        }
    }
}