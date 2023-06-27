package com.utfpr.distributed.util.socket;

import com.utfpr.distributed.controller.ServerController;
import com.utfpr.distributed.util.Gateway;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ServerSocketConnectionHandler implements Runnable {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private volatile static boolean serverContinue = true;
    private final Integer porta;

    public ServerSocketConnectionHandler(Integer porta) {
        this.porta = porta;
    }

    @Override
    public  void run() {

        // 10008
        try (ServerSocket serverSocket = new ServerSocket(this.porta)) {
            LOGGER.log(Level.INFO, "Socket de coneção iniciado");
            awaitConnection(serverSocket);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Falha ao tentar ouvir na porta especificada.");
            System.exit(1);
        }
    }

    private void awaitConnection(ServerSocket serverSocket) {

        try {
            while (serverContinue) {
                LOGGER.log(Level.INFO, "Esperando conexão...");

                execute(serverSocket.accept());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Aceite falhou.");
            System.exit(1);
        }
    }

    public void execute(Socket clientSocket) {
        LOGGER.log(Level.INFO, "Nova thread de comunicação.");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String incomingMessage = in.readLine();
            LOGGER.log(Level.INFO, "Recebido: {0}", incomingMessage);

            String outgoingMessage = Gateway.chooseOperation(incomingMessage);
            LOGGER.log(Level.INFO, "Enviado: {0}", outgoingMessage);

            ServerController.appendReceived(incomingMessage);
            ServerController.appendSent(outgoingMessage, getIdFromJSON(incomingMessage));

            out.println(outgoingMessage);

            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problema com o Servidor de Comunicação.");
            System.exit(1);
        }
    }

    private Integer getIdFromJSON(String rawJson) {
        try {
            JSONObject json = new JSONObject(rawJson);
            if(json.has("id")) {
                return json.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter id: " + e.getMessage());
        }

        return -1;
    }
}
