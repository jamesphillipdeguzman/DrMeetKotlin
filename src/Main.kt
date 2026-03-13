fun main() {
//    Create a lists or collections
    val patients = mutableListOf<Patient>()
    val doctors = mutableListOf<Doctor>()
    val appointments = mutableListOf<Appointment>()

    println("DrMeetKotlin successfully initialised!")
    println("=====================================")

//    Add patient data
    patients.add(Patient(1, "James", lastname = "Degz", email = "jamesdegz@gmail.com", phoneNumber = "1234567890" ))
    patients.add(Patient(2, firstname = "Emily", lastname="Smith", email = "emilysmith@hotmail.com", phoneNumber = "1234567891" ))
    patients.add(Patient(3, firstname = "Monkey", lastname = "D. Loofy", email = "mdloofy@gmail.com", phoneNumber = "1234567892" ))

//    Add doctor data
    doctors.add(Doctor(1, firstName = "Lynn", lastName = "Ramos", specialty = "cardiology"))
    doctors.add(Doctor(2, firstName = "Mark", lastName = "Johnson", specialty = "dermatology"))
    doctors.add(Doctor(3, firstName = "Chopper", lastName = "R", specialty = "radiology"))
    doctors.add(Doctor(4, firstName = null, lastName = "Xavier", specialty = "mutant care"))


//    Create appointments
    appointments.add(Appointment(1, 1, 1, date = "2026-03-12 10:00", reason = "Regular checkup"))
    appointments.add(Appointment(2, 2, 2, date = "2026-03-15 10:00", reason = "Skin consultation"))

//    Show relationship
    for (appointment in appointments) {
        println("Appointment ${appointment.id} -> Patient ${appointment.patientId} with Doctor ${appointment.doctorId}")
    }

//    Display patients
    for (patient in patients) {
        println("Patient ${patient.id} -> ${patient.firstname} ${patient.lastname}")

    }

//    Display Doctors
    for (doctor in doctors) {
        println("Doctor ${doctor.id} -> ${doctor.firstName} ${doctor.lastName}")
    }

//    Find a patient
    val patient = patients.find { it.id == 4 }
    if(patient != null) {
        println("The patient is ${patient.firstname} ${patient.lastname}")
    } else {
        println("The patient is not found")
    }

//    Find a doctor
    val doctor = doctors.find { it.id == 4 }
    doctor?.let {
        println("The doctor is ${doctor.id} ${doctor.firstName} ${doctor.lastName}")
    }

//    Print relationships in a nested way
    for (patient in patients) {
        println("${patient.firstname} has an appointment with ")
//        Find only patients with a doctor's appointment (i.e., not everyone in the patients list has an appointment with a doctor)
        val patientAppointments = appointments.filter { appointment -> appointment.patientId == patient.id} // filter appointment for this patient only
//        Find the doctor who's got an appointment with the patient and display to the console
        for (appointment in patientAppointments) {
            val doctor = doctors.find { doctor -> doctor.id == appointment.doctorId }  // find the corresponding doctor
            println(" - Doctor: ${doctor?.firstName} ${doctor?.lastName} with specialty ${doctor?.specialty} and reason of ${appointment.reason}")
        }
    }

}

