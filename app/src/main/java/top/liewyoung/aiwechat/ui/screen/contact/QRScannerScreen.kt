package top.liewyoung.aiwechat.ui.screen.contact

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.UUID
import java.util.concurrent.Executors
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.AIWeChatApplication
import top.liewyoung.aiwechat.model.Contact
import top.liewyoung.aiwechat.util.QRCodeUtil
import top.liewyoung.aiwechat.viewmodel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
        onBack: () -> Unit,
        onContactScanned: (Contact) -> Unit,
        viewModel: ContactViewModel =
                viewModel(
                        factory =
                                (LocalContext.current.applicationContext as AIWeChatApplication)
                                        .container.provideContactViewModelFactory()
                )
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
        )
    }

    var scannedOnce by remember { mutableStateOf(false) }

    val permissionLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
            ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = { Text("扫描二维码", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                            }
                        }
                )
            }
    ) { innerPadding ->
        Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
            if (hasCameraPermission) {
                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

                AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            val executor = Executors.newSingleThreadExecutor()

                            cameraProviderFuture.addListener(
                                    {
                                        val cameraProvider = cameraProviderFuture.get()

                                        val preview =
                                                Preview.Builder().build().also {
                                                    it.setSurfaceProvider(
                                                            previewView.surfaceProvider
                                                    )
                                                }

                                        val imageAnalysis =
                                                ImageAnalysis.Builder()
                                                        .setTargetResolution(Size(1280, 720))
                                                        .setBackpressureStrategy(
                                                                ImageAnalysis
                                                                        .STRATEGY_KEEP_ONLY_LATEST
                                                        )
                                                        .build()

                                        imageAnalysis.setAnalyzer(executor) { imageProxy ->
                                            if (!scannedOnce) {
                                                processImageProxy(imageProxy) { qrContent ->
                                                    scannedOnce = true
                                                    val contact =
                                                            QRCodeUtil.parseContactData(qrContent)
                                                    if (contact != null) {
                                                        // 生成新ID避免冲突
                                                        val newContact =
                                                                contact.copy(
                                                                        id =
                                                                                UUID.randomUUID()
                                                                                        .toString()
                                                                )
                                                        scope.launch {
                                                            viewModel.addContact(newContact)
                                                            onContactScanned(newContact)
                                                        }
                                                    }
                                                }
                                            }
                                            imageProxy.close()
                                        }

                                        try {
                                            cameraProvider.unbindAll()
                                            cameraProvider.bindToLifecycle(
                                                    lifecycleOwner,
                                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                                    preview,
                                                    imageAnalysis
                                            )
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    },
                                    ContextCompat.getMainExecutor(ctx)
                            )

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("需要相机权限才能扫描二维码")
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun processImageProxy(imageProxy: ImageProxy, onQRCodeDetected: (String) -> Unit) {
    val mediaImage = imageProxy.image ?: return
    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    val scanner = BarcodeScanning.getClient()
    scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (barcode.valueType == Barcode.TYPE_TEXT) {
                        barcode.rawValue?.let { content ->
                            if (content.startsWith("{") && content.contains("\"name\"")) {
                                onQRCodeDetected(content)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
}
