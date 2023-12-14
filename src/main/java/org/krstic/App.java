package org.krstic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.krstic.model.Application;

public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        // Welcome message
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Welcome to the time tracker. In this application, "
        + "you can track how much time you have spent using another application " +
        "per session and total time.");
        // User chooses which menu option
        System.out.println("Main Menu: \n\n1. Launch an application \n2. Edit applications\n3. Quit\n\nChoose an option:  ");
        switch (scanner.nextInt()) {
            case 1:
                launchApplications();
                break;
            case 2:
                editApplications();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    private static void launchApplications() throws IOException, InterruptedException {
        List<Application> a = readFile();
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose one of the onboarded apps: ");

        for (int i = 0; i < a.size(); i++) {
            System.out.println(i + ". " + a.get(i).getName());
            System.out.println("Total Time: " + a.get(i).getHours() + " hours and " + a.get(i).getMinutes() + " minutes");
        }

        monitor(a, scan.nextInt());

    }

    // Allows the user to onboard a new app
    // TODO delete apps
    // TODO test successful on board? (maybe)
    private static void editApplications() throws IOException {
        List<Application> a = new ArrayList(readFile());
        Application newApp = new Application();
        Scanner scan = new Scanner(System.in);
        System.out.println("Give the name of the application: ");
        newApp.setName(scan.nextLine());
        System.out.println("Give the path of the .exe to launch (Shortcut properties -> Target)");
        newApp.setDirectory(scan.nextLine());
        System.out.println("Give the name of the exe that runs in tasklist");
        newApp.setExe(scan.nextLine());
        a.add(newApp);
        writeFile(a);
    }


    // Function to read the data from applications.json.
    // TODO This should be changed so that the final executable can read from a file external to the compiled binary.
    private static List<Application> readFile() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/applications.json");
        try {
            List<Application> a = Arrays.asList(mapper.readValue(file, Application[].class));
            return a;
        } catch (Exception e) {
            return new ArrayList<Application>();
        }
    }

    // Writing to applications.json
    private static void writeFile(List<Application> a) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("src/main/resources/applications.json"), a);
    }


    // The method used to monitor if an app is running or not, and tracks the time and updates to file accordingly.
    private static void monitor(List<Application> a, int index) throws IOException, InterruptedException {

        // Here we are removing the example.exe part from the directory to use as an argument later
        String[] str = new String[1];
        // Also, for some reason, Runtime.getRuntime().exec requires that the command be wrapped in an array.
        // TODO This could be made cleaner IMO. Should fix.
        str[0] = a.get(index).getDirectory();
        String[] splitted = str[0].split("\\\\");
        String directory = "";
        for (int i = 0; i < splitted.length - 2; i++) {
            directory +=  splitted[i] + "\\";
        }
        // Here we are using the array and the directory string we came up with earlier.
        Process p = Runtime.getRuntime().exec(str, null, new File(directory));
        // Begin message
        System.out.println("Now tracking: " + a.get(index).getName());
        System.out.println("Total Time: " + a.get(index).getHours() + " hours and " + a.get(index).getMinutes() + " minutes");
        // Wait 10 seconds and then start the clock (in case snafu while starting application)
        TimeUnit.SECONDS.sleep(10);
        long start = System.currentTimeMillis();
        // While the process is alive wait
        while (p.isAlive() || checkIfProcessRunning(a.get(index))) {
            TimeUnit.SECONDS.sleep(3);
        }
        // Calculate new usage time from current session
        // TODO add messaging for current session
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        int seconds = (int) (timeElapsed / 1000);
        int minutes = seconds / 60;
        int newMins = a.get(index).getMinutes() + minutes;
        int newHours = a.get(index).getHours() + (newMins / 60);
        if (newMins > 59) {
            newMins = newMins % 60;
        }
        a.get(index).setMinutes(newMins);
        a.get(index).setHours(newHours);
        System.out.println("Total Time: " + a.get(index).getHours() + " hours and " + a.get(index).getMinutes() + " minutes");
        // Finish and write to file
        writeFile(a);
    }

    // Obtains a list of running processes and checks if the provided app.getExe is in this list.
    private static boolean checkIfProcessRunning(Application a) {
        try {
            String line;
            // Run this windows command to obtain the processes as a CSV without headers
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                // Get the first item in the CSV list for that line (the name)
                String processName = line.split(",")[0];
                if (processName.equalsIgnoreCase("\"" + a.getExe() + "\"")) {
                    return true;
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
        return false;
    }

}
