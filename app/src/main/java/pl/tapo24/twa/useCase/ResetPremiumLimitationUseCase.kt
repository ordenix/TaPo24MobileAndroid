package pl.tapo24.twa.useCase

import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import javax.inject.Inject

class ResetPremiumLimitationUseCase @Inject constructor(private val tapoDb: TapoDb){

    fun run() {
        val setting = Setting("showNotifyForTariffIcon", state = true)
        val settingLimitTariffInSignCount = Setting("limitTariffInSignCount", count = 0)
        tapoDb.settingDb().insert(setting)
        tapoDb.settingDb().insert(settingLimitTariffInSignCount)
        State.showNotifyForTariffIcon = true

    }
}