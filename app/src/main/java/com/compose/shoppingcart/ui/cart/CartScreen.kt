package com.compose.shoppingcart.ui.cart

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compose.shoppingcart.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val context = LocalContext.current

    val subtotal = cartItems.sumOf { it -> it.price * it.quantity }
    val discount = (subtotal * 0.1).toInt()
    val shipping = 50
    val total = subtotal - discount + shipping

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
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
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (cartItems.isEmpty()) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.cart_anim)
                )
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Tu carrito está vacío",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.updateQuantity(item.id, item.quantity + 1) },
                            onDecrease = {
                                if (item.quantity > 1) {
                                    viewModel.updateQuantity(item.id, item.quantity - 1)
                                }
                            },
                            onDelete = { viewModel.removeFromCart(item.id) }
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    val currentDate =
                        SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(Date())
                    Column(Modifier.padding(16.dp)) {
                        Text("Resumen de orden", fontWeight = FontWeight.Bold)
                        Text(currentDate, fontSize = 12.sp, color = Color.Gray)

                        Spacer(Modifier.height(6.dp))

                        SummaryRow("Sub Total", "$${String.format("%.2f", subtotal)}")
                        SummaryRow("Descuento (10%)", "-$$discount")
                        SummaryRow("Envío", "+$$shipping")

                        Divider(Modifier.padding(vertical = 8.dp))

                        SummaryRow("Total", "$${String.format("%.2f", total)}", bold = true)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total a pagar\n$${String.format("%.2f", total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Button(
                        onClick = {
                            Toast.makeText(context, "Procesando compra...", Toast.LENGTH_SHORT)
                                .show()
                            onCheckout()
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.buttons)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Pagar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}