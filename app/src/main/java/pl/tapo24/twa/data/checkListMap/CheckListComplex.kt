package pl.tapo24.twa.data.checkListMap

data class CheckListComplex(
    val mapId:Int,
    val name:String,
    val sortOrder: Int,
    val pathToPdf: String? = null,
    var isSelected: Boolean = false,
    var isDeleted: Boolean = false
)
