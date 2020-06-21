import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Node {

    public static void upload(String ip, int id, String bytes, String deletionCode, int size) throws IOException, ParseException {
        String masterNodeIp = "http://" + ip + "/uploadFile?fileID=" + id + "&bytes=" + bytes + "&sizeInBytes=" + size + "&deletionCode=" + deletionCode;

        System.out.println(masterNodeIp);

        URL master = new URL(masterNodeIp);
        //-----------------
        HttpURLConnection httpCon = (HttpURLConnection) master.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");
        OutputStreamWriter out = new OutputStreamWriter(
                httpCon.getOutputStream());
        System.out.println(httpCon.getResponseCode());
        System.out.println(httpCon.getResponseMessage());
        out.close();
    }

    public static JSONObject download(String ip, String id, String deletionCode) throws IOException, ParseException {
        String masterNodeIp = "http://" + ip + "/downloadFile?fileID=" + id + "&deletionCode=" + deletionCode;

        URL master = new URL(masterNodeIp);

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
