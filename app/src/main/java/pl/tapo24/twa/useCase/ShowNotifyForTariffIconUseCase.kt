package pl.tapo24.twa.useCase

import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import javax.inject.Inject

class ShowNotifyForTariffIconUseCase @Inject constructor(private val tapoDb: TapoDb){

    fun run() {
        val setting = Setting("showNotifyForTariffIcon", state = true)
        tapoDb.settingDb().insert(setting)
        State.showNotifyForTariffIcon = true

    }
}