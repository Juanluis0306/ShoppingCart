# ShoppingCart App

Aplicación de carrito de compras desarrollada en **Kotlin** utilizando **Jetpack Compose**, **Hilt**, **Room**, **Coroutines/Flow**, e integración con **Firebase**.

---

## Configuración del proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/usuario/shoppingcart.git
cd shoppingcart
```

### 2. Abrir en Android Studio

* Abrir el proyecto en **Android Studio Iguana o superior**.
* Esperar a que Gradle sincronice las dependencias.

### 3. Configuración de Firebase

1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Registrar la aplicación Android con el **applicationId** del proyecto (ej: `com.compose.shoppingcart`).
3. Descargar el archivo **`google-services.json`** desde Firebase Console.
4. Colocarlo en la ruta:

   ```
   app/google-services.json
   ```
5. Verificar que en el archivo `build.gradle` (app) exista:

   ```gradle
   apply plugin: 'com.google.gms.google-services'
   ```

### 4. Ejecutar la aplicación

* Conectar un dispositivo físico o usar un emulador Android (SDK 30+).
* Ejecutar en Android Studio.

---

## Arquitectura

El proyecto sigue el patrón **Clean Architecture + MVVM**:

* **data/**: Manejo de fuentes de datos (APIs, Room, DataStore). Incluye DAOs, Entities y Repositorios.
* **domain/**: Casos de uso (UseCases) que representan la lógica de negocio.
* **ui/**: Capas de presentación organizadas por pantallas (`home/`, `cart/`, `profile/`, etc.). Cada pantalla tiene su **ViewModel**.

### ¿Por qué MVVM?

* Permite separar responsabilidades entre **UI (View)**, **lógica de presentación (ViewModel)** y **datos (Repository/UseCase)**.
* Mejora la testabilidad al aislar la lógica de negocio de la interfaz.
* Compose + Flow se integran de forma natural con LiveData/StateFlow en MVVM.

---

## Esquema de Base de Datos (Room)

Actualmente se maneja una tabla principal para el carrito:

### Tabla `cart_items`

| Columna     | Tipo                   | Descripción                                 |
| ----------- | ---------------------- | ------------------------------------------- |
| `id`        | Int (PK, autogenerado) | Identificador único del ítem en el carrito. |
| `productId` | Int                    | ID del producto asociado.                   |
| `title`     | String                 | Nombre del producto.                        |
| `price`     | Double                 | Precio unitario del producto.               |
| `quantity`  | Int                    | Cantidad agregada al carrito.               |
| `thumbnail` | String                 | URL de la imagen del producto.              |

Actualmente no hay relaciones entre tablas, ya que el modelo es simple (solo carrito). Se puede expandir con tablas de `users` o `orders` en el futuro.

---

## Pruebas

* **Unitarias**: ubicadas en `/test/java/com/compose/shoppingcart/`. Ejemplo: `AddToCartUseCaseTest.kt`, `CartRepositoryImplTest.kt`.
* **UI Tests (Compose Test / Espresso)**: ubicados en `/androidTest/java/com/compose/shoppingcart/`.

Ejecutar pruebas desde Android Studio:

* Click derecho sobre el archivo de prueba → **Run 'NombreDeTest'**.
* O ejecutar todos los tests desde la pestaña **Run > Run...**.

---

## Dependencias principales

* **Jetpack Compose** (UI declarativa).
* **Hilt** (inyección de dependencias).
* **Room** (persistencia local).
* **DataStore** (preferencias).
* **Firebase Auth** (autenticación).
* **Coil** (carga de imágenes).
* **Lottie** (animaciones JSON).

---


## Funcionalidades Implementadas

#### Autenticación con Firebase

* Registro con email y contraseña.

* Login con credenciales creadas.

* Persistencia de sesión (Firebase Auth mantiene el estado).

* Cierre de sesión desde el Drawer.

* Opción de inicio de sesión biométrico como bonus.

#### Catálogo de Productos

* Pantalla principal con listado de productos desde dummyjson.com
  .

* Búsqueda en tiempo real (filtrado por nombre o descripción).

* Pantalla de detalle con toda la información del producto.

* Acceso a Perfil y Carrito restringido a usuarios autenticados.

#### Carrito de Compras

* Agregar productos desde el listado o el detalle.

* Persistencia local con Room.

* Pantalla de Carrito con:

* Ver productos agregados.

* Cambiar cantidad.

* Eliminar productos.

* Resumen con subtotal, descuento y total.

#### Arquitectura y Calidad

* Clean Architecture + MVVM.

* Hilt para inyección de dependencias.

* Código limpio, desacoplado y testeable.

#### Experiencia de Usuario

* Loading indicators al consultar la API.

* Placeholders para imágenes.

* Manejo de errores con mensajes amigables.

* Manejo de permisos para cámara y biometría.

#### Documentación

* README con setup, arquitectura y esquema de BD (este archivo).

#### Bonus Implementados

* Jetpack Compose en toda la UI.

* Navigation Component para flujo entre pantallas.

* Interceptor de OkHttp (para logs y headers).

* Foto de perfil tomada con cámara (persistencia de URI local).

* Pruebas unitarias y de UI (JUnit + MockK + Compose Test).

## Autor

Proyecto desarrollado por **Juan Luis Hernandez**
