package com.salesforce.casp.echo.example;

public class Application {
    public static void main(final String[] args) throws Exception {
        new HttpServer("http://localhost:8786").start();
    }
}

