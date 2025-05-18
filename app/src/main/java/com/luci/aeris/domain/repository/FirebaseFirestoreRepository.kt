package com.luci.aeris.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.luci.aeris.domain.model.User
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val usersCollection = firestore.collection("users")

    // Kullanıcı Firestore'a kaydedilir (ilk kayıt veya profil güncelleme)
    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // UID ile kullanıcı verilerini getir
    suspend fun getUser(uid: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            if (snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data is null"))
                }
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Kullanıcıyı Firestore'dan sil
    suspend fun deleteUser(uid: String): Result<Unit> {
        return try {
            usersCollection.document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
