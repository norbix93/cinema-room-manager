package cinema


class CinemaManager(private val rows: Int, private val seatsInEachRow: Int) {

    private val totalSeats: Int = rows * seatsInEachRow
    private val occupiedSeats: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private var numberOfPurchasedTickets: Int = 0
    private var currentIncome: Int = 0

    fun startMenu() {
        do {
            println(
                """
                1. Show the seats
                2. Buy a ticket
                3. Statistics
                0. Exit
            """.trimIndent()
            )
            val option: String = readln()
            when (option) {
                "1" -> this.showSeats()
                "2" -> this.buyATicket()
                "3" -> this.showStatistics()
            }
        } while (option != "0")
    }

    private fun showSeats() {
        var room = " "
        var seatIsSelected = false

        println("Cinema:")
        for (row in 0..rows) {
            if (row == 0) {
                for (seat in 1..seatsInEachRow) {
                    room += " $seat"
                }
            } else {
                room += "\n$row"
                for (seat in 1..seatsInEachRow) {
                    for ((keyRow, seatsList) in occupiedSeats) {
                        if (row == keyRow && seatsList.contains(seat)) seatIsSelected = true
                    }
                    room += if (seatIsSelected) " B" else " S"
                    seatIsSelected = false
                }
            }
        }
        println(room)
    }

    private fun buyATicket() {
        var wrongInput = true
        do {
            try {
                println("Enter a row number:")
                val row: Int = readln().toInt()
                println("Enter a seat number in that row:")
                val seat: Int = readln().toInt()
                if (row > rows || seat > seatsInEachRow) throw OutOfBoundsException()
                if (!occupiedSeats.containsKey(row)) {
                    occupiedSeats[row] = mutableListOf(seat)
                    numberOfPurchasedTickets++
                    currentIncome += calculateTicketPrice(row)
                } else {
                    if (occupiedSeats[row]!!.contains(seat)) throw SeatIsTaken()
                    occupiedSeats[row]!!.add(seat)
                    numberOfPurchasedTickets++
                    currentIncome += calculateTicketPrice(row)
                }
                println("Ticket price: $${calculateTicketPrice(row)}")
                wrongInput = false
            } catch (e: SeatIsTaken) {
                println(e.message)
                continue
            } catch (e: OutOfBoundsException) {
                println(e.message)
                continue
            }
        } while (wrongInput)
    }

    private fun calculateTicketPrice(row: Int): Int {
        return if (row > 4) 8 else 10
    }

    private fun calculatePercentageOfPurchasedTickets(): Double {
        return 100.00 * numberOfPurchasedTickets / totalSeats
    }

    private fun showStatistics() {
        println(
            """
            Number of purchased tickets: $numberOfPurchasedTickets
            Percentage: ${"%.2f".format(calculatePercentageOfPurchasedTickets())}%
            Current income: $$currentIncome
            Total income: $${calculateProfit()}
        """.trimIndent()
        )
    }

    private fun calculateProfit(): Int {
        val profit: Int = if (totalSeats < 60) {
            totalSeats * 10
        } else {
            if (rows % 2 == 0) {
                (totalSeats / 2 * 10) + (totalSeats / 2 * 8)
            } else {
                ((rows / 2) * seatsInEachRow * 10) + ((rows / 2 + 1) * seatsInEachRow * 8)
            }
        }
        return profit
    }
}