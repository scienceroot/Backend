package com.scienceroot.repository;

public class DataRequestBody {

    public byte[] data;
    public String privateKey;

    public DataRequestBody() {
    }

    public DataRequestBody(byte[] data, String privateKey) {
        this.data = data;
        this.privateKey = privateKey;
    }
    
    
}
