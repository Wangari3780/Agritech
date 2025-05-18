package com.wangari.agritech.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.wangari.agritech.models.AccountType
import com.wangari.agritech.models.NotificationSettings
import com.wangari.agritech.models.User
import kotlinx.coroutines.tasks.await
import java.util.Date


class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signUp(email: String, password: String, name: String, phone: String, location: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to create user")

            val user = User(
                uid = firebaseUser.uid,
                name = name,
                email = email,
                phone = phone,
                location = location,
                accountType = AccountType.FARMER,
                createdAt = Date()
            )

            usersCollection.document(firebaseUser.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to sign in")

            val userDoc = usersCollection.document(firebaseUser.uid).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("User profile not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val userDoc = usersCollection.document(userId).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("User profile not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMpesaInfo(userId: String, mpesaPhone: String, mpesaLinked: Boolean): Result<Boolean> {
        return try {
            usersCollection.document(userId).update(
                mapOf(
                    "mpesaPhone" to mpesaPhone,
                    "mpesaLinked" to mpesaLinked
                )
            ).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotificationSettings(userId: String, settings: NotificationSettings): Result<Boolean> {
        return try {
            usersCollection.document(userId).update("notificationSettings", settings).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}