package de.c24.hg_abstraction.scan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity: AppCompatActivity() {

    companion object {
        //declare the key ,used to get the result value returned
        val SCAN_RESULT = "scanResult"
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun initBarCodeScanner() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .build()

        barcodeScanner = BarcodeScanning.getClient(options)
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

        cameraProvider.bindToLifecycle(
            this as LifecycleOwner, cameraSelector, preview, setupAnalyzer()
        )
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
                setResult(RESULT_OK, intent)
                this.finish()

            }
        }

        cameraExecutor?.let {
            imageAnalysis.setAnalyzer(it, qrCodeAnalyzer)
        }

        return imageAnalysis
    }
}