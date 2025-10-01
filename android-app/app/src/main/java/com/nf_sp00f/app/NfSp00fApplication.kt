package com.nf_sp00f.app

import android.app.Application
<<<<<<< HEAD
import android.content.Context

/**
 * Main Application class for nf-sp00f EMV Security Platform Minimal implementation to achieve BUILD
 * SUCCESSFUL
 */
class NfSp00fApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    // Initialize core services
    initializeLogging()
    initializeErrorHandling()
  }

  private fun initializeLogging() {
    // Configure logging for debug/release builds
    // This could integrate with Timber or other logging frameworks
  }

  private fun initializeErrorHandling() {
    // Set up global error handling and crash reporting
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
      android.util.Log.e(
              "NfSp00fApplication",
              "Uncaught exception in thread ${thread.name}",
              exception
      )

      // In a production app, you might send crash reports here
      // For now, we'll just log and continue
    }
  }

  companion object {
    /** Get application context from anywhere */
    fun getContext(): Context? {
      return instance?.applicationContext
    }

    private var instance: NfSp00fApplication? = null

    init {
      // This will be called when the class is first loaded
    }
  }

  init {
    instance = this
  }
=======

class NfSp00fApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize application-wide components here
    }
>>>>>>> 52c0655 (🎯 Complete Priority 1-3: Production-grade CardReadingScreen with EmvWorkflowProcessor)
}
