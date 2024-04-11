package data.repository

import data.consts.StartAndEndOfFileTypes
import domain.model.Photo
import domain.model.PhotoType
import domain.repository.FileProcessingRepository

class FileProcessingRepositoryImpl: FileProcessingRepository {
    override suspend fun addMessage(photo: Photo): Photo? {
        val startAndEndOfFileTypes = when(photo.photoType){
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
        val startIndex = photo.photo.filterIndexed { index, byte ->
            
        }
    }
}