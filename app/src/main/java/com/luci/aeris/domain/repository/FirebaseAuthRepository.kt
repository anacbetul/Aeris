package com.luci.aeris.domain.repository

import com.luci.aeris.domain.model.UserCredentials

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

enum class FirebaseAuthState{
    Authenticated,
    UnAuthenticated
}

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val currentUser get() = firebaseAuth.currentUser


    private val _authState = MutableStateFlow<FirebaseAuthState>(
        if (firebaseAuth.currentUser != null) FirebaseAuthState.Authenticated else FirebaseAuthState.UnAuthenticated
    )
    val authState: StateFlow<FirebaseAuthState> = _authState

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val state = if (auth.currentUser != null) {
            FirebaseAuthState.Authenticated
        } else {
            FirebaseAuthState.UnAuthenticated
        }
        _authState.value = state
    }

    suspend fun signInWithEmail(email: String, password: String): Result<UserCredentials> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User is null after sign-in")
            val tokenResult = user.getIdToken(true).await()
            val token = tokenResult.token ?: throw Exception("ID Token null")
            Result.success(UserCredentials(user.uid, user.email, token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerWithEmail(email: String, password: String): Result<UserCredentials> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User is null after registration")
            val tokenResult = user.getIdToken(true).await()
            val token = tokenResult.token ?: throw Exception("ID Token null")
            Result.success(UserCredentials(user.uid, user.email, token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerWithGoogle(account: GoogleSignInAccount): Result<UserCredentials> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()

            val user = result.user ?: throw Exception("User is null after Google registration")
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false

            if (!isNewUser) {
                throw Exception("User already exists")
            }

            val tokenResult = user.getIdToken(true).await()
            val token = tokenResult.token ?: throw Exception("ID Token null")
            Result.success(UserCredentials(user.uid, user.email, token))
        } catch (e: Exception) {
            Result.failure(e)
        }


    }
    suspend fun sigInGoogle(account: GoogleSignInAccount): Result<UserCredentials> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await() // suspend extension kullanarak
            val user = authResult.user ?: throw Exception("No user found after Google sign-in")
            val idTokenResult = user.getIdToken(true).await()
            val credentials = UserCredentials(
                uid = user.uid,
                email = user.email,
                idToken = idTokenResult.token?:""
            )
            Result.success(credentials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun cleanup() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}

