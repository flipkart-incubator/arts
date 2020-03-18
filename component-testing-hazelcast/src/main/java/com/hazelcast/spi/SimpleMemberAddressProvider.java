package com.hazelcast.spi;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * In this class we will pass local hostname instead of ip address incase of VPN connections
 */
public class SimpleMemberAddressProvider implements MemberAddressProvider {

    @Override
    public InetSocketAddress getBindAddress() {
        try {
            return new InetSocketAddress(InetAddress.getLocalHost().getHostName(), 5701);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public InetSocketAddress getPublicAddress() {
        try {
            return new InetSocketAddress(InetAddress.getLocalHost().getHostName(), 5701);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
