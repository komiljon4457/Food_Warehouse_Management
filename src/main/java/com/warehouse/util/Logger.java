package com.warehouse.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private PrintWriter fileWriter;
    private SimpleDateFormat dateFormat;
    private boolean consoleOutput;

    private Logger() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.consoleOutput = true;
        try {
            this.fileWriter = new PrintWriter(new FileWriter("warehouse.log", true), true);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public synchronized void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = "[" + timestamp + "] " + message;

        if (consoleOutput) {
            System.out.println(logMessage);
        }

        if (fileWriter != null) {
            fileWriter.println(logMessage);
        }
    }

    public void setConsoleOutput(boolean enabled) {
        this.consoleOutput = enabled;
    }

    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
