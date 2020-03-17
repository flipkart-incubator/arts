package com.hazelcast.spi;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SimpleMemberAddressProvider implements MemberAddressProvider {

    @Override
    public InetSocketAddress getBindAddress() {
        try {
            String hostName = InetAddress.getByName("127.0.0.1").getHostName();
            return new InetSocketAddress(hostName, 5701);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public InetSocketAddress getPublicAddress() {
        try {
            String hostName = InetAddress.getByName("127.0.0.1").getHostName();
            return new InetSocketAddress(hostName, 5701);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
