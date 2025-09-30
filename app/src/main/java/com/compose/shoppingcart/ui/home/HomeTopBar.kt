package com.compose.shoppingcart.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compose.shoppingcart.R
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeTopBar(
    user: FirebaseUser?,
    onMenuClick: () -> Unit,
    onShoppingCart: () -> Unit,
    onLogin: () -> Unit,
    title: String,
    subtitle: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart_anim))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.primary))
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.White, fontSize = 12.sp)
            }

            if (user != null) {
                IconButton(onClick = { onShoppingCart() }) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .height(40.dp)
                            .semantics { contentDescription = "BtnCart" }
                    )
                }
            } else {
                IconButton(onClick = { onLogin() }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Iniciar sesión",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
