package com.gmail.safordog.newsreport.model;

import java.io.*;
import java.util.Date;

public class Logger {

    private String message;

    public Logger() {
    }

    public Logger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeLog(String message) throws IOException {
        File log = new File("log.txt");
        if(!log.exists()) {
            log.createNewFile();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(log, true))) {
            Date date = new Date();
            pw.println("-----begin-----");
            pw.println(date.toString());
            pw.println(message);
            pw.println("------end------");
        } catch (FileNotFoundException e) {
            System.out.println("Error file write");
        }

    }
}
