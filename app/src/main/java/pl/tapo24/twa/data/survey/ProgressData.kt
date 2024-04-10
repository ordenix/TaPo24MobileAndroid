package pl.tapo24.twa.data.survey

data class ProgressData(
    val answerList: List<ProgressAnswer>,
    val totalUserRejectResponse: Int,
    val totalUserResponse: Int
)