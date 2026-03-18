import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import java.awt.print.Printable
import java.io.File

fun main() {
//    Create a lists or collections
//    val patients = mutableListOf<Patient>()
//    val doctors = mutableListOf<Doctor>()
//    val appointments = mutableListOf<Appointment>()

    println("DrMeetKotlin successfully initialised!")
    println("=====================================")

//    Load patients and doctors first
    val patients = File("patients.txt")
        .takeIf { it.exists() }
        ?.readLines()
        ?.map {
            val parts = it.split(",")
            Patient(
                id = parts[0].toInt(),
                firstname = parts[1],
                lastname = parts[2],
                email = parts[3],
                phoneNumber = parts[4]
            )
        }?.toMutableList() ?: mutableListOf()

    val doctors = File("doctors.txt")
        .takeIf { it.exists() }
        ?.readLines()
        ?.map {
            val parts = it.split(",")
            Doctor(
                id = parts[0].toInt(),
                firstName = parts[1],
                lastName = parts[2],
                specialty = parts[3]
            )
        }?.toMutableList() ?: mutableListOf()
    val appointments = File("appointments.txt").takeIf { it.exists() }?.readLines()?.map {
        val parts = it.split(",")
        Appointment(
            id = parts[0].toInt(),
            date = parts[1],           // keep as String
            patientId = parts[2].toInt(),
            doctorId = parts[3].toInt(),
            reason = parts[4]
        )
    }?.toMutableList() ?: mutableListOf()
//    Clear patients first
    //patients.clear()
//
////    Add patient data
//    patients.add(Patient(1, "James", lastname = "Degz", email = "jamesdegz@gmail.com", phoneNumber = "1234567890" ))
//    patients.add(Patient(2, firstname = "Emily", lastname="Smith", email = "emilysmith@hotmail.com", phoneNumber = "1234567891" ))
//    patients.add(Patient(3, firstname = "Monkey", lastname = "D. Loofy", email = "mdloofy@gmail.com", phoneNumber = "1234567892" ))
//
////    Add doctor data
//    doctors.add(Doctor(1, firstName = "Lynn", lastName = "Ramos", specialty = "cardiology"))
//    doctors.add(Doctor(2, firstName = "Mark", lastName = "Johnson", specialty = "dermatology"))
//    doctors.add(Doctor(3, firstName = "Chopper", lastName = "R", specialty = "radiology"))
//    doctors.add(Doctor(4, firstName = null, lastName = "Xavier", specialty = "mutant care"))
//
//
////    Create appointments
//    appointments.add(Appointment(1, 1, 1, date = "2026-03-12 10:00", reason = "Regular checkup"))
//    appointments.add(Appointment(2, 2, 2, date = "2026-03-15 10:00", reason = "Skin consultation"))
//
////    Show relationship
//    for (appointment in appointments) {
//        println("Appointment ${appointment.id} -> Patient ${appointment.patientId} with Doctor ${appointment.doctorId}")
//    }
//
////    Display patients
//    for (patient in patients) {
//        println("Patient ${patient.id} -> ${patient.firstname} ${patient.lastname}")
//
//    }
//
////    Display Doctors
//    for (doctor in doctors) {
//        println("Doctor ${doctor.id} -> ${doctor.firstName} ${doctor.lastName}")
//    }
//
////    Find a patient
//    val patient = patients.find { it.id == 4 }
//    if(patient != null) {
//        println("The patient is ${patient.firstname} ${patient.lastname}")
//    } else {
//        println("The patient is not found")
//    }
//
////    Find a doctor
//    val doctor = doctors.find { it.id == 4 }
//    doctor?.let {
//        println("The doctor is ${doctor.id} ${doctor.firstName} ${doctor.lastName}")
//    }
//
////    Print relationships in a nested way
//    for (patient in patients) {
//        println("${patient.firstname} has an appointment with ")
////        Find only patients with a doctor's appointment (i.e., not everyone in the patients list has an appointment with a doctor)
//        val patientAppointments = appointments.filter { appointment -> appointment.patientId == patient.id} // filter appointment for this patient only
////        Find the doctor who's got an appointment with the patient and display to the console
//        for (appointment in patientAppointments) {
//            val doctor = doctors.find { doctor -> doctor.id == appointment.doctorId }  // find the corresponding doctor
//            println(" - Doctor: ${doctor?.firstName} ${doctor?.lastName} with specialty ${doctor?.specialty} and reason of ${appointment.reason}")
//        }
//    }

//  Print menu for CRUD operations on DrMeet
    println("++++++++++++++++++")
    println("Please select from menu")
    println("[1] - NEW")
    println("[2] - SEARCH")
    println("[3] - UPDATE")
    println("[4] - REMOVE")
    println("[-1] - EXIT")

    println(">>: ")
    val userInput = readLine() ?: ""
    when (userInput) {
        "1" -> {
            println("[1] - ADD PATIENT")
            println("[2] - ADD DOCTOR")
            println("[3] - ADD APPOINTMENT")
            println("[-1] - EXIT")

            print(">>: ")
            val addInput = readLine() ?: ""

            when (addInput) {
                "1" -> addData(patients, doctors, appointments, type = "patient")
                "2" -> addData(patients, doctors, appointments, type = "doctor")
                "3" -> addData(patients, doctors, appointments, type = "appointment")

            }
        }
         "2" -> {
             println("[1] - SEARCH PATIENT")
             println("[2] - SEARCH DOCTOR")
             println("[3] - SEARCH APPOINTMENT")
             println("[-1] - EXIT")
         }
        "3" -> {
            println("[1] - UPDATE PATIENT")
            println("[2] - UPDATE DOCTOR")
            println("[3] - UPDATE APPOINTMENT")
            println("[-1] - EXIT")
        }
        "4" -> {
            println("[1] - REMOVE PATIENT")
            println("[2] - REMOVE DOCTOR")
            println("[3] - REMOVE APPOINTMENT")
            println("[-1] - EXIT")

            print(">>: ")
            val removeInput = readLine() ?: ""

            when (removeInput) {
                "1" -> deleteData(patients,"patient")
            }
        }
    }


    }

    fun addPatient(
        patients: MutableList<Patient>,
        id: Int,
        firstname: String,
        lastname: String?,
        email: String?,
        phoneNumber: String?
    ) {
        val patient = Patient(id, firstname = firstname, lastname = lastname, email = email, phoneNumber = phoneNumber)
        patients.add(patient)
        println("Patient ${patient.firstname} ${patient.lastname} successfully added!")

        // Save the patient data without overwriting
        File("patients.txt").appendText("${patient.id},${patient.firstname},${patient.lastname},${patient.email},${patient.phoneNumber}\n")

    }

    fun addDoctor(
        doctors: MutableList<Doctor>,
        id: Int,
        firstName: String,
        lastName: String,
        specialty: String,
    ) {
        val doctor = Doctor(id, firstName = firstName, lastName = lastName, specialty = specialty)
        doctors.add(doctor)
        println("Doctor ${doctor.firstName} ${doctor.lastName} successfully added!")

        // Save the doctor data without overwriting
        File("doctors.txt").appendText("${doctor.id},${doctor.firstName},${doctor.lastName},${doctor.specialty}\n")

    }

    fun addAppointment(
        appointments: MutableList<Appointment>,
        id: Int,
        patientId: Int,
        doctorId: Int,
        date: String,
        reason: String
    ) {
        val appointment = Appointment(id, patientId = patientId, doctorId = doctorId, date = date, reason = reason)
        appointments.add(appointment)
        println("Appointment ${appointment.id} -> Patient ${appointment.patientId} with Doctor ${appointment.doctorId} successfully added!")

        // Save the appointment data without overwriting
        File("appointments.txt").appendText("${appointment.id},${appointment.date},${appointment.patientId},${appointment.doctorId},${appointment.reason}\n")

    }

