package de.c24.hg_abstraction

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity: AppCompatActivity() {

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        val SCAN_RESULT = "scanResult"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
    }

    override fun onStart() {
        super.onStart()
        initBarCodeScanner()
        startCamera()
    }
    
    private fun startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {

        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()

        preview.setSurfaceProvider(previewview.surfaceProvider)

        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, setupAnalyzer())
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initBarCodeScanner() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .build()

        barcodeScanner = BarcodeScanning.getClient(options)
    }

    private fun setupAnalyzer(): ImageAnalysis {
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setImageQueueDepth(30)
            .build()

        val qrCodeAnalyzer = YourImageAnalyzer(barcodeScanner) { qrCodes ->
            qrCodes.firstOrNull()?.rawValue?.let { qrToken ->
                cameraExecutor?.shutdown()
                cameraProviderFuture?.get()?.unbindAll()
                val intent = Intent()
                intent.apply {
                    putExtra(SCAN_RESULT, qrToken) }
                setResult(Activity.RESULT_OK, intent)
                this.finish()

            }
        }

        cameraExecutor?.let {
            imageAnalysis.setAnalyzer(it, qrCodeAnalyzer)
        }

        return imageAnalysis
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private class YourImageAnalyzer(
        barcodeScanner: BarcodeScanner,
        private val onQrCodesDetected: (qrCodes: List<Barcode>) -> Unit
    ) : ImageAnalysis.Analyzer {
        val barCodeScanner = barcodeScanner

        override fun analyze(imageProxy: ImageProxy) {
            imageProxy.image?.let { inputImage ->
                val processingImage = InputImage.fromMediaImage(
                    inputImage,
                    imageProxy.imageInfo.rotationDegrees
                )

                barCodeScanner.process(processingImage)
                    .addOnSuccessListener {
                        imageProxy.close()
                        onQrCodesDetected.invoke(it)
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }

}