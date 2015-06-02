package com.db.dbx.common;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamStringBuilder extends ServletOutputStream {

    StringBuilder stringBuilder;

    public ServletOutputStreamStringBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public void write(int b) throws IOException {
    } 

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        stringBuilder.append(new String(b, off, len, "UTF-8"));
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}