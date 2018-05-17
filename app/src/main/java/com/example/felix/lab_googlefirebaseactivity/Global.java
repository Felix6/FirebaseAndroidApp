package com.example.felix.lab_googlefirebaseactivity;

import android.app.Application;

import java.util.ArrayList;

public class Global extends Application {

    public static ArrayList<Bands> bandsArray = new ArrayList<>();
    public static Integer bandsArrayIndexPosition = 0;
    public static Boolean isGetRecord = false;

}
