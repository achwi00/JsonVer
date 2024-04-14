package org.jsonver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;


import java.io.*;


public class Main
{
    public static void main(String[] args)
    {
        JsonVerification jsonVerification = new JsonVerification();
        //File file = new File("data2.txt");
        //jsonVerification.check(file);
        try{
            jsonVerification.test("data2.txt");
        } catch(IOException e){
            System.out.println("Error");
        }

    }
}
class JsonVerification
{
    private String resource;

    public boolean test(String pathName) throws IOException
    {
        FileReader fileReader = new FileReader(new File(pathName));
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        StringBuilder s = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            s.append(line);
        }
        if (s.length() == 0) {
            System.out.println("The file is empty");
            return false;
        }
        String json = s.toString();

        ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            if (jsonNode != null) {
                System.out.println("is a valid JSON file");
                return true;
            } else {
                System.out.println("is not a valid JSON file");
                return false;
            }
        } catch (MismatchedInputException e) {
            System.out.println("is not a valid JSON file");
            return false;
        } catch (JsonProcessingException e){

            System.out.println("is not a valid JSON file");
            return false;
        }
    }
}