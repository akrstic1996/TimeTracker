package org.krstic.model;

public class Application {

    private String name;
    private String exe;
    private String directory;
    private int hours = 0;
    private int minutes = 0;

    private int lastSessionMinutes = 0;

    private int lastSessionHours = 0;

    public int getLastSessionMinutes() {
        return lastSessionMinutes;
    }

    public void setLastSessionMinutes(int lastSessionMinutes) {
        this.lastSessionMinutes = lastSessionMinutes;
    }

    public int getLastSessionHours() {
        return lastSessionHours;
    }

    public void setLastSessionHours(int lastSessionHours) {
        this.lastSessionHours = lastSessionHours;
    }

    public String getExe() {
        return exe;
    }

    public void setExe(String exe) {
        this.exe = exe;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }


    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

}
