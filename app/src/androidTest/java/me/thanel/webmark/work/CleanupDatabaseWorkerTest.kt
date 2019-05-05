package me.thanel.webmark.work

import androidx.work.WorkInfo
import me.thanel.webmark.test.base.work.BaseWorkerTest
import org.junit.Test

class CleanupDatabaseWorkerTest : BaseWorkerTest() {

    @Test
    fun delayed_work_will_fire_after_delay() {
        val (request, operation) = CleanupDatabaseWorker.enqueueDelayed(appContext)
        operation.result.get()

        assertWorkState(request, WorkInfo.State.ENQUEUED)

        testDriver.setInitialDelayMet(request.id)

        awaitWorkState(request, WorkInfo.State.SUCCEEDED)
    }

    @Test
    fun new_delayed_work_will_replace_previous() {
        val (request, operation) = CleanupDatabaseWorker.enqueueDelayed(appContext)
        operation.result.get()
        assertWorkState(request, WorkInfo.State.ENQUEUED)

        val (newRequest, newOperation) = CleanupDatabaseWorker.enqueueDelayed(appContext)
        newOperation.result.get()

        assertWorkNotExists(request)
        assertWorkState(newRequest, WorkInfo.State.ENQUEUED)
    }

    @Test
    fun periodic_work_will_work_correctly() {
        val (request, operation) = CleanupDatabaseWorker.enqueuePeriodic(appContext)
        operation.result.get()
        assertWorkState(request, WorkInfo.State.ENQUEUED)

        testDriver.setAllConstraintsMet(request.id)
        testDriver.setPeriodDelayMet(request.id)

        awaitWorkState(request, WorkInfo.State.ENQUEUED)
    }

    @Test
    fun new_periodic_work_will_keep_previous() {
        val (request, operation) = CleanupDatabaseWorker.enqueuePeriodic(appContext)
        operation.result.get()

        val (newRequest, newOperation) = CleanupDatabaseWorker.enqueuePeriodic(appContext)
        newOperation.result.get()

        assertWorkState(request, WorkInfo.State.ENQUEUED)
        assertWorkNotExists(newRequest)
    }

    @Test
    fun periodic_and_delayed_work_will_not_override_each_other() {
        val (periodicRequest, periodicOperation) = CleanupDatabaseWorker.enqueuePeriodic(appContext)
        periodicOperation.result.get()
        val (delayedRequest, delayedOperation) = CleanupDatabaseWorker.enqueueDelayed(appContext)
        delayedOperation.result.get()

        assertWorkState(periodicRequest, WorkInfo.State.ENQUEUED)
        assertWorkState(delayedRequest, WorkInfo.State.ENQUEUED)
    }

    @Test
    fun cancelling_delayed_work_will_cancel_delayed_work() {
        val (request, operation) = CleanupDatabaseWorker.enqueueDelayed(appContext)
        operation.result.get()
        assertWorkState(request, WorkInfo.State.ENQUEUED)

        CleanupDatabaseWorker.cancelNonPeriodic(appContext)

        assertWorkState(request, WorkInfo.State.CANCELLED)
    }

    @Test
    fun cancelling_delayed_work_will_not_cancel_periodic_work() {
        val (request, operation) = CleanupDatabaseWorker.enqueuePeriodic(appContext)
        operation.result.get()
        assertWorkState(request, WorkInfo.State.ENQUEUED)

        CleanupDatabaseWorker.cancelNonPeriodic(appContext)

        assertWorkState(request, WorkInfo.State.ENQUEUED)
    }
}
