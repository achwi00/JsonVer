package org.jsonver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


public class Main
{
    public static void main(String[] args)
    {
        JsonVerification jsonVerification = new JsonVerification();
        try{
            jsonVerification.test("data2.txt");
        } catch(IOException e){
            System.out.println("Error");
        }

    }
}
class JsonVerification
{
    private String resource = null;

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public boolean test(String pathName) throws IOException
    {
        FileReader fileReader = new FileReader(pathName);
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
            //return the resource value to a variable
            processJsonNode(jsonNode);

            if(getResource() != null) return false;
            else{
                //check if it contains a single asterisk
                System.out.println("Resource value: ");
                System.out.println(getResource());
            }

            if (jsonNode != null) {
                System.out.println("is a valid JSON file");
                return true;
            } else {
                System.out.println("is not a valid JSON file");
                return false;
            }

        }catch (JsonProcessingException e){
            System.out.println("is not a valid JSON file");
            return false;
        }
    }

    private void processJsonNode(JsonNode jsonNode) {
        System.out.println("\nNEW ITERRATION");
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();

        boolean foundPolicy = false;
        boolean foundStatement = false;
        while (fieldsIterator.hasNext()) {

            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();
            System.out.println("Field Name: " + fieldName + ", Field Value: ");
            System.out.println("Field Value Type: " + fieldValue.getNodeType());

            if (fieldValue.isObject()) {
                if(Objects.equals(fieldName, "PolicyDocument")){
                    System.out.println("found policy document");
                    foundPolicy = true;
                }

                processJsonNode(fieldValue);
            }else if(fieldValue.isArray()){
                if(Objects.equals(fieldName, "Statement") && foundPolicy){
                    foundStatement = true;
                    System.out.println("found statement");
                }


                for (JsonNode element : fieldValue) {
                    processJsonNode(element);
                }

            }
            else {
                // Print field name and value
                if(Objects.equals(fieldName, "Resource"))
                {
                    System.out.println("Resource value in method: " + String.valueOf(fieldValue));
                    setResource(String.valueOf(fieldValue));
                }
                System.out.println(fieldValue);
            }
        }
    }
}