import com.example.bookmybook.models.Book
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val rentList = mutableListOf<Rent>()
data class Rent(
    val bookId: Long,
    val contactId: Long,
    val startDate: Calendar,
    val returnDate: Calendar,
    val id: Int = rentList.size
) {
    fun getFormattedDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
