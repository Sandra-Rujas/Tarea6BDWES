package com.sandrarujas.tarea6dwessandrarujas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
public class Tarea6dwessandrarujasApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tarea6dwessandrarujasApplication.class, args);
        
        // Intentar abrir el navegador en Windows
        openBrowser("http://localhost:8080");
    }

    private static void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                // Comando para abrir el navegador en Windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Comando para abrir el navegador en Linux/Mac
                String[] browsers = { "google-chrome", "firefox", "safari" };
                for (String browser : browsers) {
                    try {
                        Runtime.getRuntime().exec(new String[] { browser, url });
                        break;
                    } catch (IOException e) {
                        // Si el navegador no se encuentra, intenta con el siguiente
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
