import java.io.*;

public class ByteFile {

    // Path of a file
    static String currentDirectory = System.getProperty("user.dir");

    static String FILEPATH = currentDirectory + "/test.txt";
    static File file = new File(FILEPATH);

    static byte[] fileToByteArray(String fileName, String fileExtension) throws Exception {
        File file = new File(currentDirectory + "/" + fileName + "." + fileExtension);
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();

        return bytesArray;
    }


    // Method which write the bytes into a file
    static void byteToFile(byte[] bytes, String fileName, String fileExtension) {

        createFile(fileName, fileExtension);

        String FILEPATH = currentDirectory + "/" + fileName + "." + fileExtension;
        File file = new File(FILEPATH);

        System.out.println(currentDirectory);
        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

    }

    public static void createFile(String name, String extension){
        try {
            File myObj = new File(currentDirectory + name + extension);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
