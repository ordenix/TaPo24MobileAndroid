package pl.tapo24.exceptions

enum class InternalMessage(val message: String) {
    InternalGetUid("Internal exception during get uid"),
    InternalGetBaseVersion("Internal exception during get data base version"),
    InternalGetCodeDrivingLicence("Internal exception during get data code driving licence")

}