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
    CodeColors("data_code_colors", "Pobieranie danych kodów kolorów", "code_colors", namePl = "UTO"),
    TelephoneNumbers("data_telephone_numbers", "Pobieranie danych telefonicznych", "telephone_numbers", namePl = "UTO"),
    AdrNumber("data_adr_nuber", "Pobieranie danych numerów ADR", "adrNumber", namePl = "ADR"),
    Company("data_company", "Pobieranie danych firm", "company", namePl = "Firmy"),
    EnginePollution("data_engine_pollution", "Pobieranie danych norm zanieczyszczeń", "enginePollution", namePl = "Normy zanieczyszczeń"),
    HinNumber("data_hin_number", "Pobieranie danych numerów HIN", "hinNumber", namePl = "HIN"),
    ImmunityAdres("data_immunity_adres", "Pobieranie danych immunitetów - adresy", "immunityAdres", namePl = "Immunitety - adresy"),
    Immunity("data_immunity", "Pobieranie danych immunitetów", "immunity", namePl = "Immunitety"),
    NoiseLevel("data_noise_level", "Pobieranie danych norm hałasu", "noiseLevel", namePl = "Normy hałasu"),
    TechnicalCondition("data_technical_conditions", "Pobieranie danych wymagań technicznych", "technicalConditions", namePl = "Wymagań hałasu"),



}