package com.compose.shoppingcart.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.compose.shoppingcart.R
import com.compose.shoppingcart.ui.generic.AlertDialog
import com.compose.shoppingcart.ui.generic.AppDrawerContent
import com.compose.shoppingcart.ui.generic.ConfirmationDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onDetail: (Int) -> Unit,
    onShoppingCart: () -> Unit,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    onProfile: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("Journal") }
    val showLogoutDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user by viewModel.authState.collectAsState()

    val products by viewModel.product.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val photoUri by viewModel.photoUri.collectAsState()

    val showAlertDialog = remember { mutableStateOf(false) }
    var messageError = remember { mutableStateOf("") }


    val filteredProducts = products?.products?.filter {
        it.title?.contains(query, ignoreCase = true) == true || it.description?.contains(
            query,
            ignoreCase = true
        ) == true
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                currentRoute = selectedItem.value,
                onItemClicked = { route ->
                    selectedItem.value = route
                    scope.launch { drawerState.close() }
                    when (route) {
                        "Carrito" -> {
                            if (user == null) {
                                onLogin()
                            } else {
                                onShoppingCart()
                            }
                        }

                        "Mi Perfil" -> {
                            if (user == null) {
                                onLogin()
                            } else {
                                onProfile()
                            }
                        }
                    }
                },
                onLogoutClicked = {
                    scope.launch { drawerState.close() }
                    if (user == null) {
                        onLogin()
                    } else {
                        showLogoutDialog.value = true
                    }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                description = "Bienvenido, ${user?.email?.split("@")[0] ?: "Invitado"}",
                photoUri = photoUri ?: "",
                onProfile = onProfile,
                user = user
            )
        },
        gesturesEnabled = true,
        modifier = Modifier.fillMaxSize()
    ) {

        if (showLogoutDialog.value) {
            ConfirmationDialog(
                onDismiss = { showLogoutDialog.value = false },
                onConfirm = {
                    showLogoutDialog.value = false
                    scope.launch { drawerState.close() }
                    viewModel.logout(onSuccess = {
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        onLogout()
                    }, onError = {

                    })
                },
                text = "¿Deseas cerrar la sesión actual?"
            )
        }

        Scaffold(
            topBar = {
                HomeTopBar(
                    user = user,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onShoppingCart = { onShoppingCart() },
                    onLogin = { onLogin() },
                    title = "Shopping Car",
                    subtitle = user?.email?.split("@")[0] ?: "Invitado"
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(colorResource(R.color.background))
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .background(colorResource(R.color.primary))
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SearchBar(query = query, onQueryChange = { query = it })
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (query.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(filteredProducts ?: emptyList()) { product ->
                            ProductItem(product) { productId -> onDetail(productId) }
                        }
                    }
                } else {
                    if (error != null) {
                        showAlertDialog.value = true
                        messageError.value = error ?: "Error desconocido"
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredProducts ?: emptyList()) { product ->
                                ProductItem(product) { productId -> onDetail(productId) }
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