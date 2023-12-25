package pl.tapo24.twa.data.customCategory

data class DataCategory (
    val query: String,
    val name: String,
    val isCustom: Boolean,
    val customId: Int = 0,
) {
}