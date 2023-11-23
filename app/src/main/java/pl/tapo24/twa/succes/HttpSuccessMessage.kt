package pl.tapo24.twa.succes

enum class HttpSuccessMessage(val message: String) {
    SuccessLogin("Poprawnie zalogowano do serwisu."),
    SuccessAccountActivate("Poprawnie aktywowano konto w serwisie. Możesz się zalogować."),
    UserHaveActivateAccount("Użytkownik posiada aktywne konto."),
    SuccessRegenerateToken("Poprawnie wygenerowano token aktywacyjny. Sprawdź e-mail."),
    SuccessUnSubscribeAdv("Poprawnie dezaktywowano treści reklamowe."),
    SuccessDeleteAccount("Poprawnie usunięto konto z serwisu. Dziękujemy, że byłeś z nami, mamy nadzieję, że niebawem do nas wrócisz."),
    SuccessForgotStep1("Poprawnie wygenerowano żądanie zmiany hasła. Sprawdź pocztę i postępuj zgodnie z instrukcjami w wiadomości e-mail."),
    SuccessForgotStep2("Poprawnie przetworzono żądanie zmiany hasła. Możesz się zalogować.")

}