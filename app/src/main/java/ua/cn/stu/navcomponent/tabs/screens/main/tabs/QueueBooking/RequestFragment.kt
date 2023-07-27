package ua.cn.stu.navcomponent.tabs.screens.main.tabs.QueueBooking
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.model.Boook.QBooking
import ua.cn.stu.navcomponent.tabs.model.carwashs.Carwash
import ua.cn.stu.navcomponent.tabs.screens.main.usermaiil
import ua.cn.stu.navcomponent.tabs.utils.MutableUnitLiveEvent
import ua.cn.stu.navcomponent.tabs.utils.publishEvent
import java.util.*


class RequestFragment: Fragment(R.layout.fragment_request) {
    private lateinit var database: DatabaseReference
    private lateinit var carwashAdapter: ArrayAdapter<String>
    private lateinit var spinnerсar: Spinner
    lateinit var radioGroupServiceType: RadioGroup
    lateinit var spinnerCarType: Spinner
    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker
    lateinit var buttonBook: Button
    lateinit var usermail: String
    private val _navigateToTabsEvent = MutableUnitLiveEvent()
    private fun launchAdminScreen() = _navigateToTabsEvent.publishEvent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request, container, false)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        if (usermaiil == "@admin@gmail.com") {
            // Открытие фрагмента для администратора
           /* val adminFragment = AdminRequestListFragment()
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, adminFragment)
                .commit()*/
            val navController = findNavController()
            // Открываем новый граф навигации
            navController.navigate(R.id.admin_list)
        }
        else {

            // Initialize Firebase database reference
            var spinnercar = view.findViewById<Spinner>(R.id.spinner_carwash)
            radioGroupServiceType = view.findViewById<RadioGroup>(R.id.radio_group_service_type)
            spinnerCarType = view.findViewById<Spinner>(R.id.spinner_car_type)
            datePicker = view.findViewById<DatePicker>(R.id.date_picker)
            timePicker = view.findViewById<TimePicker>(R.id.time_picker)
            buttonBook = view.findViewById<Button>(R.id.button_book)
            usermail = usermaiil
            buttonBook.setOnClickListener {
                database =
                    FirebaseDatabase.getInstance().reference.child("markers").child("booking")
                var bookingId = UUID.randomUUID().toString()
                bookingId = bookingId.substring(0,7)
                val carwash = spinnercar.selectedItem.toString()
                var serviceType=""
                if(view.findViewById<RadioButton>(radioGroupServiceType.checkedRadioButtonId).text.toString()==null) {
                    serviceType = R.string.standard_service.toString()
                }
                else{
                    serviceType = view.findViewById<RadioButton>(radioGroupServiceType.checkedRadioButtonId).text.toString()
                }
                val carType = spinnerCarType.selectedItem.toString()
                val date = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                val time = "${timePicker.hour}:${timePicker.minute}"
                val booking =
                    QBooking(bookingId, carwash, serviceType, carType, date, time, usermail)
                database.child(bookingId).setValue(booking)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Booking successful", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Booking failed", Toast.LENGTH_SHORT).show()
                    }
            }
            val carwashList = mutableListOf<String>()
            carwashAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                carwashList
            )
            spinnercar.adapter = carwashAdapter
            database = FirebaseDatabase.getInstance().reference.child("markers")
            val carwash1 = Carwash(10001, "DDTе", 1000, "Almaty")
            val carwash2 = Carwash(10002, "Car Wash", 1200, "Almaty")
            val carwash3 = Carwash(10003, "Service wash", 1400, "Almaty")
            val carwash4 = Carwash(10004, "Super wash", 1600, "Almaty")
            val carwash5 = Carwash(10005, "Super moika", 2000, "Almaty")
            val carwash6 = Carwash(10006, "Car spa", 800, "Almaty")
            database.child("carwashs").child("carwash1").setValue(carwash1)
            database.child("carwashs").child("carwash2").setValue(carwash2)
            database.child("carwashs").child("carwash3").setValue(carwash3)
            database.child("carwashs").child("carwash4").setValue(carwash4)
            database.child("carwashs").child("carwash5").setValue(carwash5)
            database.child("carwashs").child("carwash6").setValue(carwash6)
            database = FirebaseDatabase.getInstance().reference.child("markers").child("carwashs")
            spinnercar.adapter = carwashAdapter
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    carwashList.clear()
                    for (carwashSnapshot in dataSnapshot.children) {
                        val carwashName =
                            carwashSnapshot.child("name").getValue(String::class.java)!!
                        carwashList.add(carwashName)
                    }
                    carwashAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        carwashList
                    )
                    carwashAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnercar.adapter = carwashAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read value.", error.toException())
                }
            })
            (activity as AppCompatActivity?)!!.supportActionBar!!.title =
                resources.getString(R.string.request)
        }
        return view
    }
}