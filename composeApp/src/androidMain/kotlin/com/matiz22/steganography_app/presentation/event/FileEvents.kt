package com.matiz22.steganography_app.presentation.event

import domain.model.PhotoType


sealed class FileEvents {
    data class PickFile(val bytes: ByteArray, val type: PhotoType) : FileEvents()
    data object UnpickFile : FileEvents()
    data class UpdateMessage(val message: String) : FileEvents()
    data object SaveToStorage : FileEvents()
    data object AddMessage : FileEvents()
}