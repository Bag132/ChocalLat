package com.cornelius;

import java.io.IOException;

class Main {
    public static final String version = "Alpha";
    public static Location userLocation = Location.SELECT_TYPE;

    public static void main(String... cheese) throws InterruptedException, IOException {
        finnaYeet();
    }

    public static void finnaYeet() {
        GUI.getInstance().createSetupFrame();
    }


}