// Generic function which accepts a mutable list and a string
    fun addData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, type: String) {
        when(type) {
            "patient" -> {
                println("Enter patient first name:")
                val firstName = readLine() ?: ""
                println("Enter patient last name:")
                val lastname = readLine() ?: ""
                println("Enter patient email:")
                val email = readLine() ?: ""
                println("Enter patient phone:")
                val phoneNumber = readLine() ?: ""

//               Generate ID automatically
                val id = (patients.maxOfOrNull { it.id } ?: 0) + 1
                addPatient(patients, id, firstName, lastname, email, phoneNumber)
            }
            "doctor" -> {
                println("Enter doctor first name:")
                val firstName = readLine() ?: ""
                println("Enter doctor last name:")
                val lastname = readLine() ?: ""
                println("Enter doctor specialty:")
                val specialty = readLine() ?: ""

//               Generate ID automatically
                val id = (doctors.maxOfOrNull { it.id } ?: 0) + 1
                addDoctor(doctors, id, firstName, lastname, specialty)
            }
            "appointment" -> {
                println("Enter patient ID:")
                val patientID = readLine()?.toIntOrNull() ?: 0
                println("Enter doctor ID:")
                val doctorID = readLine()?.toIntOrNull() ?: 0
                println("Enter appointment date (YYYY-MM-DD HH:MM):")
                val appointmentDate = readLine() ?: ""
                println("Enter reason:")
                val reason = readLine() ?: "none"

//               Generate ID automatically
                val id = (appointments.maxOfOrNull { it.id } ?: 0) + 1
                addAppointment(appointments, id, patientID, doctorID, appointmentDate, reason)
            }
        }
    }

    fun deletePatient(patients: MutableList<Patient>, id: Int) {
        val removed = patients.removeIf { it.id == id }
        if(removed) {
            println("Patient with ID $id successfully deleted!")
        }
        else {
            println("Patient with ID $id not found!")
        }
    }

    fun deleteData(patients: MutableList<Patient>, deleteItemName: String) {
        when (deleteItemName) {
            "patient" -> {
                println("Enter the ID of the patient to delete:")
                val itemToDelete = readLine() ?: ""
                val id = itemToDelete.toIntOrNull() ?: 0
                if(id != null) {
                    deletePatient(patients, id)
                } else {
                    println("Patient with ID $id not found!")
                }

            }
            "doctor" -> {
                 println("Enter the doctor ID to delete:")
            }
            "appointment" -> {
                println("Enter the appointment ID to delete:")
            }
        }
    }




