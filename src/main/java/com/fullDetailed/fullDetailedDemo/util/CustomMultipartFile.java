package com.fullDetailed.fullDetailedDemo.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;

public class CustomMultipartFile implements MultipartFile {

    private final File file;
    private final String originalFilename;

    public CustomMultipartFile(File file, String originalFilename) {
        this.file = file;
        this.originalFilename = originalFilename;
    }

    @Override
    public String getName() { return "files"; }

    @Override
    public String getOriginalFilename() { return originalFilename; }

    @Override
    public String getContentType() { return "application/octet-stream"; }

    @Override
    public boolean isEmpty() { return file.length() == 0; }

    @Override
    public long getSize() { return file.length(); }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        Files.copy(file.toPath(), dest.toPath());
    }
}