import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import java.util.Random;

public class main {

    public static void main(String[] args) throws Exception {
        int numOfSplits = 3;
        int numOfBytesPerSplit = 10;

        MasterNode.masterNodeIp = "http://localhost:8080";

        String[] keys = new String[numOfSplits];

        ByteFile.fileToByteArray("logo", "png");

        byte[] bytes = ByteFile.fileToByteArray("logo", "png");

        byte[][] splitBytes = SplitByteArray.splitByteArray(bytes);

        //TODO Encrypt splitBytes

        splitBytes = encryptSplitBytes(numOfSplits,splitBytes, keys);

        //TODO Ask masternode for open nodes' ips

        String[] ipArray = getAvailableNodesIp(numOfSplits, numOfBytesPerSplit);

        //TODO Tell master node to create a document with the backup and m main node for each byte array

        int[] ids = setUpFileWithMasterNode(numOfSplits, ipArray, numOfBytesPerSplit);

        //TODO Create a key file with the unique number for the file, and the keys for each part of the file.

        //TODO Send each split byte array and file unique number to a node

        String[][] deletionCodes = uploadDataToNodes(numOfSplits, splitBytes, ids, ipArray, numOfBytesPerSplit);

        retrieveFileFromNodes("picture", "png", numOfSplits, ids, deletionCodes[0], deletionCodes[1], keys);


    }

    public static byte[][] encryptSplitBytes(int numOfSplits, byte[][] splitBytes, String[] keys){
        byte[][] encryptedSplitBytes = new byte[numOfSplits][];

        int i = 0;
        for(byte[] b : splitBytes){
            encryptedSplitBytes[i] = Encryption.encryptByteArray(b, keys[i]);
            i++;
        }

        return  encryptedSplitBytes;
    }

    public static String[] getAvailableNodesIp(int numOfSplits, int numOfBytesPerSplit) throws IOException, ParseException {
        JSONObject json = MasterNode.getNodes(numOfSplits, numOfBytesPerSplit);

        String ipString = json.get("nodes").toString();
        String[] ipArray = ipString.split(" ");
        return ipArray;
    }

    //Returns the ids
    public static int[] setUpFileWithMasterNode(int numOfSplits, String[] ipArray, int numOfBytes) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        int[] ids = new int[numOfSplits];
        for(int i = 0; i < numOfSplits*2; i +=2){
            json = MasterNode.setUpFile(ipArray[i], ipArray[i+1], numOfBytes);
            //System.out.println(json.toJSONString());
            ids[i/2] = Integer.valueOf(json.get("id").toString());
        }
        return ids;
    }

    //Returns deltion codes for both main and backup out[0] = main deletion codes out[1] = backup deletion codes.
    public static String[][] uploadDataToNodes(int numOfSplits, byte[][] splitBytes, int[] ids, String[] ipArray, int numOfBytes) throws IOException, ParseException {
        String[][] fileData = new String[numOfSplits][3];

        String[] deletionCodes = new String[numOfSplits];
        String[] deletionCodeBackups = new String[numOfSplits];

        for (int i = 0; i < numOfSplits * 2; i += 2) {
            String main = ipArray[i];
            String backup = ipArray[i + 1];

            Random rand = new Random();
            String deletionCode = rand.nextInt(1000000000) + "";
            String deletionCodeBackup = rand.nextInt(1000000000) + "";

            deletionCodes[i/2] = deletionCode;
            deletionCodeBackups[i/2] = deletionCodeBackup;

            fileData[i / 2][0] = ids[i / 2] + "";
            fileData[i / 2][1] = deletionCode;
            fileData[i / 2][2] = deletionCodeBackup;

            String base64 = Base64.encode(splitBytes[i / 2]);
            System.out.println(base64);

            Node.upload(main, ids[i / 2], base64, deletionCode, numOfBytes);
            Node.upload(backup, ids[i / 2], base64, deletionCodeBackup, numOfBytes);
        }

        String[][] out = new String[2][];

        out[0] = deletionCodes;
        out[1] = deletionCodeBackups;
        return out;
    }

    public static void retrieveFileFromNodes(String fileName, String fileExtension, int numOfSplits, int[] ids, String[] deletionCodes, String[] deletionCodeBackups, String[] keys) throws IOException, ParseException {
        JSONObject[] jsonObjects = new JSONObject[numOfSplits];
        JSONObject[] base64JSON = new JSONObject[numOfSplits];

        byte[][] downloadedBytes = new byte[numOfSplits][];

        for(int i = 0; i < numOfSplits; i++){
            jsonObjects[i] = MasterNode.getNode(ids[i] + "");
            base64JSON[i] = Node.download(jsonObjects[i].get("main").toString(), ids[i] + "", deletionCodes[i]);
            downloadedBytes[i] = Base64.decode(base64JSON[i].get("Base64").toString().replaceAll(" ", "+"));
        }

        int size = 0;
        int i = 0;
        for(byte[] b : downloadedBytes){
            Decryption.decryptByteArray(b, keys[i]);
            for(byte by : b){
                size++;
            }
            i++;
        }

        byte[] finalBytes = new byte[size];
        i = 0;
        for(byte[] b : downloadedBytes){
            for(byte by : b){
                finalBytes[i] = by;
                i++;
            }
        }

        ByteFile.byteToFile(finalBytes, fileName, fileExtension);
    }


}
