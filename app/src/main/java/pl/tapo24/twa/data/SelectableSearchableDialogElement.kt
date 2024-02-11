package pl.tapo24.twa.data

data class SelectableSearchableDialogElement(
    val f1: String,
    val f2: String = "",
    val f3: String = "",
    var isSelected: Boolean = false
)
