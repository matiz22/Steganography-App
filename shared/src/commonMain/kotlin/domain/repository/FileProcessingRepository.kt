package domain.repository

import domain.model.Photo

interface FileProcessingRepository {
    suspend fun addMessage(photo: Photo): Photo?
}