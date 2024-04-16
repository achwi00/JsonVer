package org.jsonver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.*;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
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
    private String resource;

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

//            jsonNode.fields().forEachRemaining(entry -> {
//                String fieldName = entry.getKey();
//                System.out.println("Field name: " + fieldName);
//                JsonNode fieldValue = entry.getValue();
//                if(fieldValue != null && fieldValue.isArray()){
//
//                }
//                System.out.println("Field Value: " + fieldValue);
//            });
            processJsonNode(jsonNode);
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

    private static void processJsonNode(JsonNode jsonNode) {
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
                System.out.println(fieldValue);
            }
        }

    }
}