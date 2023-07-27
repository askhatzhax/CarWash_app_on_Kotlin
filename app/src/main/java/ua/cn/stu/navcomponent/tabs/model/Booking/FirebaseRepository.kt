package ua.cn.stu.navcomponent.tabs.model.Booking

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    private val db = Firebase.firestore

    fun saveQueueBooking(booking: QueueBooking) {
        db.collection("queue_bookings")
            .add(booking)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}