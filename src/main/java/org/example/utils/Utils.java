package org.example.utils;

public class Utils {
    private Utils(){};
    public static void printMatrix(double[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.printf("%.5f ",(arr[i][j]));

            }
            System.out.println();
        }
    }

}
