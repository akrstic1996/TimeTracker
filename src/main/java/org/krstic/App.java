package org.krstic;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Desktop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.krstic.model.Application;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Welcome to the time tracker. In this application, "
        + "you can track how much time you have spent using another application " +
        "per session and total time.");
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

    private static void launchApplications() throws IOException {
        List<Application> a = readFile();
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose one of the onboarded apps: ");

        for (int i = 0; i < a.size(); i++) {
            System.out.println(i + ". " + a.get(i).getName());
            System.out.println("Total Time: " + a.get(i).getHours() + " hours and " + a.get(i).getMinutes() + " minutes");
        }

        monitor(a.get(scan.nextInt()));

    }

    private static void editApplications() throws IOException {
        List<Application> a = new ArrayList(readFile());
        Application newApp = new Application();
        Scanner scan = new Scanner(System.in);
        System.out.println("Give the name of the application: ");
        newApp.setName(scan.nextLine());
        System.out.println("Give the directory of the .exe");
        newApp.setDirectory(scan.nextLine());
        a.add(newApp);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("src/main/resources/applications.json"), a);
    }

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

    private static void monitor(Application a) throws IOException {
        //Runtime.getRuntime().exec();
        System.out.println("Now tracking: " + a.getName());

    }
}
