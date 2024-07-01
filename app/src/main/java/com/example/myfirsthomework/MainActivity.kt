package com.example.myfirsthomework

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.buttonToRace)
        val numberOfCars = findViewById<EditText>(R.id.numberOfCars)

        button.setOnClickListener {
            val counter = numberOfCars.text.toString().toIntOrNull() ?: return@setOnClickListener
            val listOfCars = generatingCarsForRace(counter)
            listOfCars.forEach { it.printInfo() } // Добавлен вывод информации об автомобилях
            val winner = game(listOfCars)
            Log.d("Race", "Final winner: ${winner.mark} ${winner.model}")
        }
    }
}

open class Car(
    val mark: String,
    val model: String,
    val yearOfRelease: Int,
    val maxSpeed: Int
) {
    open fun printInfo() {
        println("Mark of Car: $mark, Model: $model, Year: $yearOfRelease, Max Speed: $maxSpeed")
    }

    override fun toString(): String {
        return "Car(mark='$mark', model='$model', yearOfRelease=$yearOfRelease, maxSpeed=$maxSpeed)"
    }
}

class Crossover(
    mark: String,
    model: String,
    yearOfRelease: Int,
    maxSpeed: Int,
    var driveType: String
) : Car(mark, model, yearOfRelease, maxSpeed) {
    override fun printInfo() {
        super.printInfo()
        println("Type of Drive: $driveType")
    }

    override fun toString(): String {
        return "Crossover(mark='$mark', model='$model', yearOfRelease=$yearOfRelease, maxSpeed=$maxSpeed, driveType='$driveType')"
    }
}

class Sedan(
    mark: String,
    model: String,
    yearOfRelease: Int,
    maxSpeed: Int,
    val capacityOfTrunk: Int
) : Car(mark, model, yearOfRelease, maxSpeed) {
    override fun printInfo() {
        super.printInfo()
        println("Capacity: $capacityOfTrunk")
    }

    override fun toString(): String {
        return "Sedan(mark='$mark', model='$model', yearOfRelease=$yearOfRelease, maxSpeed=$maxSpeed, capacityOfTrunk=$capacityOfTrunk)"
    }
}

class Track(
    mark: String,
    model: String,
    yearOfRelease: Int,
    maxSpeed: Int,
    val weight: Int
) : Car(mark, model, yearOfRelease, maxSpeed) {
    override fun printInfo() {
        super.printInfo()
        println("Weight of car: $weight")
    }

    override fun toString(): String {
        return "Track(mark='$mark', model='$model', yearOfRelease=$yearOfRelease, maxSpeed=$maxSpeed, weight=$weight)"
    }
}

class Bus(
    mark: String,
    model: String,
    yearOfRelease: Int,
    maxSpeed: Int,
    val numberOfDoors: Int
) : Car(mark, model, yearOfRelease, maxSpeed) {
    override fun printInfo() {
        super.printInfo()
        println("Number of doors: $numberOfDoors")
    }

    override fun toString(): String {
        return "Bus(mark='$mark', model='$model', yearOfRelease=$yearOfRelease, maxSpeed=$maxSpeed, numberOfDoors=$numberOfDoors)"
    }
}

class CarBuilder(private val mark: String) {
    private var model: String = ""
    private var yearOfRelease: Int = 0
    private var maxSpeed: Int = 0

    fun setModel(model: String) = apply { this.model = model }
    fun setYearOfRelease(year: Int) = apply { this.yearOfRelease = year }
    fun setMaxSpeed(speed: Int) = apply { this.maxSpeed = speed }

    fun buildCar(): Car {
        return Car(mark, model, yearOfRelease, maxSpeed)
    }
}

fun raceCars(firstCar: Car, secondCar: Car): Car {
    return if (firstCar.maxSpeed > secondCar.maxSpeed) {
        Log.d("Race", "${firstCar.mark} ${firstCar.model} is winning the race")
        firstCar
    } else {
        Log.d("Race", "${secondCar.mark} ${secondCar.model} is winning the race")
        secondCar
    }
}

fun generatingCarsForRace(counter: Int): List<Car> {
    val cars = mutableListOf<Car>()
    val models = listOf("RAF4", "GRANTA", "M3", "M5", "Land Cruiser 200")
    for (i in 1..counter) {
        val car = CarBuilder("Mark$i")
            .setModel(models.random())
            .setYearOfRelease(1980 + i)
            .setMaxSpeed(100 + i * 2)
            .buildCar()
        cars.add(car)
    }
    return cars
}

fun game(cars: List<Car>): Car {
    var carsToRacing = cars.toMutableList()
    while (carsToRacing.size > 1) {
        val nextRound = mutableListOf<Car>()
        carsToRacing.shuffle()
        for (i in 0 until carsToRacing.size step 2) {
            if (i + 1 < carsToRacing.size) {
                val winner = raceCars(carsToRacing[i], carsToRacing[i + 1])
                Log.d("Race", "Race between ${carsToRacing[i].mark} and ${carsToRacing[i + 1].mark}, Winner: ${winner.mark}")
                nextRound.add(winner)
            } else {
                Log.d("Race", "${carsToRacing[i].mark} has no pair and moves to next round")
                nextRound.add(carsToRacing[i])
            }
        }
        carsToRacing = nextRound
    }
    return carsToRacing[0]
}
