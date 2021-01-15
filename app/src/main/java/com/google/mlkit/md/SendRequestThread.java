package com.google.mlkit.md;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendRequestThread implements Runnable{
    Thread t;

    public static final String USER_AGENT = "Mozilla/5.0";
    public static String url;
    public static String password;
    String postJsonData;
    public boolean success;
    public String error;

    public SendRequestThread(String content){
        JSONObject obj = new JSONObject();
        try{
            obj.put("content",content);
            obj.put("password",password);
            this.postJsonData = obj.toString();
            this.success = true;
        }
        catch (JSONException e){
            error = "Invalid Content";
            this.success = false;
        }
//        this.postJsonData = "{\"content\":\""+content +"\",\"password\":\""+password+"\"}";
    }

    public static int setUrlPassword(String input){
        try {
            JSONObject reader = new JSONObject(input);
            SendRequestThread.password = reader.getString("password");
            JSONArray arr = reader.getJSONArray("url");
            for (int i = 0; i < arr.length(); i++) {
                SendRequestThread.url="http://"+arr.getString(i)+":8080/post-barcode";
                System.out.println(SendRequestThread.url);
                if((new SendRequestThread("")).start()){
                    return 0;
                }
            }
            SendRequestThread.url = null;
            SendRequestThread.password = null;
            return 1;
        }catch (JSONException e){
            SendRequestThread.url = null;
            SendRequestThread.password = null;
            return 2;
        }
    }

    @Override
    public void run(){
        if(this.success==false){
            return;
        }
        success = sendPostRequest();
    }

    public boolean start () {
        if (t == null) {
            try{
                t = new Thread (this, "PairDevice");
                t.start ();
                t.join();
                return success;
            }catch (InterruptedException e){
                error = "Connection Interrupted";
                return false;
            }
        }
        return false;
    }

    // HTTP Post request
    public boolean sendPostRequest(){
        if(url == null){
            error="Device Not Paired";
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
            con.setConnectTimeout(1000);

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

                error = response.toString();
                if(response.toString().equals("Success")){
                    return true;
                }
                return false;
            }
            error = "Connection Unsuccessful : "+responseCode;
            return false;
        }
        catch(MalformedURLException e){
            error = e.getClass().getSimpleName();
            return false;
        }
        catch (IOException e){
            error = e.getClass().getSimpleName()+"\nEnsure that devices are on the same network and Pair them again";
            return false;
        }
    }
}