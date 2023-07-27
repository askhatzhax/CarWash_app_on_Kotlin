package ua.cn.stu.navcomponent.tabs.screens.main.tabs.QueueBooking

import BookingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.model.Boook.QBooking

class AdminRequestListFragment: Fragment(R.layout.fragment_admin_list) {
    private lateinit var listViewBooking: ListView
    private lateinit var bookingAdapter: ArrayAdapter<String>
    private lateinit var database: FirebaseDatabase
    private lateinit var bookingsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_list, container, false)
        database = FirebaseDatabase.getInstance()

        bookingsRef = database.reference.child("markers").child("booking")
        listViewBooking = view.findViewById(R.id.listViewBooking)

        // Инициализация адаптера для списка бронирования
        bookingAdapter = BookingAdapter(requireContext(), R.layout.item_booking, ArrayList())
        listViewBooking.adapter = bookingAdapter
        loadBookingDataFromFirebase()
        // Установка слушателя для удаления элементов
        (bookingAdapter as BookingAdapter).setOnItemClickListener { booking ->
            // Выполнение действий с выбранным элементом
            // Удаление выбранного элемента из Firebase
            deleteBookingFromFirebase(booking)
        }

        // Загрузка данных из Firebase
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            resources.getString(R.string.request)
        return view
    }

    private fun loadBookingDataFromFirebase() {
        // Чтение данных из Firebase Realtime Database
        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Очистка адаптера перед загрузкой новых данных
                bookingAdapter.clear()

                // Заполнение адаптера данными из Firebase
                for (snapshot in dataSnapshot.children) {
                    val carwash = snapshot.child("carWash").getValue(String::class.java)!!
                    val userid = snapshot.child("userId").getValue(String::class.java)!!
                    val service = snapshot.child("serviceType").getValue(String::class.java)!!
                    val date = snapshot.child("bookingDate").getValue(String::class.java)!!
                    val time = snapshot.child("bookingTime").getValue(String::class.java)!!
                    val booking_id = snapshot.child("booking_Id").getValue(String::class.java)!!
                    val bookingInfo = "Booking_Id: ${booking_id}, \nUsername: ${userid}, Carwash: ${carwash},\n Servicetype: ${service}\n Date: ${date}. Time: ${time}\n"
                    bookingAdapter.add(bookingInfo)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки при чтении данных из Firebase
                error.toException().printStackTrace()
            }
        })
    }

    private fun deleteBookingFromFirebase(booking: String?) {
        var cleanedPath = booking?.replace(".", "")
            ?.replace("#", "")
            ?.replace("$", "")
            ?.replace("[", "")
            ?.replace("]", "")
        if (cleanedPath != null) {
            cleanedPath = cleanedPath.substring(12,19)
        }
        if (cleanedPath.isNullOrEmpty()) {
            return
        }
        // Получите ссылку на узел базы данных, содержащий список бронирований
        val bookingsRef = FirebaseDatabase.getInstance().getReference("markers").child("booking")

        // Используйте метод removeValue() для удаления выбранного элемента из Firebase
        bookingsRef.child(cleanedPath).removeValue()
            .addOnSuccessListener {
                // Успешное удаление элемента
                Toast.makeText(requireContext(), "Booking deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                // Обработка ошибки при удалении элемента
                Toast.makeText(requireContext(), "Failed to delete booking: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}