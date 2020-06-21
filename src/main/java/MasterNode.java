import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MasterNode {

    //With http:// or https://
    public static String masterNodeIp = "http://localhost:8080";

    public static JSONObject getNodes(int numOfSplits, int numOfBytesPerSplit) throws IOException, ParseException {
        String masterNodeRequest = masterNodeIp + "/getNodeIpForNewFile?sizeOfEachSplitInBytes="+numOfBytesPerSplit+"&numOfSplits=" + numOfSplits;

        URL master = new URL(masterNodeRequest);

        org.json.simple.JSONObject json = new org.json.simple.JSONObject();
        JSONParser parser = new JSONParser();

        URLConnection masterConnection = master.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        masterConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            json = (org.json.simple.JSONObject) parser.parse(inputLine);
        in.close();

        return json;

    }

    public static JSONObject setUpFile(String main, String backup, int size) throws IOException, ParseException {
        String masterNodeRequest = masterNodeIp + "/setUpFile?main=" + main + "&backup=" + backup + "&size=" + size;

        URL master = new URL(masterNodeRequest);

        org.json.simple.JSONObject json = new org.json.simple.JSONObject();
        JSONParser parser = new JSONParser();

        URLConnection masterConnection = master.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        masterConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            json = (org.json.simple.JSONObject) parser.parse(inputLine);
        in.close();

        return json;
    }

    public static JSONObject getNode(String id) throws IOException, ParseException {
        String masterNodeRequest = masterNodeIp + "/getNodeIp?fileId=" + id;

        URL master = new URL(masterNodeRequest);

        org.json.simple.JSONObject json = new org.json.simple.JSONObject();
        JSONParser parser = new JSONParser();

        URLConnection masterConnection = master.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        masterConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            json = (org.json.simple.JSONObject) parser.parse(inputLine);
        in.close();

        return json;
    }

}
