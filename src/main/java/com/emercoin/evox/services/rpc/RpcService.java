package com.emercoin.evox.services.rpc;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;


public class RpcService {

    Gson gson = new Gson();

    private static RpcService rpcService = null;

    int idcounter = 1;
    String protocol = "http";
    String username = "user123";
    String password = "password123";
    String host = "localhost";
    String port = "6662";
    String path = "/";

    private RpcService() {
    }

    public static RpcService getInstance() {
        if (rpcService == null) rpcService = new RpcService();
        return rpcService;
    }

    public String request(String method) {
        return request(method, "[]");
    }

    public String request(String method, Object params) {
        return request(method, gson.toJson(params));
    }

    public String request(String method, String params) {
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(protocol + "://" + host + ":" + port + path);
            String urlParameters = "{\"jsonrpc\": \"2.0\", \"method\": \"" + method + "\", \"params\": " + params + ", \"id\": " + (idcounter++) + "}";
//            System.out.println(url);
//
//            System.out.println(urlParameters);

            byte[] postData = urlParameters.getBytes();
            int postDataLength = postData.length;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            String authorization = username + ":" + password;
            String encodedAuthorization = new String(Base64.getEncoder().encode(authorization.getBytes()));
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
//                System.out.println(inputLine);
            }
            in.close();

            conn.disconnect();

        } catch (IOException e) {
//            e.printStackTrace();
            content.append(e.getMessage());
        }

        return content.toString();
    }

    public Map<String, Object> requestMap(String method, Object params) {
        Map<String, Object> responce = gson.fromJson(request(method, params), Map.class);
        return responce;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public static void main(String[] args) {
        RpcService rpcService = new RpcService();
        String responce = rpcService.request("name_new", Arrays.asList("kluch", "znachenie", 45));
        System.out.println(responce);
    }
}
