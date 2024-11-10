# Proyecto appSocial

## Descripción

Importante!: la app no esta terminada y es un proyecto universitario desarrollado para la asignatura "Desarrollo Mobile"
appSocial es una aplicación móvil de tipo social. Actualmente, la funcionalidad específica de la aplicación aún no ha sido detallada, pero esta cuenta con una estructura de menús y opciones que permiten explorar la aplicación. Entre sus características principales, appSocial incluye un menú de navegación, una página de inicio de sesión y registro, y una página para gestionar usuarios con opciones de creación, actualización, eliminación y visualización de usuarios.

## Tecnologías Principales

- **Java**: Lenguaje de programación principal de la aplicación.
- **Android Studio**: IDE utilizado para el desarrollo de la aplicación.
- **MVVM**: Arquitectura empleada para estructurar el proyecto.
- **Fragments y Activities**: Utilizados para estructurar las diferentes pantallas y secciones de la aplicación.
- **Firebase Authentication**: Para la autenticación de usuarios.

## Funcionalidades Principales

1. **Menú de Navegación**: Un menú de navegación inferior con las siguientes opciones:
   - **Home**: Página de inicio de la aplicación.
   - **Perfil**: Pantalla donde el usuario puede ver y editar su perfil.
   - **Chats**: Página para iniciar y gestionar conversaciones.
   - **Filtros**: Opciones de filtrado de contenido.

2. **Vista de Login**: La vista principal de la aplicación es una página de inicio de sesión. Los usuarios pueden iniciar sesión si ya tienen una cuenta registrada. En caso de no estar registrados, son dirigidos a la página de registro.

3. **Registro de Usuario**: Páginas de registro e inicio de sesión para usuarios no registrados, implementadas mediante Firebase Authentication.

34 **Gestión de Usuarios**: En la página de gestión de usuarios, hay cuatro botones:
   - **Leer**: Muestra información de los usuarios registrados.
   - **Actualizar**: Permite actualizar los datos de un usuario.
   - **Eliminar**: Permite eliminar a un usuario.
   - **Crear**: Crea un nuevo usuario en el sistema.
  
## Estructura del Proyecto

La estructura del proyecto sigue el patrón MVVM (Model-View-ViewModel) y está organizada en diferentes paquetes según el propósito de cada clase.

### `/main/java/com/murek/appsocial`

#### `/model`
- **User.java**: Define la clase de usuario con sus atributos y métodos.

#### `/providers`
- **AuthProvider.java**: Proveedor de autenticación para la gestión de inicio de sesión y registro.
- **UserProvider.java**: Proveedor para realizar operaciones de CRUD con los datos de usuario (pendiente de implementación en Firebase).

#### `/util`
- **Validaciones.java**: Contiene funciones para validar los datos de entrada en los formularios, como correos electrónicos, contraseñas, etc.

#### `/view`
- **MainActivity.java**: Activity principal que gestiona la navegación entre las secciones de la app.
- **RegisterActivity.java**: Activity para el registro de usuarios.
- **UserActivity.java**: Activity para la gestión de usuarios, donde se puede crear, leer, actualizar y eliminar usuarios.

#### `/view/fragments`
- **HomeFragment.java**: Fragmento que muestra la pantalla de inicio.
- **FiltrosFragment.java**: Fragmento donde se gestionan los filtros.
- **ChatsFragment.java**: Fragmento para gestionar las conversaciones.
- **PerfilFragment.java**: Fragmento para ver y editar el perfil de usuario.

#### `/viewModel`
- **HomeViewModel.java**: ViewModel para manejar la lógica de la pantalla de inicio.
- **MainViewModel.java**: ViewModel que facilita la interacción entre el modelo y la vista principal.
- **RegisterViewModel.java**: ViewModel que gestiona la lógica de registro de usuario.
- **UserViewModel.java**: ViewModel para la lógica de gestión de usuario.

### `/res/layout`
- **activity_main.xml**: Layout de la pantalla principal.
- **activity_register.xml**: Layout para el registro de usuarios.
- **activity_user.xml**: Layout de gestión de usuarios.
- **activity_home.xml**: Layout para la pantalla de inicio.
- **activity_chats.xml**: Layout para la sección de chats.
- **activity_filtros.xml**: Layout para la sección de filtros.
- **activity_perfil.xml**: Layout para el perfil de usuario.

### `/res/menu`
- **bottom_navigation_menu.xml**: Menú de navegación inferior con opciones para **Home**, **Perfil**, **Chats** y **Filtros**.

## Notas Importantes

- **Firebase Authentication**: La autenticación de usuarios se realiza mediante Firebase Authentication. Actualmente, los datos de usuario solo se registran en Firebase Authentication, pero se tiene planeado implementar el uso de Firebase para almacenar otros datos de usuario en una base de datos.
- **Validaciones de Usuario**: Es importante implementar validaciones adecuadas para el registro y la actualización de los datos de usuario para asegurar la integridad de la información.
- **Gestión de Estados**: Dado que la aplicación usa MVVM, los cambios en el modelo se reflejan automáticamente en la vista, mejorando la respuesta de la interfaz y simplificando el manejo de estados.

## Contacto

Para cualquier consulta o comentario, puedes contactarme a través de [LinkedIn](https://www.linkedin.com/in/nkaminski-profile/) o [GitHub](https://github.com/N-Kaminski).

---

¡Gracias por revisar mi proyecto!
