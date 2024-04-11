package domain.model

data class Photo(
    val message: String,
    val photo: ByteArray,
    val photoType: PhotoType
)