package pl.tapo24.twa.exceptions

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
    InternalGetAssetList("Internal exception during get data Asset List"),
    InternalGetAppVersion("Internal exception during get data App Version"),
    InternalGetPostal("Internal exception during get postal code data"),
    InternalLogin("Internal exception during login"),
    InternalTestToken("Internal exception during test token"),
    InternalFavOffenseToken("Internal exception during process favourite offense"),
    InternalGetSpb("Internal exception during get data Spb"),
    InternalRegister("Internal exception during register"),
    InternalCustomCategory("Internal exception during custom category"),
    InternalCustomCategoryMap("Internal exception during custom category map"),
    InternalGetUserData("Internal exception during get user data"),
    InternalGetMourning("Internal exception during get data Mourning"),
    InternalGetData("Internal exception during get data"),
    InternalGetHomeNewsData("Internal exception during get home news data"),
    InternalGetSurveyList("Internal exception during get survey list"),
    InternalSendSurvey("Internal exception during send survey"),
    InternalGetSurveyProgress("Internal exception during get survey progress"),
    InternalChangeSurveyStatus("Internal exception during change survey status"),



}