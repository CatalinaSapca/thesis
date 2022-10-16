package com.thesis.tattootopy.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketHelper {

    static Thread sent;
    static Thread receive;
    static Socket socket;

    private static final Logger logger = LogManager.getLogger();

    public static String generate(Long id, String description) {
        String filePath = null;

        try {
            socket = new Socket("localhost", 9999);

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            logger.info("TattooGenerationJob : callPythonServer( " + description + " ) is called");
            System.out.println("Trying to read...");
            String in = stdIn.readLine();

            logger.info("TattooGenerationJob : " + in);
            System.out.println(in);

            // ------------------------------------------------- sending the tattoo description
            out.print(description + "\r\n");
            out.flush();

            // ------------------------------------------------- sending the unique id of the future generated tattoo
            out.print(id + "\r\n");
            out.flush();

            System.out.println("Request sent");

            String fileP = stdIn.readLine();
            System.out.println("File path is: " + fileP);
            logger.info("TattooGenerationJob : file path - " + fileP);

            filePath = fileP;

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return filePath;
    }



}
