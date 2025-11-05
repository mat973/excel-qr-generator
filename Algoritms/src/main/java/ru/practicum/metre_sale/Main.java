package ru.practicum.metre_sale;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] params = reader.readLine().split(" ");
        int amountOfFabric = Integer.parseInt(params[1]);
        int amountOFMagazine = Integer.parseInt(params[0]);
        int[] fullCost = new int[amountOFMagazine];
        int[] metreForSale = new int[amountOFMagazine];
        int[] saleCost = new int[amountOFMagazine];
        int[] metreInMagazine = new int[amountOFMagazine];

        int totalFabric = 0;
        for (int i = 0; i < amountOFMagazine; i++) {
            String[] line = reader.readLine().split(" ");
            fullCost[i] = Integer.parseInt(line[0]);
            metreForSale[i] = Integer.parseInt(line[1]);
            saleCost[i] = Integer.parseInt(line[2]);
            metreInMagazine[i] = Integer.parseInt(line[3]);
            totalFabric += metreInMagazine[i];

        }

        if (amountOfFabric > totalFabric) {
            writer.write(-1 + "");
            writer.close();
            reader.close();
            return;
        }

        int[][] dp = new int[amountOFMagazine][totalFabric + 1];
        int[][] prev = new int[amountOFMagazine][totalFabric + 1];

        for (int i = 0; i < amountOFMagazine; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        for (int j = 0; j <= metreInMagazine[0]; j++) {
            if (j < metreForSale[0]) {
                dp[0][j] = j * fullCost[0];
            } else {
                dp[0][j] = Math.min(j * fullCost[0], j * saleCost[0]);
            }
            prev[0][j] = j;
        }

        for (int i = 1; i < amountOFMagazine; i++) {
            for (int j = 0; j <= totalFabric; j++) {
                for (int k = 0; k <= metreInMagazine[i]; k++) {
                    int prevFabric = Math.max(0, j - k);
                    if (dp[i - 1][prevFabric] == Integer.MAX_VALUE) continue;

                    int costPerMetre = (k >= metreForSale[i]) ? saleCost[i] : fullCost[i];
                    int cost = dp[i - 1][prevFabric] + k * costPerMetre;

                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        prev[i][j] = k;
                    }
                }
            }
        }

        int min = Integer.MAX_VALUE;
        int bestCol = amountOfFabric;
        for (int j = amountOfFabric; j <= totalFabric; j++) {
            if (dp[amountOFMagazine - 1][j] < min) {
                min = dp[amountOFMagazine - 1][j];
                bestCol = j;
            }
        }
//        if (min == Integer.MAX_VALUE) {
//            writer.write(-1 + "");
//            writer.close();
//            reader.close();
//        }
        int[] answer = new int[amountOFMagazine];
        int j = bestCol;
        for (int i = amountOFMagazine - 1; i >= 0; i--) {
            int k = prev[i][j];
            answer[i] = k;
            j = Math.max(0, j - k);
        }

        writer.write(min + "\n");
        for (int i = 0; i < amountOFMagazine; i++) {
            writer.write(answer[i] + " ");
        }




        reader.close();
        writer.close();
    }
}
