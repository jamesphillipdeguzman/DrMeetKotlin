package com.example
import java.io.File
import kotlin.system.exitProcess

fun main() {
    //    Create a lists or collections
    //    val patients = mutableListOf<Patient>()
    //    val doctors = mutableListOf<Doctor>()
    //    val appointments = mutableListOf<Appointment>()



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

    println("DrMeetKotlin successfully initialised!")
    println("=====================================")

    var running = true

    while (running) {
        //  Print menu for CRUD operations on DrMeet
        println("++++++++++++++++++")
        println("Please select from menu")
        println("[1] - NEW")
        println("[2] - SHOW")
        println("[3] - UPDATE")
        println("[4] - REMOVE")
        println("[-1] - BACK/EXIT")

        print(">>: ")
        val userInput = readLine() ?: ""
        if (userInput.isBlank()) {
            println("No input detected. Exiting...")
            return
        }
        when (userInput) {
            "1" -> {
                println("[1] - PATIENT")
                println("[2] - DOCTOR")
                println("[3] - APPOINTMENT")
                println("[-1] - BACK/EXIT")

                print(">>: ")
                val userInput = readLine() ?: ""

                when (userInput) {
                    "1" -> addData(patients, doctors, appointments, type = "patient")
                    "2" -> addData(patients, doctors, appointments, type = "doctor")
                    "3" -> addData(patients, doctors, appointments, type = "appointment")

                }
            }
            "2" -> {
                println("[1] - PATIENTS")
                println("[2] - DOCTORS")
                println("[3] - APPOINTMENTS")
                println("[-1] - BACK/EXIT")

                print(">>: ")
                val userInput = readLine() ?: ""
                if (userInput.isBlank()) {
                    println("No input detected. Exiting...")
                    return
                }

                when (userInput) {
                    "1" -> showData(patients, doctors, appointments, type = "patient")
                    "2" -> showData(patients, doctors, appointments, type = "doctor")
                    "3" -> showData(patients, doctors, appointments, type = "appointment")

                }
            }
            "3" -> {
                println("[1] - PATIENT")
                println("[2] - DOCTOR")
                println("[3] - APPOINTMENT")
                println("[-1] - BACK/EXIT")

                print(">>: ")
                val userInput = readLine() ?: ""
                if (userInput.isBlank()) {
                    println("No input detected. Exiting...")
                    return
                }

                when (userInput) {
                    "1" -> updateData(patients, doctors, appointments, "patient")
                    "2" -> updateData(patients, doctors, appointments, "doctor")
                    "3" -> updateData(patients, doctors, appointments, "appointment")
                }
            }
            "4" -> {
                println("[1] - PATIENT")
                println("[2] - DOCTOR")
                println("[3] - APPOINTMENT")
                println("[-1] - BACK/EXIT")

                print(">>: ")
                val userInput = readLine() ?: ""
                if (userInput.isBlank()) {
                    println("No input detected. Exiting...")
                    return
                }

                when (userInput) {
                    "1" -> deleteData(patients, doctors, appointments, "patient")
                    "2" -> deleteData(patients, doctors, appointments, "doctor")
                    "3" -> deleteData(patients, doctors, appointments, "appointment")
                }
            }

            "-1" -> {
                println("Exiting DrMeetKotlin...Bye!")
                exitProcess(-1)
                running = false
            }
            else -> println("Invalid input! Please enter (1-4) or -1 to exit...")
        }
    }



}


// ============================================================
// ===== SAVE PATIENTS, DOCTORS, AND APPOINTMENTS
// ============================================================


fun savePatients(patients: List<Patient>) {
    File("patients.txt").writeText(
        patients.joinToString("\n") {
            "${it.id},${it.firstname.trim()},${it.lastname?.trim()},${it.email?.trim()},${it.phoneNumber?.trim()}"
        }
    )
}

fun saveDoctors(doctors: List<Doctor>) {
    File("doctors.txt").writeText(
        doctors.joinToString("\n") {
            "${it.id},${it.firstName?.trim()},${it.lastName.trim()},${it.specialty.trim()}"
        }
    )
}

fun saveAppointments(appointments: List<Appointment>) {
    File("appointments.txt").writeText(
        appointments.joinToString("\n") {
            "${it.id},${it.date.trim()},${it.patientId},${it.doctorId},${it.reason.trim()}"
        }
    )
}


// ============================================================
// ===== [1] - NEW: ADD NEW DATA FOR PATIENTS, DOCTORS, AND APPOINTMENTS
// ============================================================

