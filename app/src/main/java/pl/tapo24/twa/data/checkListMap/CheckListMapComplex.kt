package pl.tapo24.twa.data.checkListMap

data class CheckListMapComplex(
    val checkListDictionary: CheckListDictionary,
    val checkListType: CheckListType,
    val id: Int,
    val sortOrder: Int
)