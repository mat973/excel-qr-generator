package ru.practicum;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        int count;
        Map<Integer, Integer> arr1 = new HashMap<>();
        int sum;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            count = Integer.parseInt(reader.readLine());
            Arrays.stream(reader.readLine().split(" "))
                    .map(Integer::parseInt)
                    .forEach(x -> arr1.merge(x, 1 ,Integer::sum));

            sum = Integer.parseInt(reader.readLine());

        }


        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        for (Integer i : arr1.keySet()) {
            if (arr1.containsKey(sum - i)){
                if (i == sum -i && arr1.get(i) < 2){
                    continue;
                }
                writer.write(i + " " + (sum - i ));
                writer.flush();
                return;
            }
        }
        writer.write("None");
        writer.flush();
    }
}

