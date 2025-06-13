package com.luci.aeris.domain.repository

import Clothes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.luci.aeris.domain.model.User
import com.luci.aeris.utils.constants.StringConstants
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val usersCollection = firestore.collection(StringConstants.users)

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

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    suspend fun addClothesForCurrentUser(clothes: Clothes): Result<Unit> {
        val userId = getUserId()
        return if (userId != null) {
            try {
                usersCollection
                    .document(userId)
                    .collection(StringConstants.clothes)
                    .document(clothes.id)
                    .set(clothes)
                    .await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("User is not logged in"))
        }
    }

    suspend fun getClothesForCurrentUser(): Result<List<Clothes>> {
        val userId = getUserId()
        return if (userId != null) {
            try {
                val snapshot = usersCollection
                    .document(userId)
                    .collection(StringConstants.clothes)
                    .get()
                    .await()

                val clothesList = snapshot.documents.mapNotNull { it.toObject(Clothes::class.java) }
                Result.success(clothesList)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("User is not logged in"))
        }
    }

    suspend fun deleteClothesForCurrentUser(idClothes: String): Result<Boolean> {
        val userId = getUserId()
        return if (userId != null) {
            try {
                usersCollection
                    .document(userId)
                    .collection(StringConstants.clothes)
                    .document(idClothes)
                    .delete()
                    .await()

                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("User is not logged in"))
        }
    }

}
