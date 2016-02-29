package domain

import java.io.File

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.analytics.{Analytics, AnalyticsScopes}

class AnalyticsConnectionFactory {
  val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance

  def createAnalytics(serviceAccountId: String, keyFileLocation: String, applicationName: String): Analytics = {
    val httpTransport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport
    val credential: GoogleCredential = new GoogleCredential.Builder()
      .setTransport(httpTransport)
      .setJsonFactory(jsonFactory)
      .setServiceAccountId(serviceAccountId)
      .setServiceAccountPrivateKeyFromP12File(new File(keyFileLocation))
      .setServiceAccountScopes(AnalyticsScopes.all)
      .build


    new Analytics.Builder(httpTransport, jsonFactory, credential)
      .setApplicationName(applicationName).build()
  }
}
