package ru.practicum.five_in_the_row;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] param = reader.readLine().split(" ");
        int row = Integer.parseInt(param[0]);
        int column = Integer.parseInt(param[1]);
        


        reader.close();
        writer.close();
    }
}
