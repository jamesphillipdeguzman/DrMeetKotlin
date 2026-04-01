package com.example

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess
import java.sql.Timestamp

fun main() {
    //    Create a lists or collections
    //    val patients = mutableListOf<Patient>()
    //    val doctors = mutableListOf<Doctor>()
    //    val appointments = mutableListOf<Appointment>()

//    println("DB_USER: ${System.getenv("DB_USER")}")
//    println("DB_PASSWORD: ${System.getenv("DB_PASSWORD")}")
//    println("DB_HOST: ${System.getenv("DB_HOST")}")
//    println("DB_PORT: ${System.getenv("DB_PORT")}")
//    println("DB_NAME: ${System.getenv("DB_NAME")}")

// Connect to Aiven cloud database first!
    try {
        val conn = Database.connect()
        println("✅ Connected to Aiven MySQL!")

        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT VERSION()")

        if (rs.next()) {
            println("MySQL Version: ${rs.getString(1)}")
        }

        conn.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }




    //    Load patients, doctors, and appointments first
    val patients = File("patients.txt")
        .takeIf { it.exists() }
        ?.readLines()
        ?.map {
            val parts = it.split(",")
            Patient(
                id = parts[0].toInt(),
                firstname = parts[1],
                lastname = parts[2],
                dateOfBirth = parts[3],
                email = parts[4],
                phoneNumber = parts[5]
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
                specialty = parts[3],
                email = parts[4],
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
            "1" -> { // NEW
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
            "2" -> { // SHOW
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
                    "1" -> showPatientsDB() // showData(patients, doctors, appointments, type = "patient")
                    "2" -> showDoctorsDB() // showData(patients, doctors, appointments, type = "doctor")
                    "3" -> showAppointmentsDB() // showData(patients, doctors, appointments, type = "appointment")

                }
            }
            "3" -> { // UPDATE
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
            "4" -> { // REMOVE
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

            "-1" -> { // BACK/EXIT
                println("Exiting DrMeetKotlin...Bye!")
                exitProcess(-1)
                running = false
            }
            else -> println("Invalid input! Please enter (1-4) or -1 to exit...")
        }
    }



}


// ============================================================
// ===== SAVE PATIENTS, DOCTORS, AND APPOINTMENTS TO TEXT FILES AS BACKUP OR CACHED DATA...
// ============================================================


fun savePatients(patients: List<Patient>) {
    File("patients.txt").writeText(
        patients.joinToString("\n") {
            "${it.id},${it.firstname.trim()},${it.lastname?.trim()},${it.dateOfBirth?.trim()},${it.email?.trim()},${it.phoneNumber?.trim()}"
        }
    )
}

fun saveDoctors(doctors: List<Doctor>) {
    File("doctors.txt").writeText(
        doctors.joinToString("\n") {
            "${it.id},${it.firstName?.trim()},${it.lastName.trim()},${it.specialty.trim()},${it.email.trim()}"
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
    dateOfBirth: String?,
    email: String,
    phoneNumber: String?
) {
    val patient = Patient(id, firstname = firstname, lastname = lastname, dateOfBirth = dateOfBirth, email = email, phoneNumber = phoneNumber)

    // Add a new patient
    patients.add(patient)
    // Save data
    savePatients(patients)
    // Display success message to console
    println("Patient ${patient.firstname} ${patient.lastname} successfully added!")

}


fun addPatientDB(firstname: String, lastname: String, dateOfBirth: String, email: String, phoneNumber: String) {
    try {
        val conn = Database.connect()

        val query = """
            INSERT INTO Patients (firstname, lastname, dateOfBirth, email, phoneNumber)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setString(1, firstname)
        stmt.setString(2, lastname)
        stmt.setString(3, dateOfBirth)
        stmt.setString(4, email)
        stmt.setString(5, phoneNumber)

        stmt.executeUpdate()
        println("Patient added to DB!")
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        println("Error adding patient to DB!")
        e.printStackTrace()
    }

}

fun addDoctor(
    doctors: MutableList<Doctor>,
    id: Int,
    firstName: String,
    lastName: String,
    specialty: String,
    email: String
) {
    val doctor = Doctor(id, firstName = firstName, lastName = lastName, specialty = specialty, email=email)

    // Add a new patient
    doctors.add(doctor)
    // Save data
    saveDoctors(doctors)
    // Display success message to console
    println("Doctor ${doctor.firstName} ${doctor.lastName} successfully added!")

}

fun addDoctorDB(firstname: String, lastname: String, specialty: String, email: String) {
    try {
        val conn = Database.connect()

        val query = """
            INSERT INTO Doctors (firstname, lastname, specialty, email)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setString(1, firstname)
        stmt.setString(2, lastname)
        stmt.setString(3, specialty)
        stmt.setString(4, email)

        stmt.executeUpdate()
        println("Doctor added to DB!")
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        println("Error adding doctor to DB!")
        e.printStackTrace()
    }

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

fun addAppointmentDB(patientId: Int, doctorId: Int, appointmentDateTime: String, reason: String) {
    try {
        val conn = Database.connect()

        val query = """
            INSERT INTO Appointments (patientId, doctorId, appointmentDateTime, reason)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setInt(1, patientId)
        stmt.setInt(2, doctorId)
        stmt.setString(3, appointmentDateTime)
        stmt.setString(4, reason)

        stmt.executeUpdate()
        println("Appointment added to DB!")
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        println("Error adding appointment to DB!")
        e.printStackTrace()
    }

}

// Generic function which accepts a mutable list and a string
fun addData(patients: MutableList<Patient>, doctors: MutableList<Doctor>, appointments: MutableList<Appointment>, type: String) {
    when(type) {
        "patient" -> {
            println("Enter patient first name:")
            val firstname = (readLine() ?: "").trim().toTitleCase()
            println("Enter patient last name:")
            val lastname = (readLine() ?: "").trim().toTitleCase()
            println("Enter patient date of birth [yyyy-MM-dd]:")
            val dateOfBirth = (readLine() ?: "").trim().toTitleCase()
            println("Enter patient email:")
            val email = (readLine() ?: "").trim().lowercase()
            println("Enter patient phone:")
            val phoneNumber = readLine() ?: ""

//               Generate ID automatically
            val id = (patients.maxOfOrNull { it.id } ?: 0) + 1
            addPatient(patients, id, firstname, lastname, dateOfBirth, email, phoneNumber) // Save data to text file
            addPatientDB(firstname, lastname ?: "", dateOfBirth ?: "", email ?: "", phoneNumber ?: "") // Save data to cloud
        }
        "doctor" -> {
            println("Enter doctor first name:")
            val firstname = readLine() ?: ""
            println("Enter doctor last name:")
            val lastname = readLine() ?: ""
            println("Enter doctor specialty:")
            val specialty = readLine() ?: ""
            println("Enter doctor email:")
            val email = readLine() ?: ""

//               Generate ID automatically
            val id = (doctors.maxOfOrNull { it.id } ?: 0) + 1
            addDoctor(doctors, id, firstname, lastname, specialty, email) // Save data to text file
            addDoctorDB(firstname, lastname, specialty, email) // Save data to cloud
        }
        "appointment" -> {
            println("Enter patient ID:")
            val patientID = readLine()?.toIntOrNull() ?: 0
            println("Enter doctor ID:")
            val doctorID = readLine()?.toIntOrNull() ?: 0
            println("Enter appointment date and time (YYYY-MM-DD HH:MM):")
            val appointmentDateTime = readLine() ?: ""
            println("Enter reason:")
            val reason = readLine() ?: "none"

//               Generate ID automatically
            val id = (appointments.maxOfOrNull { it.id } ?: 0) + 1
            addAppointment(appointments, id, patientID, doctorID, appointmentDateTime, reason) // Save data to text file
            addAppointmentDB(patientID, doctorID, appointmentDateTime, reason) // Save data to cloud
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

fun showPatientsDB() {
    try {
        val conn = Database.connect()
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM Patients")

        while (rs.next()) {
            println("Patient ${rs.getInt("PatientID")} -> ${rs.getString("Firstname")} ${rs.getString("Lastname")}")
        }
        rs.close()
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

fun showDoctorsDB() {
    try {
        val conn = Database.connect()
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM Doctors")

        while (rs.next()) {
            println("Doctor ${rs.getInt("DoctorID")} -> ${rs.getString("Firstname")} ${rs.getString("Lastname")} -> ${rs.getString("Specialty")}")
        }
        rs.close()
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

fun showAppointmentsDB() {
    try {
        val conn = Database.connect()
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("""
            SELECT p.PatientID AS PatientID, p.Firstname AS PatientFirst, p.Lastname AS PatientLast , 
            d.DoctorID AS DoctorID, d.Firstname AS DoctorFirst, d.Lastname AS DoctorLast, d.Specialty AS Specialty, a.AppointmentDateTime AS AppointmentDate, a.Reason AS Reason 
            FROM Appointments a
            JOIN Patients p ON a.PatientID = p.PatientID
            JOIN Doctors d ON a.DoctorID = d.DoctorID
        """.trimIndent())

        while (rs.next()) {
            println("Patient ${rs.getInt("PatientID")} -> ${rs.getString("PatientFirst")} ${rs.getString("PatientLast")} has appointment with " +
                    "doctor ${rs.getInt("DoctorID")} -> ${rs.getString("DoctorFirst")} ${rs.getString("DoctorLast")} (${rs.getString("Specialty")}) on ${rs.getString("AppointmentDate")} " +
                    "for ${rs.getString("Reason")}.")
        }
        rs.close()
        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
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
    if(firstname == "-1") return // go back immediately
    if (!firstname.isNullOrBlank()) patient.firstname = firstname

    println("Last name: [${patient.lastname}]")
    val lastname = readLine()?.trim()?.toTitleCase()
    if(lastname == "-1") return // go back immediately
    if (!lastname.isNullOrBlank()) patient.lastname = lastname

    println("Email: [${patient.email}]")
    val email = readLine()?.trim()?.lowercase()
    if(email == "-1") return // go back immediately
    if (!email.isNullOrBlank()) patient.email = email

    println("Phone: [${patient.phoneNumber}]")
    val phone = readLine()?.trim()
    if(phone == "-1") return // go back immediately
    if (!phone.isNullOrBlank()) patient.phoneNumber = phone

    // Save changes to text file
    savePatients(patients)
    println("Patient with id ${patient.id} successfully updated!")

    // Save changes to the cloud
    updatePatientDB(
        PatientID = patient.id,
        Firstname = patient.firstname,
        Lastname = patient.lastname,
        DateOfBirth = patient.dateOfBirth,
        Email = patient.email,
        PhoneNumber = patient.phoneNumber,
    )

    println("Patient with ${patient.id} successfully updated!")

}

fun updatePatientDB(
    PatientID: Int,
    Firstname: String?,
    Lastname: String?,
    DateOfBirth: String?,
    Email: String,
    PhoneNumber: String?
) {
    try {
        val conn = Database.connect()
        val query = """
            UPDATE Patients
            SET Firstname = ?, Lastname = ?, DateOfBirth = ?, Email = ?, PhoneNumber = ?
            WHERE PatientID = ?
            """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setString(1, Firstname ?: "")
        stmt.setString(2, Lastname ?: "")
        stmt.setString(3, DateOfBirth ?: "")
        stmt.setString(4, Email)
        stmt.setString(5, PhoneNumber ?: "")
        stmt.setInt(6, PatientID ?: 0)

        val rowsAffected = stmt.executeUpdate()
        println("$rowsAffected patient(s) updated in DB.")

        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

fun updateDoctor(doctors: MutableList<Doctor>) {
    println("Enter the id of the doctor to update: ")
    val id = readLine()?.toIntOrNull() ?: run {
        println("Invalid id!")
        return
    }

    // Sanity check: is the doctor in the database?
    val doctor = doctors.find { it.id == id }

    if(doctor == null) {
        println("The doctor with id $id not found!")
        return
    }

    println("Please leave blank to keep current value.")

    println("First name: [${doctor.firstName}]")
    val firstName = readLine()?.trim()?.toTitleCase()
    if(firstName == "-1") return // go back immediately
    if (!firstName.isNullOrBlank()) doctor.firstName = firstName

    println("Last name: [${doctor.lastName}]")
    val lastName = readLine()?.trim()?.toTitleCase()
    if(lastName == "-1") return // go back immediately
    if (!lastName.isNullOrBlank()) doctor.lastName = lastName

    println("Specialty: [${doctor.specialty}]")
    val specialty = readLine()?.trim()?.toTitleCase()
    if(specialty == "-1") return // go back immediately
    if (!specialty.isNullOrBlank()) doctor.specialty = specialty

    println("Email: [${doctor.email}]")
    val email = readLine()?.trim()
    if(email == "-1") return // go back immediately
    if (!email.isNullOrBlank()) doctor.email = email

    // Save changes to text file
    saveDoctors(doctors)

    // Save changes to the cloud
    updateDoctorDB(
        DoctorID = doctor.id,
        Firstname = doctor.firstName,
        Lastname = doctor.lastName,
        Specialty = doctor.specialty,
        Email = doctor.email
        )

    println("Doctor with id ${doctor.id} successfully updated!")
}

fun updateDoctorDB(
    DoctorID: Int,
    Firstname: String?,
    Lastname: String?,
    Specialty: String?,
    Email: String,

) {
    try {
        val conn = Database.connect()
        val query = """
            UPDATE Doctors
            SET Firstname = ?, Lastname = ?, Specialty = ?, Email = ?
            WHERE DoctorID = ?
            """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setString(1, Firstname ?: "")
        stmt.setString(2, Lastname ?: "")
        stmt.setString(3, Specialty ?: "")
        stmt.setString(4, Email)
        stmt.setInt(5, DoctorID ?: 0)

        val rowsAffected = stmt.executeUpdate()
        println("$rowsAffected doctor(s) updated in DB.")

        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

fun updateAppointment(appointments: MutableList<Appointment>) {
    println("Enter the id of the appointment to update: ")
    val id = readLine()?.toIntOrNull() ?: run {
        println("Invalid id!")
        return
    }

    // Sanity check: is the appointment in the database?
    val appointment = appointments.find { it.id == id }

    if(appointment == null) {
        println("The appointment with id $id not found!")
        return
    }

    println("Please leave blank to keep current value.")

    println("Patient ID: [${appointment.patientId}]")
    val patientId = readLine()?.toIntOrNull()
    if(patientId == -1) return // go back immediately
    if (patientId != null) appointment.patientId = patientId

    println("Doctor ID: [${appointment.doctorId}]")
    val doctorId = readLine()?.toIntOrNull()
    if(doctorId == -1) return // go back immediately
    if (doctorId != null) appointment.doctorId = doctorId

    println("Date: [${appointment.date}]")
    val date = readLine()?.trim()
    if(date == "-1") return // go back immediately
    if (!date.isNullOrBlank()) appointment.date = date

    println("Reason: [${appointment.reason}]")
    val reason = readLine()?.trim()
    if(reason == "-1") return // go back immediately
    if (!reason.isNullOrBlank()) appointment.reason = reason

    // Save changes to text file
    saveAppointments(appointments)

    // Save change to the cloud
    updateAppointmentDB(
        AppointmentID = appointment.id,
        PatientID = appointment.patientId,
        DoctorID = appointment.doctorId,
        AppointmentDateTime = appointment.date,
        Reason = appointment.reason,
    )

    println("Appointment with id ${appointment.id} successfully updated!")
}

fun updateAppointmentDB(
    AppointmentID: Int,
    PatientID: Int?,
    DoctorID: Int?,
    AppointmentDateTime: String,
    Reason: String,

    ) {
    try {
        val conn = Database.connect()
        val query = """
            UPDATE Appointments
            SET PatientID = ?, DoctorID = ?, AppointmentDateTime = ?, Reason = ?
            WHERE AppointmentID = ?
            """.trimIndent()

        val stmt = conn.prepareStatement(query)
        stmt.setInt(1, PatientID ?: 0)
        stmt.setInt(2, DoctorID ?: 0)

        // Proper datetime handling
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(AppointmentDateTime, formatter)
        val timestamp = Timestamp.valueOf(dateTime)

        stmt.setTimestamp(3, timestamp ?: Timestamp.valueOf(LocalDateTime.now()))
        stmt.setString(4, Reason ?: "not stated")
        stmt.setInt(5, AppointmentID ?: 0)

        val rowsAffected = stmt.executeUpdate()
        println("$rowsAffected appointment(s) updated in DB.")

        stmt.close()
        conn.close()
    } catch(e: Exception) {
        e.printStackTrace()
    }
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
    val exists = patients.any { it.id == id } // check if patient exists first
    if(!exists) {
        println("Patient with id $id not found!")
        return
    }

    // Try DB delete first
    val success = deletePatientDB(id)

    if(success) {
        // Only update local if DB succeeds
        patients.removeIf { it.id == id }
        // Save changes to text file
        savePatients(patients)

        println("Patient with ID $id successfully deleted!")
    } else {
        println("Delete failed. Patient may have existing appointments.")
    }


}

fun deletePatientDB(PatientID: Int): Boolean {

    return try {
        val conn = Database.connect()

        // Check if patient has appointments
        val checkStmt = conn.prepareStatement(
            "SELECT COUNT(*) FROM Appointments WHERE PatientID = ?"
        )

        checkStmt.setInt(1, PatientID)
        val rs = checkStmt.executeQuery()
        rs.next()

        val count = rs.getInt(1)

        if (count > 0) {
            println("Cannot delete: Patient has $count appointment(s)")
            conn.close()
            return false
        }

        // Safe to delete

        val stmt = conn.prepareStatement(
            "DELETE FROM Patients WHERE PatientID = ?"
        )

        stmt.setInt(1, PatientID)

        val rows = stmt.executeUpdate()

        stmt.close()
        conn.close()

        rows > 0
    } catch(e: Exception) {
        e.printStackTrace()
        false
    }
}


fun deleteDoctor(doctors: MutableList<Doctor>, id: Int) {
    val exists = doctors.any { it.id == id } // check if doctor exists first
    if(!exists) {
        println("Doctor with id $id not found!")
        return
    }

    // Try DB delete first
    val success = deleteDoctorDB(id)

    if(success) {
        // Only update local if DB succeeds
        doctors.removeIf { it.id == id }
        // Save changes to text file
        saveDoctors(doctors)

        println("Doctor with ID $id successfully deleted!")
    } else {
        println("Delete failed. Doctor may have existing appointments.")
    }


}

fun deleteDoctorDB(DoctorID: Int): Boolean {

    return try {
        val conn = Database.connect()

        // Check if patient has appointments
        val checkStmt = conn.prepareStatement(
            "SELECT COUNT(*) FROM Appointments WHERE DoctorID = ?"
        )

        checkStmt.setInt(1, DoctorID)
        val rs = checkStmt.executeQuery()
        rs.next()

        val count = rs.getInt(1)

        if (count > 0) {
            println("Cannot delete: Doctor has $count appointment(s)")
            conn.close()
            return false
        }

        // Safe to delete

        val stmt = conn.prepareStatement(
            "DELETE FROM Doctors WHERE DoctorID = ?"
        )

        stmt.setInt(1, DoctorID)

        val rows = stmt.executeUpdate()

        stmt.close()
        conn.close()

        rows > 0
    } catch(e: Exception) {
        e.printStackTrace()
        false
    }
}

fun deleteAppointment(appointments: MutableList<Appointment>, id: Int) {
    val exists = appointments.any { it.id == id } // remove a doctor by ID
    if(exists) {
        println("Appointment with id $id not found!")
        return
    }

    // Try DB delete first
    val success = deleteAppointmentDB(id)

    if(success) {
        // Only update local if DB succeeds
        appointments.removeIf { it.id == id }
        // Save changes to text file
        saveAppointments(appointments)

        println("Appointment with ID $id successfully deleted!")
    } else {
        println("Delete failed. Appointment may not exist yet.")
    }

}

fun deleteAppointmentDB(AppointmentID: Int): Boolean {

    return try {
        Database.connect().use { conn ->
            conn.prepareStatement("DELETE FROM Appointments WHERE AppointmentID = ?").use { stmt ->
            stmt.setInt(1, AppointmentID)
            stmt.executeUpdate() > 0
        }
    }
    } catch(e: Exception) {
        e.printStackTrace()
        false
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
                println("Appointment with ID $id not found!")
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


