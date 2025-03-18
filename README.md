# Proyecto appSocial

## Descripción

Importante!: la app no esta terminada y es un proyecto universitario desarrollado para la asignatura "Desarrollo Mobile"

appSocial es una aplicación móvil de tipo social. Actualmente, permite a los usuarios compartir experiencias de viaje a través de publicaciones (posts) que incluyen imágenes, descripciones, precios y ubicaciones. Otros usuarios pueden interactuar con estas publicaciones mediante comentarios. La aplicación también ofrece funcionalidades básicas de autenticación y gestión de perfiles.

## Tecnologías Principales

-   **Java**: Lenguaje de programación principal de la aplicación.
-   **Android Studio**: IDE utilizado para el desarrollo de la aplicación.
-   **MVVM**: Arquitectura empleada para estructurar el proyecto.
-   **Fragments y Activities**: Utilizados para estructurar las diferentes pantallas y secciones de la aplicación.
-   **Parse**: Para la persistencia de datos y autenticación de usuarios.

## Funcionalidades Principales

1.  **Menú de Navegación**: Un menú de navegación inferior con las siguientes opciones:
   -   **Home**: Página de inicio de la aplicación (publicaciones).
   -   **Perfil**: Pantalla donde el usuario puede ver y editar su perfil, y gestionar sus publicaciones.
   -   **Chats**: Página para iniciar y gestionar conversaciones (pendiente de implementación).
   -   **Filtros**: Opciones de filtrado de contenido.

2.  **Vista de Login**: La vista principal de la aplicación es una página de inicio de sesión. Los usuarios pueden iniciar sesión si ya tienen una cuenta registrada. En caso de no estar registrados, son dirigidos a la página de registro.

3.  **Registro de Usuario**: Páginas de registro e inicio de sesión para usuarios no registrados, implementadas mediante Parse.

4.  **Publicaciones de Viaje (Posts)**:
   -   Los usuarios pueden crear publicaciones de viaje con hasta 3 imágenes, descripción, precio y lugar.
   -   Otros usuarios pueden ver estas publicaciones y dejar comentarios.
   -   Los usuarios pueden ver y eliminar sus propias publicaciones desde su perfil.
   -   Los usuarios pueden ver los detalles de una publicación individual.

5.  **Perfil de Usuario**:
   -   Los usuarios pueden ver sus publicaciones en su perfil.
   -   Los usuarios pueden eliminar sus publicaciones desde su perfil.
   -   Los usuarios pueden actualizar su información de perfil (con problemas pendientes).

6.  **Filtros**: Los usuarios pueden filtrar las publicaciones por diferentes criterios.


## Estructura del Proyecto

La estructura del proyecto sigue el patrón MVVM (Model-View-ViewModel) y está organizada en diferentes paquetes según el propósito de cada clase.

### `/main/java/com/murek/appsocial`

#### `/adapters`

-   **CarruselAdapter.java**: Adaptador para mostrar múltiples imágenes en un carrusel.
-   **ComentarioAdapter.java**: Adaptador para mostrar comentarios en una lista.
-   **ImageAdapter.java**: Adaptador para manejar la selección de imágenes.
-   **ImageSlideAdapter.java**: Adaptador para mostrar imágenes en una presentación de diapositivas.
-   **PostAdapter.java**: Adaptador para mostrar publicaciones de viaje en una lista.
-   **TransdormerAdapter.java**: Adaptador para efectos de transición en el carrusel de imágenes.

#### `/model`

-   **Comentario.java**: Define la clase de comentario con sus atributos y métodos.
-   **Post.java**: Define la clase de publicación con sus atributos y métodos.
-   **User.java**: Define la clase de usuario con sus atributos y métodos.

#### `/providers`

-   **AuthProvider.java**: Proveedor de autenticación para la gestión de inicio de sesión y registro.
-   **PostProvider.java**: Proveedor para realizar operaciones de CRUD con las publicaciones de viaje (usando Parse).
-   **UserProvider.java**: Proveedor para realizar operaciones de CRUD con los datos de usuario (actualmente usando Parse).

