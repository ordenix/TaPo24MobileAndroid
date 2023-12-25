package pl.tapo24.twa.data.customCategory

enum class CategoryDictionary(val element: DataCategory) {
    All(DataCategory("%", "Wszystkie", false)),
    Prevention(DataCategory("prevention", "Prewencja", false)),
    Accident(DataCategory("accident", "Kolizja", false)),
    Pedestrian(DataCategory("pedestrian", "Wykroczenia pieszych", false)),
    Support(DataCategory("support", "Wykroczenia pieszych z urz. wsp. ruch/ rowerzystów", false)),
    ToPede(DataCategory("to_pede", "Wykroczenia wobec pieszych przez poj. mech.", false)),
    NonMechToPede(DataCategory("non_mech_to_pede", "Wykroczenia wobec pieszych przez poj. inny niż mech.", false)),
    Speed(DataCategory("speed", "Prędkość pojazdu/ wyprzedzanie", false)),
    Lights(DataCategory("lights", "Światła zewnętrzne", false)),
    Sign(DataCategory("sign", "Znaki i sygnały", false)),
    Belts(DataCategory("belts", "Przewóz osób/ pasy bezp.", false)),
    Invalid(DataCategory("invalid", "Karta Parkingowa", false)),
    Stop(DataCategory("stop", "Postój/Zatrzymanie/ Cofanie/ Zawracanie/ Holowanie", false)),
    Allow(DataCategory("allow", "Dopuszczenie/ Kierowanie", false)),
    Package(DataCategory("package", "Przewóz ładunku/ Tablice/ Przejazd Kolejowy lub tramw.", false)),
    Others(DataCategory("others", "Pozostałe", false)),
}