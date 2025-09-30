package com.compose.shoppingcart.ui.generic

import com.compose.shoppingcart.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppDrawerContent(
    currentRoute: String,
    onItemClicked: (String) -> Unit,
    onLogoutClicked: () -> Unit,
    onCloseDrawer: () -> Unit,
    onProfile: () -> Unit,
    user: FirebaseUser?,
    description: String,
    photoUri: String?,
) {
    val drawerItems = listOf(
        DrawerItem("Mi Perfil", painterResource(R.drawable.ic_profile)),
        DrawerItem("Carrito", painterResource(R.drawable.ic_cart)),
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.primary))
                .padding(16.dp, top = 32.dp, bottom = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Cerrar menú",
                    tint = Color.White,
                    modifier = Modifier
                        .clickable { onCloseDrawer() }
                        .padding(end = 8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                AsyncImage(
                    model = if (user != null) photoUri else  R.drawable.ic_app,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable {
                            if (user != null){
                                onProfile()
                            }
                        },
                    placeholder = painterResource(R.drawable.ic_app),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text("Shopping Cart", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        description,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        drawerItems.forEach { item ->
            val isSelected = currentRoute == item.label
            val background = if (isSelected) Color.Transparent else Color.Transparent

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background)
                    .clickable { onItemClicked(item.label) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(painter = item.icon, contentDescription = item.label, tint = Color.Black)
                Spacer(modifier = Modifier.width(16.dp))
                Text(item.label, color = Color.Black, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogoutClicked() }
                .padding(16.dp, bottom = 80.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Cerrar sesión", color = Color.Black, fontSize = 16.sp)
        }
    }
}


data class DrawerItem(val label: String, val icon: Painter)

