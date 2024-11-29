package com.murek.appsocial.util;

public class Validaciones {

    public static boolean validarUsuario(String usuario) {
        return usuario != null && !usuario.isEmpty() && usuario.length() >3;
    }

    public static boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex) &&email.contains("@");
    }

    public static String validarPassword(String password, String password2) {
        if(password == null || password.isEmpty() || password2 == null || password2.isEmpty()) {
            return "Las contraseñas no pueden estar vacias";
        }
        if(password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }
        if(!password.equals(password2)) {
            return "Las contraseñas no coinciden";
        }
        return null; // contraseña válida
    }

    public static boolean controlarPassword(String password) {
        return (password != null && password.length() >= 8);
    }

    public static boolean validarTexto(String texto) {
        return texto != null && !texto.isEmpty();
    }

    public static boolean validarNumero(String numero) {
        return numero != null && !numero.isEmpty() && numero.matches("[0-9]+");
    }
}
