package com.jin.myrpc.spirngboot.registry;

/**
 * @author wangjin
 */
public class URL {
    private String serverAddress;
    private String serverPort;

    @Override
    public String toString() {
        return "URL{" +
                "serverAddress='" + serverAddress + '\'' +
                ", serverPort='" + serverPort + '\'' +
                '}';
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}
