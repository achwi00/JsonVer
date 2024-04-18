import org.jsonver.JsonVerification;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonVerificationTest
{
    private static final String fileName = "test_file.txt";
    private static String currentContent;

    @BeforeClass
    public static void createTempFile() throws IOException {
        File tempFile = File.createTempFile(fileName, ".txt");
        tempFile.deleteOnExit();
    }

//    @Before
//    public void updateTestContent(String content) throws IOException
//    {
//        currentContent = content;
//
//    }

    @Test
    public void testValidResource_ReturnTrue() throws IOException
    {
        String content = "{\"PolicyName\":\"root\",\"PolicyDocument\":{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"IamListAccess\",\"Effect\":\"Allow\",\"Action\":[\"iam:ListRoles\",\"iam:ListUsers\"],\"Resource\":\"validResource\"}]}}";
        Files.writeString(Paths.get(fileName), content);
        JsonVerification jsonVerification = new JsonVerification();
        assertTrue(jsonVerification.validateJsonResource(fileName));
    }
    @Test
    public void testValidArrayResource_ReturnTrue(){

    }


    @Test
    public void testExistingResourceWithAsterisk_ReturnFalse() throws IOException
    {
        String content = "{\"PolicyName\":\"root\",\"PolicyDocument\":{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"IamListAccess\",\"Effect\":\"Allow\",\"Action\":[\"iam:ListRoles\",\"iam:ListUsers\"],\"Resource\":\"*\"}]}}";
        Files.writeString(Paths.get(fileName), content);
        JsonVerification jsonVerification = new JsonVerification();
        assertFalse(jsonVerification.validateJsonResource(fileName));
    }
    @Test
    public void testEmptyFile_ReturnFalse(){

    }
    @Test
    public void testMissingResource_ReturnFalse(){

    }
    @Test
    public void testResourceWrongLocation_ReturnFalse(){

    }

}
