package com.example.test_platform_channel

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.test_platform_channel.arcgis.ArcGISMapActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterFragmentActivity() {
    private val CHANNEL = "flutter_channel"

    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Autenticate usando el sensor biométrico")
            .setAllowedAuthenticators(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            ).build()

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, channelResult ->
            when (call.method) {
                "showDialog" -> {
                    val title = call.argument<String>("title")
                    val message = call.argument<String>("message")

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(title)
                    builder.setMessage(message)
                    builder.setPositiveButton("Aceptar") { _, _ ->
                        Log.i("message Flutter", "Genial")
                    }
                    builder.setNegativeButton("Cancelar") { _, _ ->
                        // Acción cuando se presiona el botón "Cancelar"
                    }
                    val dialog = builder.create()
                    dialog.show()

                    channelResult.success(null)
                }
                "startNativeActivity" -> {
                    val intent = Intent(this, TestActivity::class.java)
                    startActivity(intent)
                    channelResult.success(null)
                }
                "biometricAuth" -> {

                    if(BiometricManager.from(this).canAuthenticate(
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        ) == BIOMETRIC_SUCCESS) {

                        channelResult.success("SUCCESS")

                        BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                            object: BiometricPrompt.AuthenticationCallback() {
                                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                    super.onAuthenticationSucceeded(result)

                                    methodChannel.invokeMethod("biometricAuthStream", "AUTHENTICATED")
                                }

                                override fun onAuthenticationFailed() {
                                    super.onAuthenticationFailed()

                                    methodChannel.invokeMethod("biometricAuthStream", "NOT_AUTHENTICATED")
                                }

                                override fun onAuthenticationError(
                                    errorCode: Int,
                                    errString: CharSequence
                                ) {
                                    super.onAuthenticationError(errorCode, errString)

                                    methodChannel.invokeMethod("biometricAuthStream", "CANCEL_AUTHENTICATED")
                                }
                            }).authenticate(promptInfo)

                    } else {
                        channelResult.success("FAILED")
                    }
                }
                "arcGISActivity" -> {
                    val intent = Intent(this, ArcGISMapActivity::class.java)
                    startActivity(intent)
                    channelResult.success(null)
                }
                else -> {
                    channelResult.notImplemented()
                }
            }
        }

    }
}
