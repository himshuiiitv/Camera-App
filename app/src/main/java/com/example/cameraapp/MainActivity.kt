package com.example.cameraapp

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.cameraapp.ui.theme.CameraAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var bitmap = remember {
                mutableStateOf<Bitmap?>(null)
            }
            val cameraLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {

                    bitmap.value = it
                }
            CameraAppTheme {
                var permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) {
                    if (it) {     // it means permission asked is granted
                        cameraLauncher.launch()
                    }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        
                        var context = LocalContext.current
                        TextButton(onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                )
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                cameraLauncher.launch()

                            } else {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }

                        }) {
                            Text(text = "Use Camera")
                        }
                        bitmap.value.let {
                            if(it!=null){
                                Image(bitmap = it.asImageBitmap(), contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}
