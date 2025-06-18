package com.newEra.strangers.chat.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import com.newEra.strangers.chat.util.Constants;

public class SocketUtil {
    public static Socket socket = null;

    public static Socket getSocketInstance() throws URISyntaxException {
        return socket;
    }

    public static void initializeAndConnect() throws URISyntaxException {
        if (socket == null) {
            socket = IO.socket(Constants.CHAT_SERVER_URL);
        }
        socket.connect();
    }
}
