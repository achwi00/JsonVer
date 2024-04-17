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
    private boolean foundResource = false;
    private boolean foundPolicy = false;
    private boolean foundStatement = false;

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

            if (jsonNode != null) {
                processJsonNode(jsonNode);
                String res = getResource();
                if(getResource() == null){
                    System.out.println("No resource field available");
                    return false;
                }
                else{
                    //check if it contains a single asterisk
                    for (int i = 0; i < res.length() ; i++) {
                        if (res.charAt(i) == '*') {
                            System.out.println("Contains a single asterisk!");
                            return false;
                        }
                    }
                    return true;
                }

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
                if(isFoundResource()){
                    return false;
                } else{
                    setFoundResource(true);
                    if(isFoundStatement()){
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
                        setFoundPolicy(true);
                    }
                    processJsonNode(fieldValue);
                }
                else if(fieldValue.isArray()){
                    if(Objects.equals(fieldName, "Statement") && isFoundPolicy()){
                        setFoundStatement(true);
                    }

                    for (JsonNode element : fieldValue) {
                        if (element.isTextual()) {
                            System.out.println(element);
                        }else{
                            processJsonNode(element);
                        }

                    }

                }else System.out.println(fieldValue);
            }

        }
        return getResource() != null;
    }

    public String getResource() {return resource;}
    public void setResource(String resource) {this.resource = resource;}
    public boolean isFoundResource() {return foundResource;}
    public void setFoundResource(boolean foundResource) {this.foundResource = foundResource;}
    public boolean isFoundPolicy() {return foundPolicy;}
    public void setFoundPolicy(boolean foundPolicy) {this.foundPolicy = foundPolicy;}
    public boolean isFoundStatement() {return foundStatement;}
    public void setFoundStatement(boolean foundStatement) {this.foundStatement = foundStatement;}
}