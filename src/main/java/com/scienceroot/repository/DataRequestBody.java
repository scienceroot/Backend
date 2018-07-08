package com.scienceroot.repository;

public class DataRequestBody {

    public String key;
    public byte[] data;
    public String privateKey;

    public DataRequestBody() {
    }

    public DataRequestBody(byte[] data, String privateKey) {
        this.data = data;
        this.privateKey = privateKey;
    }
    
    public DataRequestBody(String key, byte[] data, String privateKey) {
        this.key = key;
        this.data = data;
        this.privateKey = privateKey;
    }
}
