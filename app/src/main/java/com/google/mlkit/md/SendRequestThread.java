package com.google.mlkit.md;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequestThread implements Runnable{
    Thread t;

    public static final String USER_AGENT = "Mozilla/5.0";
    private static String url;
    private static String password;
    String postJsonData;
    public boolean success;

    public SendRequestThread(String content){
        this.postJsonData = "{\"content\":\""+content +"\",\"password\":\""+password+"\"}";
        this.success = false;
    }

    public static boolean setUrlPassword(String input){
        try {
            JSONObject reader = new JSONObject(input);
            SendRequestThread.url = reader.getString("url");
            SendRequestThread.password = reader.getString("password");
            return true;
        }catch (Exception e){
            SendRequestThread.url = null;
            SendRequestThread.password = null;
            return false;
        }
    }

    @Override
    public void run(){
        try  {
            success = sendPostRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean start () {
        if (t == null) {
            try{
                t = new Thread (this, "PairDevice");
                t.start ();
                t.join();
                return success;
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }

    // HTTP Post request
    public boolean sendPostRequest(){
        if(url == null){
            return false;
        }
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Setting basic post request
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type","application/json");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if(responseCode>=200 && responseCode<300){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String output;
                StringBuffer response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();

                if(response.toString().equals("Success")){
                    return true;
                }
                return false;
            }
            return false;
        }catch(Exception e){
            return false;
        }

    }
}