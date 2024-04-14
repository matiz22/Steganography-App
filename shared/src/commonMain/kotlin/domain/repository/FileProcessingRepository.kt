package domain.repository

import domain.model.Photo

interface FileProcessingRepository {
    suspend fun addMessage(photo: Photo): Photo?
    suspend fun readMessage(photo: Photo): String?
}