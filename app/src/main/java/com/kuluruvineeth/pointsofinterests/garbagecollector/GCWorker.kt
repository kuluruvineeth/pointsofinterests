package com.kuluruvineeth.pointsofinterests.garbagecollector

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.kuluruvineeth.domain.features.poi.interactor.DeleteGarbageUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltWorker
class GCWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val deleteGarbageUseCase: DeleteGarbageUseCase
) : CoroutineWorker(context, workerParameters){

    override suspend fun doWork(): Result {
        val deletedItemCount = deleteGarbageUseCase.invoke(Unit)
        val resultData = Data.Builder().putInt(KEY_DELETED_ITEMS_COUNT, deletedItemCount!!).build()
        return Result.success(resultData)
    }

    companion object{
        const val KEY_DELETED_ITEMS_COUNT = "key_items_count"
        const val TAG_GC = "tag_poi_gc_worker"
    }
}