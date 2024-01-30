package pl.tapo24.twa.updater

enum class DataBaseType (
    val path: String,
    val info: String,
    val nameInDb: String,
    val isGeneric: Boolean = true,
    val namePl: String)
{
    CheckList("xxxxxxxxxxxx", "Pobieranie danych check list", "check_list", false, "Check listy"),
    CodeCountryDrivingLicence("data_country_driving_licence", "Pobieranie danych zagranicznych PJ", "code_country_driving_licence", namePl = "Kody krajów Pj"),
    CodeDrivingLicence("data_code_driving_licence", "Pobieranie danych kodów prawa jazdy", "code_driving_licence", namePl = "Kody PJ"),
    CodeLights("data_lights_code_country", "Pobieranie danych kodów świateł", "code_lights", namePl = "Kody świateł"),
    CodeLimitsDrivingLicence("data_code_limits_driving_licence", "Pobieranie danych kodów ograniczeń PJ", "code_limits_driving_licence", namePl = "Kody ograniczeń Pj"),
    ControlList("data_control_list", "Pobieranie danych listy kontrolnej", "control_list", namePl = "Lista kontrolna"),
    HoldingDocuments("data_holding_documents", "Pobieranie danych zatrzymywania dokumentów", "holding_documents", namePl = "Zatrzymywanie dokumentów"),
    LightsFront("data_lights_front", "Pobieranie danych świateł przednich", "lights_front", namePl = "Światła przednie"),
    LightsOther("data_lights_others", "Pobieranie danych świateł zewnętrznych", "lights_others", namePl = "Światła zewnętrzne"),
    CodePointsNew("data_code_points_new", "Pobieranie danych punktów", "points_new", namePl = "Punkty"),
    Sign("data_sign", "Pobieranie danych znaków", "sign", namePl = "Znaki"),
    Spb("data_spb", "Pobieranie danych spb", "spb", namePl = "ŚPB"),
    Status("data_status", "Pobieranie danych statusów", "status", namePl = "Statusy"),
    Story("data_story", "Pobieranie danych historyjek", "story", namePl = "Historyjki"),
    Tariff("data_tariff", "Pobieranie danych taryfikatora", "tariff", false, "Taryfikator"),
    Towing("data_towing", "Pobieranie danych holowania", "towing", namePl = "Holowanie"),
    Uto("data_uto", "Pobieranie danych uto", "uto", namePl = "UTO"),


}