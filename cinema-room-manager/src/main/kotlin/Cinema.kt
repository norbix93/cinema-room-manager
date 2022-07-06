package cinema

fun main() {
    println("Enter the number of rows:")
    val rows: Int = readln().toInt()

    println("Enter the number of seats in each row:")
    val seatsInEachRow: Int = readln().toInt()

    val cinema = CinemaManager(rows, seatsInEachRow)

    cinema.startMenu()
}

