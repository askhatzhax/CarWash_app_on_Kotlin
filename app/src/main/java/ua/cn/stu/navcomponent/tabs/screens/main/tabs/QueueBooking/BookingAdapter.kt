import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import ua.cn.stu.navcomponent.tabs.R

class BookingAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    fun setOnItemClickListener(listener: (String?) -> Unit) {
        itemClickListener = listener
    }

    // Внутри вашего адаптера
    private var itemClickListener: ((String?) -> Unit)? = null


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_booking, parent, false)
        }

        val bookingInfo = getItem(position)
        val textViewBookingItem: TextView = view!!.findViewById(R.id.textViewTime)
        val buttonDeleteBooking: Button = view.findViewById(R.id.buttonDelete)

        textViewBookingItem.text = bookingInfo

        buttonDeleteBooking.setOnClickListener {
            val booking = getItem(position)
            itemClickListener?.invoke(booking)
        }

        // Установка обработчика события на кнопку удаления

        return view
    }
}



