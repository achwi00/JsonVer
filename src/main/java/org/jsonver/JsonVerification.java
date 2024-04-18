package org.jsonver;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class JsonVerification
{
    private String validatedResource = null;
    private boolean foundPolicy = false;
    private boolean foundStatement = false;

    public boolean validateJsonResource(String pathName) throws IOException
    {

        try (FileReader fileReader = new FileReader(pathName)) {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(fileReader);

            //Parse the content to JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonParser);
            jsonParser.close();

            if (jsonNode != null) {

                processJsonNode(jsonNode);
                if(getValidatedResource() == null){
                    System.out.println("No resource field available");
                    return false;
                }
                else{
                    //check if it contains a single asterisk
                    for (int i = 0; i < getValidatedResource().length() ; i++) {
                        if (getValidatedResource().charAt(i) == '*') {
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

            //check if the key is a Resource, assign it to this.Resource
            if(Objects.equals(fieldName, "Resource")) {

                if(isFoundStatement()){
                    if(getValidatedResource() == null){
                        System.out.println(fieldValue);
                        setValidatedResource(String.valueOf(fieldValue));
                    } else{
                        System.out.println("Multiple resource values found");
                        return false;
                    }
                } else{
                    System.out.println("Resource found in an invalid place");
                    return false;
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

                }else System.out.print(fieldValue + "\n");
            }

        }
        return getValidatedResource() != null;
    }

    public String getValidatedResource() {return validatedResource;}
    public void setValidatedResource(String validatedResource) {this.validatedResource = validatedResource;}
    public boolean isFoundPolicy() {return foundPolicy;}
    public void setFoundPolicy(boolean foundPolicy) {this.foundPolicy = foundPolicy;}
    public boolean isFoundStatement() {return foundStatement;}
    public void setFoundStatement(boolean foundStatement) {this.foundStatement = foundStatement;}
}