package ru.practicum.some_algoritm_pricol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        int MishaSum = 0;
        int MashaSum = 0;
        int MishaMinValue = Integer.MAX_VALUE;
        int MashaMaxValue = Integer.MIN_VALUE;
        int mushroomAmount = Integer.parseInt(reader.readLine());
        String line = reader.readLine();
        String[] currentWeights = line.split(" ");
        for (int i = 0; i < mushroomAmount; i++) {
            int currentWeight = Integer.parseInt(currentWeights[i]);
            if (i % 2 == 0) {
                MishaSum += currentWeight;
                if (currentWeight < MishaMinValue) {
                    MishaMinValue = currentWeight;
                }
            } else {
                MashaSum += currentWeight;
                if (currentWeight > MashaMaxValue) {
                    MashaMaxValue = currentWeight;
                }
            }
        }

        if (MishaMinValue >= MashaMaxValue) {
            writer.write(MishaSum - MashaSum + "");
        } else {
            writer.write(MishaSum - MashaSum + 2 * (MashaMaxValue - MishaMinValue) + "");
        }

            reader.close();
            writer.close();
        }
    }

