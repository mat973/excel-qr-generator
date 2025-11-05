package ru.practicum.dp_cating_word;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        String line = reader.readLine();
        int numbOfStrings = Integer.parseInt(reader.readLine());
        Set<String> set = new HashSet<>();
        for (int i = 0; i < numbOfStrings; i++) {
            set.add(reader.readLine());
        }
        boolean[] dp = new boolean[line.length() + 1];
        int[] prev = new int[line.length() + 1];
        Arrays.fill(prev, -1);
        dp[0] = true;
        for (int i = 1; i <= line.length(); i++) {
            for (int j = Integer.max(0, i -20); j < i; j++) {
                if (dp[j] && set.contains(line.substring(j, i))) {
                    dp[i] = true;
                    prev[i] = j;
                    break;
                }
            }
        }
        List<String> answer = new ArrayList<>();

        while (!line.isEmpty()){
            int link = prev[line.length()];
            answer.add(line.substring(link));
            line = line.substring(0, link);
        }
        Collections.reverse(answer);

        writer.write(String.join(" ", answer));
        reader.close();
        writer.close();
    }

}
