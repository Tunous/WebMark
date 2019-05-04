package me.thanel.webmark.test.base.work

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestDriver
import androidx.work.testing.WorkManagerTestInitHelper
import me.thanel.webmark.ext.asApp
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.hamcrest.collection.IsEmptyCollection.empty
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

abstract class BaseWorkerTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    protected lateinit var testDriver: TestDriver
        private set

    protected lateinit var workManager: WorkManager
        private set

    protected lateinit var appContext: Context
        private set

    @Before
    fun setupWorkManager() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)
        workManager = WorkManager.getInstance(appContext)
        testDriver = WorkManagerTestInitHelper.getTestDriver(appContext)!!
    }

    protected inline fun <reified T : Any> getDependency(): T {
        return appContext.asApp().kodein.direct.instance()
    }

    protected fun assertWorkNotExists(request: WorkRequest) {
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertNull("Work should not exist", workInfo)
    }

    protected fun assertWorkExists(request: WorkRequest) {
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertNotNull("Work should exist", workInfo)
    }

    protected fun assertWorkState(request: WorkRequest, state: WorkInfo.State) {
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertThat("Incorrect work state", workInfo.state, equalTo(state))
    }

    protected fun awaitWorkState(request: WorkRequest, state: WorkInfo.State) {
        val latch = CountDownLatch(1)
        val liveData = workManager.getWorkInfoByIdLiveData(request.id)
        val observer = Observer<WorkInfo> {
            if (it.state == state) {
                latch.countDown()
            }
        }
        liveData.observeForever(observer)

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Work (${request.id}) did not reach expected state ($state) in time")
        }
        liveData.removeObserver(observer)
    }

    protected fun assertWorkWithTagStarted(tag: String) {
        val workInfos = workManager.getWorkInfosByTag(tag).get()
        assertThat("Work should be started", workInfos, hasSize(1))
    }

    protected fun assertWorkWithTagNotStarted(tag: String) {
        val workInfos = workManager.getWorkInfosByTag(tag).get()
        assertThat("Work shouldn't be started", workInfos, empty())
    }
}
