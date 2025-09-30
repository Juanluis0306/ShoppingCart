package com.compose.shoppingcart.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.compose.shoppingcart.R
import com.compose.shoppingcart.ui.generic.AlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCart: () -> Unit
) {
    val product by viewModel.product.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    var quantity by remember { mutableIntStateOf(1) }

    val showAlertDialog = remember { mutableStateOf(false) }
    var messageError = remember { mutableStateOf("") }

    val user by viewModel.authState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(productId) {
        viewModel.loadProductById(productId)
    }

    Scaffold(
        containerColor = colorResource(R.color.white),
        topBar = {
            TopAppBar(
                title = { Text("Detalle de producto", color = Color.White) },
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
        when {
            loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error != null -> {
                showAlertDialog.value = true
                messageError.value = error ?: "Error desconocido"
            }

            product != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    AsyncImage(
                        model = product?.thumbnail,
                        contentDescription = product?.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(product?.title ?: "", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(product?.description ?: "", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$${product?.price}",
                        color = colorResource(R.color.primary_dark),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    if ((product?.stock ?: 0) > 0)
                        Text(
                            "En Stock",
                            color = colorResource(R.color.accent_green),
                            fontWeight = FontWeight.Bold
                        )
                    Text(product?.shippingInformation ?: "", fontSize = 12.sp, color = Color.Gray)

                    Spacer(Modifier.height(16.dp))

                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Cantidad: ", fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_less),
                                    contentDescription = "Disminuir",
                                    tint = colorResource(R.color.buttons)
                                )
                            }
                            Text("$quantity", fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { quantity++ }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_more),
                                    contentDescription = "Aumentar",
                                    tint = colorResource(R.color.accent_green)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (user == null) {
                                Toast.makeText(
                                    context,
                                    "Debes iniciar sesión para agregar al carrito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            product?.let { p ->
                                Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT)
                                    .show()
                                viewModel.addToCart(p, quantity)
                                onCart()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp).semantics { contentDescription = "BtnAdd" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.buttons)
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Agregar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    if (showAlertDialog.value) {
        AlertDialog(
            onDismiss = { showAlertDialog.value = false },
            text = messageError.value
        )
    }
}
