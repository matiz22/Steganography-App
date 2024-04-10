package com.matiz22.steganography_app.presentation.event

import android.net.Uri


sealed class FileEvents {
    data class PickFile(val uri: Uri?) : FileEvents()
    data object UnpickFile : FileEvents()
}