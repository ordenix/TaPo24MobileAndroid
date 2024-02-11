package pl.tapo24.twa.ui.home.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.acra.ACRA
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.updater.InitPackageDownloader
import javax.inject.Inject

@HiltViewModel
class UpdaterViewModel @Inject constructor(
    private val initPackageDownloader: InitPackageDownloader,
    private val tapoDb: TapoDb
) : ViewModel() {
    val choseConnection = MutableLiveData<String>()
    var startDownloadProcedure = false

    val showPausedDialog = MutableLiveData<Boolean>(false)
    val showError = MutableLiveData<Boolean>(false)
    val showSuccess = MutableLiveData<Boolean>(false)

    private fun downloadOnExternal() {
        viewModelScope.launch(Dispatchers.IO) {
            initPackageDownloader.downloadInitPackage(true)
                ?.onSuccess {
                    successDownloadOnExternalStorage()
                }
                ?.onFailure {
                    // error on external first download
                    if (it.message?.contains("Permission denied") ?: false) {
                        // go to 1 st external
                        proceduralDownloadOnExternal(it)

                    } else if (it.message?.contains("PAUSED") ?: false) {
                        // paused
                        pausedDownload()
                    } else {
                        retryDownloadOnExternal()
                    }

                }
        }

    }

    private fun retryDownloadOnExternal() {
        viewModelScope.launch(Dispatchers.IO) {
            initPackageDownloader.downloadInitPackage(true)
                ?.onSuccess {
                    successDownloadOnExternalStorage()
                }
                ?.onFailure {
                    // error on retry external first download
                    if (it.message?.contains("PAUSED") ?: false) {
                        pausedDownload()
                    } else {
                        proceduralDownloadOnExternal(it)

                    }
                }
        }

    }

    private fun proceduralDownloadOnExternal(ex: Throwable) {
        viewModelScope.launch(Dispatchers.Main) {
            showError.value = true
        }
        ACRA.errorReporter.handleSilentException(ex)
    }
    private fun retryDownloadOnInternal() {
        viewModelScope.launch(Dispatchers.IO) {
            initPackageDownloader.downloadInitPackage(false)
                ?.onSuccess {
                    successDownloadOnInternalStorage()
                }
                ?.onFailure {
                    // error on internal second download
                    if (it.message?.contains("PAUSED") ?: false) {
                        pausedDownload()
                    } else {
                        // go to 1 st external
                        downloadOnExternal()
                    }
                }
        }

    }

    fun startDownload() {
        if (!startDownloadProcedure) {
            startDownloadProcedure = true
            // first DOWNLOAD on internal
            viewModelScope.launch(Dispatchers.IO) {
                initPackageDownloader.downloadInitPackage(false)
                    ?.onSuccess {
                        successDownloadOnInternalStorage()
                    }
                    ?.onFailure {
                        // error on internal first download
                        if (it.message?.contains("Permission denied") ?: false) {
                            // go to 1 st external
                            downloadOnExternal()

                        } else if (it.message?.contains("PAUSED") ?: false) {
                            // paused
                            pausedDownload()
                        } else {
                            retryDownloadOnInternal()
                        }
                    }
            }

        }

    }


    private fun successDownloadOnInternalStorage() {
        viewModelScope.launch(Dispatchers.Main) {
            initPackageDownloader.insertDataToDb()
            initPackageDownloader.saveSettings(false, choseConnection.value!!)
            showSuccess.value = true
        }


    }


    private fun successDownloadOnExternalStorage() {
        viewModelScope.launch(Dispatchers.Main) {
            initPackageDownloader.insertDataToDb()
            initPackageDownloader.saveSettings(true, choseConnection.value!!)
            showSuccess.value = true
        }

    }

    private fun pausedDownload() {
        viewModelScope.launch(Dispatchers.Main) {
            State.dialogDownloadFileMessage = "Błąd"
            State.dialogDownloadFileProgress.value = null
            showPausedDialog.value = true

        }
    }
}