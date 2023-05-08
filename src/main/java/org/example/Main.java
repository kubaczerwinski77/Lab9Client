package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    // {"name": "Kuba", "age":30, "id": 125, "email": "kubac@gmail.com"}

    private static final String BASE_URL = "http://localhost:8080/";

    public static void main(String[] args) {
        try {
            MyData.myInfo();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        String endpoint = "";
        String data = "";
        String id = "";

        while (true) {
            System.out.println("Podaj endpoint (users, posts, etc.):");
            endpoint = scanner.nextLine();

            System.out.println("Wybierz operacje CRUD: (1) GET, (2) POST, (3) PUT, (4) DELETE");
            int operation = scanner.nextInt();
            scanner.nextLine();

            switch (operation) {
                case 1 -> get(endpoint);
                case 2 -> {
                    System.out.println("Podaj dane JSON:");
                    data = scanner.nextLine();
                    post(endpoint, data);
                }
                case 3 -> {
                    System.out.println("Podaj ID:");
                    id = scanner.nextLine();
                    System.out.println("Podaj dane JSON:");
                    data = scanner.nextLine();
                    put(endpoint, id, data);
                }
                case 4 -> {
                    System.out.println("Podaj ID:");
                    id = scanner.nextLine();
                    delete(endpoint, id);
                }
                default -> System.out.println("Nie ma takiej operacji!");
            }

            System.out.println("Czy chcesz kontynuowac?");
            String res = scanner.nextLine();
            if (!res.equalsIgnoreCase("TAK")) {
                break;
            }
        }
    }

    private static void get(String endpoint) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                String jsonString = content.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
//                JSONObject embeddedObject = new JSONObject(jsonObject.get("_embedded").toString());
                System.out.println(jsonObject.toString(4));
            } else {
                System.out.println("Zapytanie GET nie powiodlo sie!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void post(String endpoint, String data) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes());

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(content.toString());
                System.out.println(jsonObject.toString(4));
            } else {
                System.out.println("Zapytanie POST nie powiodlo sie!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void put(String endpoint, String id, String data) {
        try {
            URL url = new URL(BASE_URL + endpoint + "/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes());

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(content.toString());
                System.out.println(jsonObject.toString(4));
            } else {
                System.out.println("Zapytanie PUT nie powiodlo sie!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delete(String endpoint, String id) {
        try {
            URL url = new URL(BASE_URL + endpoint + "/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                System.out.println("Pomyslnie usunieto!");
            } else {
                System.out.println("Zapytanie DELETE nie powiodlo sie!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

           
