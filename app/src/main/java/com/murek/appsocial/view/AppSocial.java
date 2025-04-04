package com.murek.appsocial.view;

import android.app.Application;

import com.murek.appsocial.BuildConfig;// Si esta importación falla, la corregiremos
import com.murek.appsocial.model.Post;
import com.murek.appsocial.model.User;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

public class AppSocial extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Configuración de Parse Server
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);

        // Inicializa Parse con los valores de BuildConfig
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.BACK4APP_APP_ID)
                .clientKey(BuildConfig.BACK4APP_CLIENT_KEY)
                .server(BuildConfig.BACK4APP_SERVER_URL)
                .build());

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(false);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}