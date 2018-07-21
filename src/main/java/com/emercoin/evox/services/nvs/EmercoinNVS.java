package com.emercoin.evox.services.nvs;

import com.emercoin.evox.services.rpc.RpcService;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EmercoinNVS {

    RpcService rpcService;

    Gson gson = new Gson();

    public EmercoinNVS() {
        rpcService = RpcService.getInstance();
    }

    public EmercoinNVS(RpcService rpcService) {
        this.rpcService = rpcService;
    }

    String addNewObjectValue(String prefix, String key, Object value, int days) {
        return addNewValue(prefix, key, gson.toJson(value), days);
    }

    public String addNewValue(String prefix, String key, String value, int days) {
        return addNewValue(prefix + ":" + key, value, days);
    }

    String addNewValue(String key, String value, int days) {
        String responce = "";
        try {
            value = value.replace("\"","\\\"");
            responce = rpcService.request("name_new", "[\"" + key + "\",\"" + value + "\"," + days + "]");

            Map<String, Object> resp = gson.fromJson(responce, Map.class);
            return String.valueOf(resp.get("result"));
        } catch (Exception e) {
            return responce;
        }
    }

    List<RecordDTO> filterByPrefix(String prefix) {
        try {
            String responce = rpcService.request("name_filter", "[\"" + prefix + "\"]");

            NameFilterDTO resp = gson.fromJson(responce, NameFilterDTO.class);
            return resp.getResult();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> getAllByPrefix(String prefix) {
        List<RecordDTO> records = filterByPrefix(prefix);
        List<String> result = new ArrayList<>();
        for (RecordDTO recordDTO : records) {
            result.add(nameShow(recordDTO.name));

        }
        return result;
    }

    public String nameShow(String name) {
        try {
            String responce = rpcService.request("name_show", "[\"" + name + "\",\"hex\"]");
//            System.out.println(responce);
            NameShowDTO resp = gson.fromJson(responce, NameShowDTO.class);
            String hexString = resp.getResult().getValue();
            byte[] bytes = Hex.decodeHex(hexString.toCharArray());

            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            return "";
        }


    }

    public void setRpcService(RpcService rpcService) {
        this.rpcService = rpcService;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        EmercoinNVS emercoinNVS = new EmercoinNVS();

        List<String> result = emercoinNVS.getAllByPrefix("evote");
        for (String record : result) {
            System.out.println(record);
        }
    }

    class NameFilterDTO {
        List<RecordDTO> result;
        String error;
        Long id;

        public List<RecordDTO> getResult() {
            return result;
        }

        public void setResult(List<RecordDTO> result) {
            this.result = result;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    class NameShowDTO {
        RecordDTO result;
        String error;
        Long id;

        public RecordDTO getResult() {
            return result;
        }

        public void setResult(RecordDTO result) {
            this.result = result;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    class RecordDTO {
        String name;
        String value;
        String txid;
        String address;
        Long registered_at;
        Long expires_in;
        Long time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Long getRegistered_at() {
            return registered_at;
        }

        public void setRegistered_at(Long registered_at) {
            this.registered_at = registered_at;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }
}
