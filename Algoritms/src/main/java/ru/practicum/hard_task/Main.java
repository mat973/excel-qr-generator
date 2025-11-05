package ru.practicum.hard_task;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder out = new StringBuilder();

        int numbOfOperation = Integer.parseInt(reader.readLine());
        Map<String, FigonList> map = new HashMap<>();

        for (int k = 0; k < numbOfOperation; k++) {
            String line = reader.readLine();
            String[] params = line.split("\\s+");
            if (line.contains("new") && params.length == 5) {

                String name = params[1];
                String strNumb = params[4].substring(params[4].indexOf("(") + 1, params[4].lastIndexOf(")"));
                List<Integer> numbers = Arrays.stream(strNumb.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                map.put(name, new FigonList(numbers));
            } else if (line.contains("subList") && params.length == 4) {
                String name = params[1];
                String baseName = params[3].substring(0, params[3].indexOf('.'));
                String[] strNumbs = params[3].substring(params[3].indexOf("(") + 1, params[3].lastIndexOf(")")).split(",");
                int from = Integer.parseInt(strNumbs[0].trim());
                int to = Integer.parseInt(strNumbs[1].trim());
                FigonList base = map.get(baseName);
                map.put(name, new FigonList(base, from, to));
            } else {
                String[] parts = line.split("\\.");
                String name = parts[0];
                String op = parts[1];
                FigonList figon = map.get(name);

                if (op.startsWith("set")) {
                    String inside = op.substring(op.indexOf("(") + 1, op.lastIndexOf(")"));
                    String[] nums = inside.split(",");
                    int i = Integer.parseInt(nums[0].trim());
                    int x = Integer.parseInt(nums[1].trim());
 //                   System.out.println("Ошибка в методе set на значениях" + i + " "+ x + " для  троки с именем " +  name);
                    figon.set(i, x);
                } else if (op.startsWith("add")) {
                    int x = Integer.parseInt(op.substring(op.indexOf("(") + 1, op.lastIndexOf(")")));
                    figon.add(x);
                } else if (op.startsWith("get")) {
                    int i = Integer.parseInt(op.substring(op.indexOf("(") + 1, op.lastIndexOf(")")).trim());
 //                   System.out.println("Ошибка в методе get на значениях" + i + " для  троки с именем " +  name);
                    out.append(figon.get(i)).append('\n');
                }
            }
        }

        writer.write(out.toString());
        writer.flush();
        writer.close();
        reader.close();

    }


    static class FigonList {
        private FigonList root;
        private List<Integer> data;
        private int offset;
        private int size;
        private boolean isRoot;

        public FigonList(List<Integer> data) {
            this.data = data;
            this.root = this;
            this.offset = 0;
            this.size = data.size();
            this.isRoot = true;
        }

        public FigonList(FigonList parent, int from, int to) {
            this.root = parent.root;
            this.offset = parent.offset + from - 1;
            this.size = to - from + 1;
            this.isRoot = false;
        }

        int get(int index) {
            int positionInRoot = offset + (index - 1);
            return root.data.get(positionInRoot);
        }

        void set(int index, int value) {
            int positionInRoot = offset + (index - 1);
            root.data.set(positionInRoot, value);
        }

        void add(int value) {
            if (!isRoot) {
                return;
            }
            root.data.add(value);
            size++;
        }
    }


}
