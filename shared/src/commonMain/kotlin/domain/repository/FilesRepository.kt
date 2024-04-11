package domain.repository

import domain.model.Photo

interface FilesRepository {
    suspend fun savePhoto(photo: Photo): Boolean
}