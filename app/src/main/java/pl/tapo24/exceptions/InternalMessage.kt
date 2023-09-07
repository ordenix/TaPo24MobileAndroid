package pl.tapo24.exceptions

enum class InternalMessage(val message: String) {
    InternalGetUid("Internal exception during get uid"),
    InternalGetBaseVersion("Internal exception during get data base version"),
    InternalGetCodeDrivingLicence("Internal exception during get data code driving licence"),
    InternalGetCountry("Internal exception during get data country driving licence"),
    InternalGetLightsCode("Internal exception during get data lights code"),
    InternalGetFrontLights("Internal exception during get data front lights"),
    InternalGetLightsOther("Internal exception during get data lights other"),
    InternalGetStatus("Internal exception during get data status"),
    InternalGetTowing("Internal exception during get data towing"),
    InternalGetStory("Internal exception during get data story"),
    InternalGetPointsOld("Internal exception during get data points old"),
    InternalGetPointsNew("Internal exception during get data points new"),
    InternalGetControlList("Internal exception during get data control list"),
    InternalGetCodeLimits("Internal exception during get data code limits"),
    InternalGetHoldingDocuments("Internal exception during get data holding documents"),
    InternalGetUtoDocuments("Internal exception during get data uto "),
    InternalGetSignDocuments("Internal exception during get data sign"),
    InternalImpossibleState("Internal IMPOSSIBLE STATE"),
    InternalGetLawDocuments("Internal exception during get data law"),
    InternalGetLawTariff("Internal exception during get data Tariff"),
    InternalGetElastic("Internal exception during get data from elastic"),




}