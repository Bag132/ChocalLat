package com.cornelius;

// %HOMEDRIVE%%HOMEPATH%
// Zombie Spawner Coordinates: X = -150, Y = 21, Z = 330
class Main {
    public static final String version = "Unreleased But Released";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String... cheese) throws Shit {
        finnaYeet();
    }

    private static void finnaYeet() {
        GUI.getInstance().createSetupFrame();
    }

    private static class Shit extends Exception {
        public Shit() {
            super("AAAAAAAAAAAAA");
        }
    }

}
