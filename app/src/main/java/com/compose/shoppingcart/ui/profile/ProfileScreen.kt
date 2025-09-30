package com.compose.shoppingcart.ui.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import java.io.File
import com.compose.shoppingcart.R
import com.compose.shoppingcart.ui.generic.ConfirmationDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val photoUri by viewModel.photoUri.collectAsState()
    val context = LocalContext.current
    val showLogoutDialog = remember { mutableStateOf(false) }


    val tmpUri = remember {
        File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }.let { file ->
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.savePhoto(tmpUri)
        }
    }

    Scaffold(
        containerColor = colorResource(R.color.background),
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.primary))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(24.dp))

            AsyncImage(
                model = photoUri ?: R.drawable.ic_app,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        takePictureLauncher.launch(tmpUri)
                    },
                placeholder = painterResource(R.drawable.ic_app)
            )

            Spacer(Modifier.height(16.dp))
            Text("Toca la foto para actualizar", color = Color.Gray)

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {showLogoutDialog.value = true},
                modifier = Modifier
                    .width(180.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.buttons)
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showLogoutDialog.value) {
        ConfirmationDialog(
            onDismiss = { showLogoutDialog.value = false },
            onConfirm = {
                showLogoutDialog.value = false
                viewModel.logout(onSuccess = {
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    onLogout()
                }, onError = {

                })
            },
            text = "¿Deseas cerrar la sesión actual?"
        )
    }
}
