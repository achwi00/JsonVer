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
    //those should be class attributes:
    private boolean foundResource = false;
    private boolean foundPolicy = false;
    private boolean foundStatement = false;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean test(String pathName) throws IOException {

        try (FileReader fileReader = new FileReader(pathName);
             BufferedReader reader = new BufferedReader(fileReader)) {

            ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            if (stringBuilder.length() == 0) {
                System.out.println("The file is empty");
                return false;
            }

            // Convert directly to JsonNode without converting to String
            JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
            processJsonNode(jsonNode);
            
            if(getResource() == null){
                System.out.println("No resource field available");
                return false;
            }
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

    private boolean processJsonNode(JsonNode jsonNode) {
        System.out.println("\nNEW ITERRATION");
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();

        while (fieldsIterator.hasNext()) {

            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();
            System.out.println("Field Name: " + fieldName + ", Field Value: ");
            System.out.println("Field Value Type: " + fieldValue.getNodeType());

            //check if the key is a Resource, assign it to this.Resource
            if(Objects.equals(String.valueOf(fieldName), "Resource")) {
                if(foundResource){
                    return false;
                } else{
                    this.foundResource = true;
                    if(foundStatement){
                        if(getResource() == null){
                            setResource(String.valueOf(fieldValue));
                        } else{
                            System.out.println("Multiple resource values found");
                            return false;
                        }
                    } else{
                        System.out.println("Resource found in an invalid place");
                        return false;
                    }
                }

            }
            //if it is not Resource
            else {
                if (fieldValue.isObject()) {
                    if(Objects.equals(fieldName, "PolicyDocument")){
                        System.out.println("found policy document");
                        this.foundPolicy = true;
                    }
                    processJsonNode(fieldValue);
                }else if(fieldValue.isArray()){
                    if(Objects.equals(fieldName, "Statement") && foundPolicy){
                        this.foundStatement = true;
                        System.out.println("found statement");
                    }

                    for (JsonNode element : fieldValue) {
                        if (element.isTextual()) {
                            System.out.println(element);
                        }else{
                            processJsonNode(element);
                        }

                    }

                }else{
                    System.out.println(fieldValue);
                }
            }

        }
        if(getResource() != null) return true;
        else return false;
    }
}