package org.example.jakobian;

import lombok.Getter;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class UniversalElement {
    private double[][] ksi;
    private double[][] eta;
    private double[][][] surface;
    private double[] points;
    private double[] weights;
    private int numberOfPoints;

    Function<Double, Double>[] etaFunc = new Function[4];
    Function<Double, Double>[] ksiFunc = new Function[4];
    BiFunction<Double, Double, Double>[] nFunc = new BiFunction[4];

    public UniversalElement(int numberOfPoints) {
        ksi = new double[(int) Math.pow(numberOfPoints, 2.0)][4];
        eta = new double[(int) Math.pow(numberOfPoints, 2.0)][4];
        surface = new double[4][numberOfPoints][4];
        this.numberOfPoints = numberOfPoints;
        initFunc();
        initTab(numberOfPoints);

    }

    private void initTab(int size) {
        if (size == 2) {
            points = new double[]{-1 / Math.sqrt(3), 1 / Math.sqrt(3)};
            weights = new double[]{1, 1};
        }
        if (size == 3) {
            points = new double[]{-Math.sqrt((3.0 / 5.0)), 0.0, Math.sqrt((3.0 / 5.0))};
            weights = new double[]{5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
        }
        if (size == 4) {
            points = new double[]{-1.0 * Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), -1.0 * Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))),
                    Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)))};
            weights = new double[]{(18.0-Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0-Math.sqrt(30.0))/36.0};
        }
        for (int i = 0; i < ksi.length; i++) {
            for (int j = 0; j < ksi[0].length; j++) {
                ksi[i][j] = ksiFunc[j].apply(points[i % size]);
                eta[i][j] = etaFunc[j].apply(points[i % size]);
            }
        }
        Arrays.sort(ksi, (a, b) -> Double.compare(a[0], b[0]));


        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                surface[0][i][j] = nFunc[j].apply(points[i],-1.0);
            }
        }
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                surface[1][i][j] = nFunc[j].apply(1.0,points[(numberOfPoints-1) - i]);
            }
        }
        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                surface[2][i][j] = nFunc[j].apply(points[i],1.0);
            }
        }

        for (int i = 0; i < numberOfPoints; i++) {
            for (int j = 0; j < 4; j++) {
                surface[3][i][j] = nFunc[j].apply(-1.0,points[(numberOfPoints-1) - i]);
            }
        }
        //Arrays.stream(surface).iterator().forEachRemaining(x-> System.out.println(Arrays.deepToString(x)));
    }

    private void initFunc() {
        ksiFunc[0] = x -> -0.25 * (1 - x);
        ksiFunc[1] = x -> 0.25 * (1 - x);
        ksiFunc[2] = x -> 0.25 * (1 + x);
        ksiFunc[3] = x -> -0.25 * (1 + x);

        etaFunc[0] = x -> -0.25 * (1 - x);
        etaFunc[1] = x -> -0.25 * (1 + x);
        etaFunc[2] = x -> 0.25 * (1 + x);
        etaFunc[3] = x -> 0.25 * (1 - x);

        nFunc[0] = (x,y) -> 0.25 * (1-x) * (1-y);
        nFunc[1] = (x,y) -> 0.25 * (1+x) * (1-y);
        nFunc[2] = (x,y) -> 0.25 * (1+x) * (1+y);
        nFunc[3] = (x,y) -> 0.25 * (1-x) * (1+y);
    }
}
