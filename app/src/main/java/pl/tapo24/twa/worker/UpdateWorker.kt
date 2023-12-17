package pl.tapo24.twa.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateWorker(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        println("TEST")
        return Result.success()
    }

}