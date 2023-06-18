package com.utfpr.distributed.util.socket;

import com.utfpr.distributed.util.ClientOperationHandler;
import com.utfpr.distributed.util.ClientSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClientSocketConnectionHandler {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    public static JSONObject run(Map<String, Object> input) {
        try (Socket serverSocket = new Socket(ClientSession.getEndereco(), ClientSession.getPorta());
             PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));) {

            String outgoingMessage = ClientOperationHandler.request(input);
            out.println(outgoingMessage);

            LOGGER.log(Level.INFO, "Request: {0}", outgoingMessage);

            String incomingMessage = in.readLine();
            LOGGER.log(Level.INFO, "Response: {0}", incomingMessage);

            return ClientOperationHandler.handleResponse(incomingMessage);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem with Communication Server.");
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
