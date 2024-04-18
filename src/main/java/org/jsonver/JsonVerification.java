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

    public boolean validateJsonResource(String pathName) throws IOException {
        try (FileReader fileReader = new FileReader(pathName)) {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(fileReader);

            //parse the content to JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonParser);
            jsonParser.close();
            //if jsonNode is not empty, process it
            if (jsonNode != null) {

                processJsonNode(jsonNode);
                //if the resource field is empty, there was no valid resource field available
                if(getValidatedResource() == null){
                    System.out.println("No resource field available.");
                    return false;
                }
                else{
                    //check if the resource contains a single asterisk
                    for (int i = 0; i < getValidatedResource().length() ; i++) {
                        if (getValidatedResource().charAt(i) == '*') {
                            System.out.println("Contains a single asterisk.");
                            return false;
                        }
                    }
                    //if it does not contain a single asterisk, return true
                    System.out.println("Valid resource: " + getValidatedResource());
                    return true;
                }

            } else {
                System.out.println("Is not a valid JSON file");
                return false;
            }

        }catch (JsonProcessingException e){
            System.out.println("Is not a valid JSON file");
            return false;
        }
    }

    private boolean processJsonNode(JsonNode jsonNode) {
        //iterate through the node
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();

        while (fieldsIterator.hasNext()) {

            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();

            //check if the key is a Resource
            if(Objects.equals(fieldName, "Resource")) {
                //assign it to this.Resource
                if(isFoundStatement()){
                    if(getValidatedResource() == null){
                        setValidatedResource(String.valueOf(fieldValue));
                    } else{
                        System.out.println("Multiple resource values found.");
                        return false;
                    }
                } else{
                    //if the statement was not found yet, the resource is not in the right place
                    System.out.println("Resource found in an invalid place.");
                    return false;
                }

            }
            //if the key is not Resource
            else {
                //check the key value is an object
                if (fieldValue.isObject()) {
                    if(Objects.equals(fieldName, "PolicyDocument")){
                        //if the policy document is found, change the according flag
                        setFoundPolicy(true);
                    }
                    processJsonNode(fieldValue);
                }//check if the field value is an array
                else if(fieldValue.isArray()){
                    if(Objects.equals(fieldName, "Statement") && isFoundPolicy()){
                        //if the statement field is found, and policy was found before, change the according flag
                        setFoundStatement(true);
                    }
                    //recursively process each element
                    for (JsonNode element : fieldValue) {
                        processJsonNode(element);
                    }

                }
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