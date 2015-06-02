package com.db.dbx.gateway;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.db.dbx.common.ServletOutputStreamStringBuilder;

public class DispatchServletResponseWrapper extends HttpServletResponseWrapper {

    ServletOutputStream outputStream;

    public DispatchServletResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        outputStream = new ServletOutputStreamStringBuilder();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
    }
}