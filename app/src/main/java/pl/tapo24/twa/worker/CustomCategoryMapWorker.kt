package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CustomCategoryMapWorker(val context: Context, params: WorkerParameters): CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}