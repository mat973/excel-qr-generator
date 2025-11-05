package ru.practicum.jumping_balls;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        int count = Integer.parseInt(reader.readLine());

        int[] jumpingCount = new int[count+3];
        jumpingCount[0] = 0;
        jumpingCount[1] = 0;
        jumpingCount [2] = 1;
        for (int i = 3; i < jumpingCount.length; i++) {
            jumpingCount[i] = jumpingCount[i-1] + jumpingCount[i -2] + jumpingCount[i - 3];
        }

        writer.write(jumpingCount[jumpingCount.length -1] + "");
        reader.close();
        writer.close();
    }
}
