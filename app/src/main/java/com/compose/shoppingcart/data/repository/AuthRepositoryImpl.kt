package com.compose.shoppingcart.data.repository

import com.compose.shoppingcart.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> =
        suspendCoroutine { cont ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(Result.success(Unit))
                    } else {
                        cont.resume(
                            Result.failure(
                                task.exception ?: Exception("Error desconocido")
                            )
                        )
                    }
                }
        }

    override suspend fun register(email: String, password: String): Result<Unit> =
        suspendCoroutine { cont ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(Result.success(Unit))
                    } else {
                        cont.resume(
                            Result.failure(
                                task.exception ?: Exception("Error desconocido")
                            )
                        )
                    }
                }
        }

    override fun logout() = firebaseAuth.signOut()

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
}
