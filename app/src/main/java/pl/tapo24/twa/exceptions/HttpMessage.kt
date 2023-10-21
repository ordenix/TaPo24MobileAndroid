package pl.tapo24.twa.exceptions

enum class HttpMessage(val message: String) {
    PostalCodeNotFound("Nie znaleziono kodu w bazie"),
    PostalCityNotFound("Nie znaleziono miasta w bazie"),
    AccountNotActivated("Konto nie jest aktywowane"),
    AccountWasBanned("Konto zbanowano"),
    AccountNotEmailConfirmed("Konto nie jest aktywowane"),
    WrongPasswordOrAccountNotFound("Błędny login lub hasło"),
    TokenExpired("Token stracił ważność "),
    LoginExistInService("Login jest już w użyciu, jeżeli uważasz to za błąd napisz do nas"),
    EmailExistInService("Email jest już w użyciu, jeżeli uważasz to za błąd napisz do nas")
}