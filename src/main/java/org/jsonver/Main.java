package org.jsonver;
import java.io.*;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        JsonVerification jsonVerification = new JsonVerification();
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the path to the data file: ");
            String filePath = scanner.nextLine();
            jsonVerification.validateJsonResource(filePath);
        } catch(IOException e){
            System.out.println("Error");
        }

    }
}