#### `/util`

-   **ImageUtils.java**: Contiene utilidades para el manejo de imágenes.
-   **Validaciones.java**: Contiene funciones para validar los datos de entrada en los formularios, como correos electrónicos, contraseñas, etc.

#### `/view`

-   **AppSocial.java**: Clase de aplicación principal para la inicialización.
-   **HomeActivity.java**: Activity para la pantalla de inicio.
-   **MainActivity.java**: Activity principal que gestiona la navegación entre las secciones de la app.
-   **PostActivity.java**: Activity para crear nuevas publicaciones de viaje.
-   **PostDetailActivity.java**: Activity para ver los detalles de una publicación de viaje.
-   **RegisterActivity.java**: Activity para el registro de usuarios.

#### `/view/fragments`

-   **ChatsFragment.java**: Fragmento para gestionar las conversaciones (en evaluacion de si se utilizara o no).
-   **FiltrosFragment.java**: Fragmento donde se gestionan los filtros.
-   **HomeFragment.java**: Fragmento que muestra las publicaciones de viaje.
-   **PerfilFragment.java**: Fragmento para ver y editar el perfil de usuario, y gestionar las publicaciones del usuario.

#### `/viewModel`

-   **AuthViewModel.java**: ViewModel para la autenticación general.
-   **MainAuthViewModel.java**: ViewModel para la lógica de la actividad principal.
-   **PostAuthViewModel.java**: ViewModel para la lógica de las publicaciones.
-   **PostDetailAuthViewModel.java**: ViewModel para la lógica del detalle de las publicaciones.
-   **RegisterAuthViewModel.java**: ViewModel que gestiona la lógica de registro de usuario.
-   **UserAuthViewModel.java**: ViewModel para la lógica del perfil de usuario y gestión de publicaciones.

### `/res/layout`

-   **activity\_home.xml**: Layout para la pantalla de inicio.
-   **activity\_main.xml**: Layout de la pantalla principal.
-   **activity\_post.xml**: Layout para la creación de publicaciones.
-   **activity\_post\_detail.xml**: Layout para los detalles de una publicación.
-   **activity\_register.xml**: Layout para el registro de usuarios.
-   **fragment\_chats.xml**: Layout para la sección de chats (pendiente de implementación).
-   **fragment\_filtros.xml**: Layout para la sección de filtros.
-   **fragment\_home.xml**: Layout para la pantalla de inicio (publicaciones).
-   **fragment\_perfil.xml**: Layout para el perfil de usuario.
-   **item\_comentario.xml**: Layout para cada comentario en la lista.
-   **item\_image.xml**: Layout para la selección de imágenes.
-   **item\_post.xml**: Layout para cada publicación de viaje en la lista.
-   **item\_slide\_image.xml**: Layout para cada imagen en la presentación de diapositivas.
-   **progress\_layout.xml**: Layout para mostrar el progreso de carga.
-   **spinner\_item.xml**: Layout para los elementos del spinner.

### `/res/menu`

-   **bottom\_navigation\_menu.xml**: Menú de navegación inferior con opciones para **Home**, **Perfil**, **Chats** y **Filtros**.

## Notas Importantes

-   **Parse**: La autenticación de usuarios y la persistencia de datos se realizan mediante Parse.
-   **Validaciones de Usuario**: Es importante implementar validaciones adecuadas para el registro y la actualización de los datos de usuario para asegurar la integridad de la información.
-   **Gestión de Estados**: Dado que la aplicación usa MVVM, los cambios en el modelo se reflejan automáticamente en la vista, mejorando la respuesta de la interfaz y simplificando el manejo de estados.
- **Chats**: El fragmento para gestionar conversaciones está pendiente de implementación y se está evaluando su inclusión en futuras versiones.


## Contacto

Para cualquier consulta o comentario, puedes contactarme a través de [LinkedIn](https://www.linkedin.com/in/nkaminski-profile/) o [GitHub](https://github.com/nicoKaminski).

---

¡Gracias por revisar mi proyecto!