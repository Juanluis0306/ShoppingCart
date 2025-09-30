package com.compose.shoppingcart.navigation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.compose.shoppingcart.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de integración/UI para validar el flujo de navegación principal
 * con Jetpack Compose Navigation.
 */
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            runBlocking {
                try {
                    auth.signInWithEmailAndPassword("jlhm9606@gmail.com", "123456").await()
                } catch (e: Exception) {
                    print(e.message)
                }
            }
        }
    }

    @Test
    fun navegar_login_a_home_y_ver_lista_productos() {
        //Esperar a que cargue la pantalla de Home
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Shopping Car").fetchSemanticsNodes()
                .isNotEmpty()
        }
        // Abrir drawer
        composeTestRule.onNodeWithContentDescription("Menú").performClick()
        // Cerrar sesión
        composeTestRule.onNodeWithContentDescription("Cerrar sesión").performClick()
        // Aceptar alerta
        composeTestRule.onNodeWithContentDescription("BtnAccept").performClick()

        // Validar que estamos en pantalla de login
        composeTestRule.onNodeWithText("Inicia sesión").assertIsDisplayed()

        // Ingresar credenciales y hacer login
        composeTestRule.onNodeWithContentDescription("EmailField")
            .performTextInput("jlhm9606@gmail.com")
        composeTestRule.onNodeWithContentDescription("PasswordField")
            .performTextInput("123456")

        // Realizamos el login
        composeTestRule.onNodeWithContentDescription("BtnLogin").performClick()

        // Esperar a que cargue la pantalla de Home
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Shopping Car").fetchSemanticsNodes()
                .isNotEmpty()
        }
        // Validar que estamos en pantalla de Home
        composeTestRule.onNodeWithText("Shopping Car").assertIsDisplayed()

        // Validar que la lista de productos es visible
        composeTestRule.onNode(hasScrollAction()).assertIsDisplayed()
    }

    @Test
    fun navegar_de_home_a_detalle_y_regresar() {
        //Esperar a que cargue la pantalla de Home
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Shopping Car").fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Buscar primer producto en la lista y hacer click
        composeTestRule.onAllNodes(hasClickAction())[0].performClick()

        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Detalle de producto").fetchSemanticsNodes()
                .isNotEmpty()
        }
        // Validar que estamos en pantalla de detalle
        composeTestRule.onNodeWithContentDescription("BtnAdd").performClick()

        // Regresar
        composeTestRule.waitUntil(3_000) {
            composeTestRule.onAllNodesWithText("Shopping Car").fetchSemanticsNodes()
                .isNotEmpty()
        }
        // Validar que volvemos al Home
        composeTestRule.onNodeWithText("Shopping Car").assertIsDisplayed()
    }

    @Test
    fun navegar_al_carrito_desde_home() {
        //Esperar a que cargue la pantalla de Home
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Shopping Car").fetchSemanticsNodes()
                .isNotEmpty()
        }
        // Click en el ícono del carrito
        composeTestRule.onNodeWithContentDescription("BtnCart").performClick()

        // Validar que aparece la pantalla de carrito
        composeTestRule.onNodeWithText("Mi carrito").assertIsDisplayed()
    }
}
