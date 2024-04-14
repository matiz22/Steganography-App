import data.consts.StartAndEndOfFileTypes
import domain.model.Photo
import domain.model.PhotoType
import domain.repository.FileProcessingRepository

class FileProcessingRepositoryImpl : FileProcessingRepository {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun addMessage(photo: Photo): Photo? {
        val startAndEndOfFileType = when (photo.photoType) {
            PhotoType.JPG -> {
                StartAndEndOfFileTypes.JPGFileStartEnd
            }

            PhotoType.PNG -> {
                StartAndEndOfFileTypes.PNGFileStartEnd
            }

            PhotoType.TypeNotSupported -> {
                return null
            }
        }
        var matchingIndex = 0
        val photoArrayIndexes = photo.photo.indices
        var startOfPhoto: Int = 0
        for (i in photoArrayIndexes) {
            if (startAndEndOfFileType.start[matchingIndex] == photo.photo[i]) {
                matchingIndex += 1
            } else {
                matchingIndex = 0
            }
            if (matchingIndex == startAndEndOfFileType.start.size) {
                startOfPhoto = i - matchingIndex
                matchingIndex = 0
                break
            }
        }
        var endOfPhoto: Int = 0
        val reversedEndSequence = startAndEndOfFileType.end.reversed()
        for (i in photoArrayIndexes.reversed()) {
            if (reversedEndSequence[matchingIndex] == photo.photo[i]) {
                matchingIndex += 1
            } else {
                matchingIndex = 0
            }
            if (matchingIndex == startAndEndOfFileType.end.size) {
                endOfPhoto = i - matchingIndex
                break
            }
        }
        return try {
            photo.copy(
                photo = photo.photo.copyOfRange(
                    startOfPhoto,
                    endOfPhoto
                ) + photo.message.hexToByteArray()
            )
        } catch (e: Exception) {
            null
        }
    }
}