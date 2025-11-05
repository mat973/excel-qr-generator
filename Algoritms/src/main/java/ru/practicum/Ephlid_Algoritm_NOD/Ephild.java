package ru.practicum.Ephlid_Algoritm_NOD;

public class Ephild {
    public static void main(String[] args) {
        System.out.println(gcd(35, 5));
        System.out.println(gcd(5, 35));
        System.out.println(gcd(7, 3));
    }

    public static int gcd(int p, int q){
        if (q == 0) return p;
        int r = p % q;
        return  gcd(q, r);
    }
}


