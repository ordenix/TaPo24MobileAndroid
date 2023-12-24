package pl.tapo24.twa.exceptions

enum class HttpMessage(val message: String) {
    PostalCodeNotFound("Nie znaleziono kodu w bazie"),
    PostalCityNotFound("Nie znaleziono miasta w bazie"),
    AccountNotActivated("Konto nie jest aktywne"),
    AccountWasBanned("Konto zbanowano"),
    AccountNotEmailConfirmed("Konto nie jest aktywne"),
    WrongPasswordOrAccountNotFound("Błędny login lub hasło"),
    TokenExpired("Token stracił ważność lub jest niepoprawny"),
    LoginExistInService("Login jest już w użyciu, jeżeli uważasz to za błąd skontaktuj się z nami"),
    EmailExistInService("Email jest już w użyciu, jeżeli uważasz to za błąd skontaktuj się z nami"),
    MaxActivation("Osiągnięto maksymalną ilość prób aktywacji konta w ciągu 30 minut"),
    EmptyBody("Brak danych"),
    Internal("Błąd wewnętrzny"),
    UserNotFound("Użytkownika nie znaleziono"),
    UserNotHaveActivateAccount("Konto nie jest aktywne"),
    MaxRegenerateToken("Osiągnięto maksymalną ilość prób wygenerowania nowego tokenu w ciągu 10 minut"),
    TokenIsIncorrectly("Token jest niepoprawny"),
    AdvNowIsUnsubscribed("Subskrypcja treści reklamowych jest już dezaktywowana"),
    UserHaveConfirmedEmail("Konto zostało aktywowane, nie można go skasować w ten sposób"),
    MaxRegenerateForgotPassword("Osiągnięto maksymalną ilość prób wygenerowania żądania nowego hasła w ciągu 10 minut"),
    UserWasBanned("Użytkownik został zbanowany"),
    MaxExecuteForgotPassword("Osiągnięto maksymalną ilość prób żądania nadania nowego hasła ciągu 30 minut"),
    DifferentIp("Obecny adres IP jest inny niż ten podczas żądania zmiany hasła. Najprawdopodobniej doszło do zmiany sieci - spróbuj jeszcze raz wysłać żądanie zmiany hasła w tej samej sieci"),
    UserHaveAlreadyThatCustomCategory("Użytkownik posiada już taką kategorię"),
    ThisCustomCategoryNotExist("Taka kategoria nie istnieje"),

}