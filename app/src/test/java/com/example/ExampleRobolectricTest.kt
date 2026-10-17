package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.IbadatRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ইবাদত ট্র্যাকার", appName)
  }

  @Test
  fun `test ibadat repository point calculation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = IbadatRepository(context)
    val testDate = "2026-10-17"
    
    // Toggle Fajr (+10) and Fajr Dua (+5)
    repository.toggleTask("salah", testDate, "fajr")
    repository.toggleTask("salah", testDate, "fajr_dua")
    
    val points = repository.calculatePoints(testDate)
    assertEquals(15, points)
  }
}
