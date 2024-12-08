package com.murek.appsocial.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    // permisos para acceder a imágenes
    public static void pedirPermisos(Activity activity, String[] permisos, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisos = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permisos = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        ActivityCompat.requestPermissions(activity, permisos, requestCode);
    }

    public static void openGallery(Context context, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    public static void subirImagenAParse(Context context, Uri imageUri, ImageUploadCallback callback) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
            byte[] bytes = getBytesFromInputStream(inputStream);
            if (bytes == null) {
                callback.onFailure(new Exception("El arreglo de bytes es null"));
                return;
            }

            ParseFile parseFile = new ParseFile("image.jpg", bytes);
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        String imageUrl = parseFile.getUrl();
                        callback.onSuccess(imageUrl);
                    } else {
                        callback.onFailure(e);
                    }
                }
            });
        } catch (IOException e) {
            callback.onFailure(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Leer bytes de un InputStream biblioteca de manejo de flujos de datos
    private static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // Interfaz para manejar el resultado de la subida de imágenes
    public interface ImageUploadCallback {
        void onSuccess(String imageUrl);

        void onFailure(Exception e);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

}// fin
