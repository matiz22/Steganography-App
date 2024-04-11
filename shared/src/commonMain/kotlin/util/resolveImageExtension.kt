package util

import domain.model.PhotoType

fun resolveImageExtension(imageType: String): PhotoType {
    return when (imageType.split('/').last()) {
        "jpeg" -> PhotoType.JPG
        "png" -> PhotoType.PNG
        else -> PhotoType.TypeNotSupported
    }
}