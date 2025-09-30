package com.compose.shoppingcart.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.compose.shoppingcart.R
import com.compose.shoppingcart.ui.generic.AlertDialog

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit,
    onRegister: () -> Unit
) {
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val showAlertDialog = remember { mutableStateOf(false) }
    var messageError = remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFormValid = email.isNotBlank() && password.isNotBlank()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.primary))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text("Inicia sesión", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        TextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            placeholder = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "EmailField" },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        Spacer(Modifier.height(16.dp))

        TextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            placeholder = { Text("Contraseña") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
                .semantics { contentDescription = "PasswordField" },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) R.drawable.ic_ocultar_pass else R.drawable.ic_mostrar_pass
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(24.dp))

        if (showAlertDialog.value) {
            AlertDialog(
                onDismiss = { showAlertDialog.value = false },
                text = messageError.value
            )
        }

        Button(
            onClick = {
                viewModel.login(email = email, password = password, onSuccess = {
                    onAuthSuccess()
                }, onError = {
                    showAlertDialog.value = true
                    messageError.value = error ?: "Error desconocido"
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics { contentDescription = "BtnLogin" },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.buttons)
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = isFormValid
        ) {
            Text("Ingresar", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = Color.White,
            modifier = Modifier
                .clickable { onRegister() }
                .padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 8.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

        }
    }
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}