import java.util.*
import kotlin.math.ceil

const val MINUTES_IN_MILLISECONDS = 60000

data class ParkingSpace(var vehicle: Vehicle, val parking: Parking) {
    private val checkInTime = vehicle.checkInTime
    private val parkedTime : Long
        get() = (Calendar.getInstance().timeInMillis - checkInTime ) / MINUTES_IN_MILLISECONDS


    fun checkOutVehicle(plate: String, onSuccess: (Int) -> Unit, onError: () -> Unit){
        val vehicle = parking.findVehicleByPlate(plate)
        val fee = vehicle?.let { calculateFee(it.type, parkedTime.toInt(),hasVehicleDiscount(it)) } ?: 0
        if (parking.validatePlate(plate)){
            onSuccess(fee)
        }else{
            onError()
        }
    }

    private fun hasVehicleDiscount(vehicle: Vehicle):Boolean{
        return vehicle.discountCard?.isNotBlank() ?: false
    }

    /**
     * @param calculateFee function that calculates fee of vehicle depending
     * on parked time and rate of specific vehicle type.
     * For the first two hours, the fee is flat. For every 15 minutes after two hours, the fee increases
     * by $5.
     * If vehicle has a discount card, the fee is decreased by 15%.
     * */
    private fun calculateFee(type: VehicleType, parkedTime: Int, hasDiscountCard:Boolean): Int{

        // Declaring every usable constants as doubles for easier calculation.
        val twoHoursInMinutes = 120.0
        val extraFeePerBlock = 5.0
        val block = 15.0
        val flatFee = type.rate.toDouble()

        // If vehicle has discount, multiply fee by 0.85 (15% discount). Multiply by 1 if not.
        val discountRate = if(hasDiscountCard) 0.85 else 1.0

        return if(parkedTime>twoHoursInMinutes){
            val extraMinutes = parkedTime - twoHoursInMinutes
            val blocks = ceil(extraMinutes / block)
            val extraFee = blocks * extraFeePerBlock
            val fee = flatFee + extraFee
            val totalFee = ceil(fee * discountRate)
            totalFee.toInt()
        }else{
            val totalFee = ceil(flatFee*discountRate)
            totalFee.toInt()
        }

    }
}