fun addPatient(
    patients: MutableList<Patient>,
    id: Int,
    firstname: String,
    lastname: String?,
    email: String?,
    phoneNumber: String?
) {
    val patient = Patient(id, firstname = firstname, lastname = lastname, email = email, phoneNumber = phoneNumber)

    // Add a new patient
    patients.add(patient)
    // Save data
    savePatients(patients)
    // Display success message to console
    println("Patient ${patient.firstname} ${patient.lastname} successfully added!")

}

fun addDoctor(
    doctors: MutableList<Doctor>,
    id: Int,
    firstName: String,
    lastName: String,
    specialty: String
) {
    val doctor = Doctor(id, firstName = firstName, lastName = lastName, specialty = specialty)

    // Add a new patient
    doctors.add(doctor)
    // Save data
    saveDoctors(doctors)
    // Display success message to console
    println("Doctor ${doctor.firstName} ${doctor.lastName} successfully added!")

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

    // Add a new patient
    appointments.add(appointment)
    // Save data
    saveAppointments(appointments)
    // Display success message to console
    println("Appointment ${appointment.id} -> Patient ${appointment.patientId} with Doctor ${appointment.doctorId} successfully added!")

}

// Generic function which accepts a mutable list and a string
fun addData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, type: String) {
    when(type) {
        "patient" -> {
            println("Enter patient first name:")
            val firstName = (readLine() ?: "").trim().toTitleCase()
            println("Enter patient last name:")
            val lastname = (readLine() ?: "").trim().toTitleCase()
            println("Enter patient email:")
            val email = (readLine() ?: "").trim().lowercase()
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


// ============================================================
// ===== [2] - SHOW: DISPLAY DATA FOR PATIENTS, DOCTORS, AND APPOINTMENTS
// ============================================================


// Generic function which accepts a mutable list and a string
fun showData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, type: String) {
    when(type) {
        "patient" -> {
            for (patient in patients) {
                println("Patient ${patient.id} -> ${patient.firstname} ${patient.lastname}")
            }

        }
        "doctor" -> {
            for (doctor in doctors) {
                println("Doctor ${doctor.id} -> ${doctor.firstName} ${doctor.lastName}")
            }
        }
        "appointment" -> {
            for (patient in patients) {
                println("${patient.firstname} has an appointment with ")
                //        Find only patients with a doctor's appointment (i.e., not everyone in the patients list has an appointment with a doctor)
                val patientAppointments =
                    appointments.filter { appointment -> appointment.patientId == patient.id } // filter appointment for this patient only
                //        Find the doctor who's got an appointment with the patient and display to the console
                for (appointment in patientAppointments) {
                    val doctor =
                        doctors.find { doctor -> doctor.id == appointment.doctorId }  // find the corresponding doctor
                    println(" - Doctor: ${doctor?.firstName} ${doctor?.lastName} with specialty ${doctor?.specialty} and reason of ${appointment.reason}")
                }
            }
        }
    }
}


// ============================================================
// ===== [3] - UPDATE: MODIFY DATA FOR PATIENTS, DOCTORS, AND APPOINTMENTS
// ============================================================


fun updatePatient(patients: MutableList<Patient>) {
    println("Enter the id of the patient to update: ")
    val id = readLine()?.toIntOrNull() ?: run {
        println("Invalid id!")
        return
    }

    // Sanity check: is the patient in the database?
    val patient = patients.find { it.id == id }

    if(patient == null) {
        println("The patient with id $id not found!")
        return
    }

    println("Please leave blank to keep current value.")

    println("First name: [${patient.firstname}]")
    val firstname = readLine()?.trim()?.toTitleCase()
    if (!firstname.isNullOrBlank()) patient.firstname = firstname

    println("Last name: [${patient.lastname}]")
    val lastname = readLine()?.trim()?.toTitleCase()
    if (!lastname.isNullOrBlank()) patient.lastname = lastname

    println("Email: [${patient.email}]")
    val email = readLine()?.trim()?.lowercase()
    if (!email.isNullOrBlank()) patient.email = email

    println("Phone: [${patient.phoneNumber}]")
    val phone = readLine()?.trim()
    if (!phone.isNullOrBlank()) patient.phoneNumber = phone

    // Save changes
    savePatients(patients)
    println("Patient with id ${patient.id} successfully updated!")
}

fun updateDoctor(doctors: MutableList<Doctor>) {
    println("Enter the id of the doctor to update: ")
    val id = readLine()?.toIntOrNull() ?: run {
        println("Invalid id!")
        return
    }

    // Sanity check: is the patient in the database?
    val doctor = doctors.find { it.id == id }

    if(doctor == null) {
        println("The doctor with id $id not found!")
        return
    }

    println("Please leave blank to keep current value.")

    println("First name: [${doctor.firstName}]")
    val firstName = readLine()?.trim()?.toTitleCase()
    if (!firstName.isNullOrBlank()) doctor.firstName = firstName

    println("Last name: [${doctor.lastName}]")
    val lastName = readLine()?.trim()?.toTitleCase()
    if (!lastName.isNullOrBlank()) doctor.lastName = lastName

    println("Specialty: [${doctor.specialty}]")
    val specialty = readLine()?.trim()?.toTitleCase()
    if (!specialty.isNullOrBlank()) doctor.specialty = specialty

    // Save changes
    saveDoctors(doctors)
    println("Doctor with id ${doctor.id} successfully updated!")
}

fun updateAppointment(appointments: MutableList<Appointment>) {
    println("Enter the id of the appointment to update: ")
    val id = readLine()?.toIntOrNull() ?: run {
        println("Invalid id!")
        return
    }

    // Sanity check: is the patient in the database?
    val appointment = appointments.find { it.id == id }

    if(appointment == null) {
        println("The appointment with id $id not found!")
        return
    }

    println("Please leave blank to keep current value.")

    println("Patient ID: [${appointment.patientId}]")
    val patientId = readLine()?.toIntOrNull()
    if (patientId != null) appointment.patientId = patientId

    println("Doctor ID: [${appointment.doctorId}]")
    val doctorId = readLine()?.toIntOrNull()
    if (doctorId != null) appointment.doctorId = doctorId

    println("Date: [${appointment.date}]")
    val date = readLine()?.trim()
    if (!date.isNullOrBlank()) appointment.date = date

    println("Reason: [${appointment.reason}]")
    val reason = readLine()?.trim()
    if (!reason.isNullOrBlank()) appointment.reason = reason

    // Save changes
    saveAppointments(appointments)
    println("Appointment with id ${appointment.id} successfully updated!")
}

fun updateData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, type: String) {
    when(type) {
        "patient" -> updatePatient(patients)
        "doctor" -> updateDoctor(doctors)
        "appointment" -> updateAppointment(appointments)
    }
}


// ============================================================
// ===== [4] - REMOVE: DELETE DATA FOR PATIENTS, DOCTORS, AND APPOINTMENTS
// ============================================================

fun deletePatient(patients: MutableList<Patient>, id: Int) {
    val removed = patients.removeIf { it.id == id } // remove a patient by ID
    if(removed) {

        savePatients(patients)
        println("Patient with ID $id successfully deleted!")

    }
    else {
        println("Patient with ID $id not found!")
    }
}

fun deleteDoctor(doctors: MutableList<Doctor>, id: Int) {
    val removed = doctors.removeIf { it.id == id } // remove a doctor by ID
    if(removed) {

        saveDoctors(doctors)
        println("Doctor with ID $id successfully deleted!")

    }
    else {
        println("Doctor with ID $id not found!")
    }
}

fun deleteAppointment(appointments: MutableList<Appointment>, id: Int) {
    val removed = appointments.removeIf { it.id == id } // remove a doctor by ID
    if(removed) {

        saveAppointments(appointments)
        println("Appointment with ID $id successfully deleted!")

    }
    else {
        println("Appointment with ID $id not found!")
    }
}


fun deleteData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, deleteItemName: String) {
    when (deleteItemName) {
        "patient" -> {
            println("Enter the ID of the patient to delete:")
            val itemToDelete = readLine() ?: ""
            print(">>:")
            val id = itemToDelete.toIntOrNull() ?: 0
            if(id != null) {
                deletePatient(patients, id)
            } else {
                println("Patient with ID $id not found!")
            }

        }
        "doctor" -> {
            println("Enter the doctor ID to delete:")
            val itemToDelete = readLine() ?: ""
            print(">>:")
            val id = itemToDelete.toIntOrNull() ?: 0
            if(id != null) {
                deleteDoctor(doctors, id)
            } else {
                println("Doctor with ID $id not found!")
            }
        }
        "appointment" -> {
            println("Enter the appointment ID to delete:")
            val itemToDelete = readLine() ?: ""
            print(">>:")
            val id = itemToDelete.toIntOrNull() ?: 0
            if(id != null) {
                deleteAppointment(appointments, id)
            } else {
                println("Doctor with ID $id not found!")
            }
        }
    }
}




// Extension function to convert a string to Title Case (works in older Kotlin versions)
fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { word ->
        if (word.isEmpty()) {
            word
        } else {
            word.substring(0, 1).uppercase() + word.substring(1).lowercase()
        }
    }
}