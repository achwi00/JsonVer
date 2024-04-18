package org.jsonver;
import java.io.*;

public class Main
{
    public static void main(String[] args)
    {
        JsonVerification jsonVerification = new JsonVerification();
        try{
            jsonVerification.validateJsonResource("data2.txt");
        } catch(IOException e){
            System.out.println("Error");
        }

    }
}
