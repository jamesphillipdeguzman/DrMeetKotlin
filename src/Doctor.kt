data class Doctor(
    val id: Int,
    val firstName: String?, // allow missing firstName for doctor
    val lastName: String,
    val specialty: String,
)
