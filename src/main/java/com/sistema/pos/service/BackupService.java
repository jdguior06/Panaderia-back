package com.sistema.pos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistema.pos.config.LoggableAction;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BackupService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private static final String BACKUP_DIR = "backups/"; 

    @LoggableAction
    public File createBackup() throws IOException, InterruptedException {
        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String backupFileName = BACKUP_DIR + dbName + "_backup_" + timestamp + ".backup";

        new File(BACKUP_DIR).mkdirs(); // Crea el directorio si no existe

        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump",         // Ruta completa de pg_dump
                "-h", "localhost",
                "-U", dbUser,
                "-F", "c",          // Formato custom
                "-f", backupFileName,
                dbName
        );

        processBuilder.environment().put("PGPASSWORD", dbPassword);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Error al crear el backup, código de salida: " + exitCode + "\n" + errorOutput);
        }
        return new File(backupFileName);
    }

    // Método para restaurar un backup desde un archivo
    @LoggableAction
    public void restoreBackup(MultipartFile file) throws IOException, InterruptedException {
        File tempFile = saveFileToTemp(file);
        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);

        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_restore",      // Ruta completa de pg_restore
                "-h", "localhost",
                "-U", dbUser,
                "-d", dbName,
                "--clean",          // Limpia objetos existentes antes de restaurar
                "--if-exists",      // Ignora si el objeto no existe
                "--no-owner",       // Evita problemas de propietario
                "-F", "c",          // Especifica el formato Custom
                tempFile.getAbsolutePath()
        );

        processBuilder.environment().put("PGPASSWORD", dbPassword);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = stdOutput.readLine()) != null) {
                output.append("OUT: ").append(line).append("\n");
            }

            while ((line = stdError.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        Files.delete(tempFile.toPath()); // Limpia el archivo temporal

        if (exitCode != 0) {
            throw new IOException("Error al cargar el backup. Código de salida: " + exitCode + "\nDetalles del error:\n" + output.toString());
        }
    }

    // Método auxiliar para guardar el archivo recibido en una ubicación temporal
    @LoggableAction
    private File saveFileToTemp(MultipartFile file) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "backup_" + timestamp + ".backup");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }
}

