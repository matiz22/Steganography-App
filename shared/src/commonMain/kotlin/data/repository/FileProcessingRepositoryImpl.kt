package data.repository

import data.consts.StartAndEndOfFileTypes
import domain.model.Photo
import domain.model.PhotoType
import domain.repository.FileProcessingRepository

class FileProcessingRepositoryImpl : FileProcessingRepository {

    /**
     * Adds an encrypted message to a photo by locating the start and end points of the photo file type,
     * extracting the photo content between those points, and appending the encrypted message to it.
     *
     * @param photo The photo object to which the message will be added.
     * @return The modified photo object with the encrypted message added, or null if an error occurs.
     */
    override suspend fun addMessage(photo: Photo): Photo? {
        val startAndEndOfFileType = provideByteSequence(photo) ?: return null
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
                startOfPhoto = (i - matchingIndex) + 1
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
                endOfPhoto = i + matchingIndex
                break
            }
        }
        return try {
            val message = encrypt(text = photo.message).encodeToByteArray()
            val combinedArray = photo.photo.copyOfRange(startOfPhoto, endOfPhoto).plus(message)
            photo.copy(
                photo = combinedArray
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Reads and decrypts a message embedded within a photo. This function locates the start and end points
     * of the photo file type, extracts the encrypted message from the photo content, and decrypts it.
     *
     * @param photo The photo object from which the message will be read.
     * @return The decrypted message extracted from the photo, or null if the message cannot be read or decrypted.
     */
    override suspend fun readMessage(photo: Photo): String? {
        val startAndEndOfFileType = provideByteSequence(photo) ?: return null
        val photoArrayIndexes = photo.photo.indices
        var matchingIndex = 0
        for (i in photoArrayIndexes) {
            if (startAndEndOfFileType.end[matchingIndex] == photo.photo[i]) {
                matchingIndex += 1
            } else {
                matchingIndex = 0
            }
            if (matchingIndex == startAndEndOfFileType.start.size) {
                return decrypt(photo.photo.copyOfRange(i + 1, photo.photo.size).decodeToString())
            }
        }
        return null
    }

    /**
     * Encrypts the input text using a Caesar cipher with the specified shift key.
     * Only alphabetical characters are encrypted; non-alphabetical characters remain unchanged.
     *
     * @param text The text to be encrypted.
     * @param shiftKey The shift key used for encryption. Defaults to 10 if not provided.
     * @return The encrypted text.
     */
    private fun encrypt(text: String, shiftKey: Int = 10): String {
        val result = StringBuilder()

        for (char in text) {
            if (char.isLetter()) {
                val base = if (char.isLowerCase()) 'a' else 'A'
                val encryptedChar = ((char - base + shiftKey) % 26 + base.code).toChar()
                result.append(encryptedChar)
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    /**
     * Decrypts the input text using a Caesar cipher with the specified shift key.
     * Decrypts text encrypted using the `encrypt` function with the same shift key.
     *
     * @param text The text to be decrypted.
     * @param shiftKey The shift key used for encryption. Defaults to 10 if not provided.
     * @return The decrypted text.
     */
    private fun decrypt(text: String, shiftKey: Int = 10): String {
        return encrypt(text, 26 - shiftKey)
    }

    /**
     * Provides the start and end byte sequences of supported image file types based on the photo's file type.
     *
     * @param photo The photo object for which to provide the start and end byte sequences.
     * @return The start and end byte sequences of the photo's file type, or null if the file type is not supported.
     */
    private fun provideByteSequence(photo: Photo): StartAndEndOfFileTypes? =
        when (photo.photoType) {
            PhotoType.JPG -> {
                StartAndEndOfFileTypes.JPGFileStartEnd
            }

            PhotoType.PNG -> {
                StartAndEndOfFileTypes.PNGFileStartEnd
            }

            PhotoType.TypeNotSupported -> {
                null
            }
        }
}