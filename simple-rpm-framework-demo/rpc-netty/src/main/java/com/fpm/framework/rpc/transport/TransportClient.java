package com.fpm.framework.rpc;

import java.io.Closeable;
import java.io.InterruptedIOException;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedIOException, TimeoutException;
    void close();
}
