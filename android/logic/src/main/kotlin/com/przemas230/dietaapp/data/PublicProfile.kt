package com.przemas230.dietaapp.data

/**
 * FR-76: a row from Firestore's `publicProfiles/{uid}` collection -- the
 * JUST-public subset of a real (non-anonymous) account, deliberately never
 * including email/diet profile/pantry/favorites. Mirrors index.html's
 * `openUserListModal`/`openUserProfileModal` document shape.
 */
data class PublicProfile(
    val uid: String,
    val displayName: String,
    val lastLoginAtMillis: Long?,
)
