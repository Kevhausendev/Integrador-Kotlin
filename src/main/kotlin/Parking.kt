data class Parking(
    val vehicles: MutableSet<Vehicle>,
) {
    private val maxRoom = 20

//    Pair containing (Number of vehicles in parking / earnings)
    var earningsLog = Pair(0,0)

    // Print remaining vehicle plates.
    fun listVehicles() = vehicles.forEach{ println(it.plate) }

    // Update earningsLog whenever a vehicle leaves parking.
    private fun updateCheckoutLog(fee:Int){
        earningsLog = earningsLog.copy( second = earningsLog.second + fee)
        earningsLog = earningsLog.copy(first = earningsLog.first + 1)
    }

    /**
     * @param addVehicle
     * Function that adds vehicle to the set if doesn't exist on the records
     * and if there's enough room
     * **/
    // First check if vehicle is already on parking, then only if there's enough room, is able to check in.
    fun addVehicle(vehicle: Vehicle): Boolean{
        return when{
            vehicles.contains(vehicle) -> {
                println("Sorry, the has check-in failed")
                false
            }
            (vehicles.size < maxRoom) -> {
                println("Welcome to AlkeParking!")
                vehicles.add(vehicle)
                true
            }
            else -> {
                println("Sorry, the check-in failed")
                false
            }
        }
    }

    /**
     * @param validatePlate
     * Function that check by plate, if the vehicle is already parked
     * **/
    fun validatePlate(plate: String): Boolean{
        return this.vehicles.any { it.plate == plate }
    }

    fun findVehicleByPlate(plate:String): Vehicle? {
        return vehicles.find { it.plate == plate }
    }

    fun checkTotalEarnings() =
        println("${earningsLog.first} vehicles have checked out " +
                "and have earnings of $${earningsLog.second}")

//    If vehicle check out is successful, remove vehicle and update log.
    fun checkOutVehicle(vehicle: Vehicle){
        val parkingSpace = ParkingSpace(vehicle,this)
        parkingSpace.checkOutVehicle(vehicle.plate,
            {
                println("Your fee is $it. Come back soon.")
                vehicles.remove(vehicle)
                updateCheckoutLog(it)
            },
            {
                println("Sorry, the check-out failed")
            })
    }


}
