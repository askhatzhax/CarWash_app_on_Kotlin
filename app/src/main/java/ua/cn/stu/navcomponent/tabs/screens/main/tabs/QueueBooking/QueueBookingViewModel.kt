package ua.cn.stu.navcomponent.tabs.screens.main.tabs.QueueBooking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.cn.stu.navcomponent.tabs.model.Booking.FirebaseRepository
import ua.cn.stu.navcomponent.tabs.model.Booking.QueueBooking

class QueueBookingViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _booking = MutableLiveData<QueueBooking>()
    val booking: LiveData<QueueBooking>
        get() = _booking

    fun saveQueueBooking() {
        val booking = _booking.value ?: return
        repository.saveQueueBooking(booking)
    }

    fun setBooking(carType: String, carModel: String, washType: String, bookingDate: String, bookingTime: String, userId: String) {
        _booking.value = QueueBooking(carType, carModel, washType, bookingDate, bookingTime, userId)
    }
}