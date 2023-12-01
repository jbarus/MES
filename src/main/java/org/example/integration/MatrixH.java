package org.example.integration;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.example.jakobian.UniversalElement;
import org.example.mes.GlobalData;
import org.example.mes.Node;

import java.util.Arrays;
import java.util.logging.Level;

public class MatrixH {
    private GlobalData globalData;
    private UniversalElement universalElement;
    private RealMatrix[] jacobianMatrix;
    private RealMatrix[] reverseJacobianMatrix;
    private double[] weights;
    private RealMatrix dNdX;
    private RealMatrix dNdY;
    private double[] detJacobian;
    private RealMatrix[] H;
    private int numberOfPoints;
    private RealMatrix dNdKsi;
    private RealMatrix dNdEta;
    Level loggingLevel;


    public MatrixH(UniversalElement universalElement, GlobalData globalData) {
        // Level.INFO - Critical steps are being printed out
        // Level.ALL - All steps are being printed out
        // Level.OFF - Logging is turned off
        this.universalElement = universalElement;
        this.globalData = globalData;
        this.numberOfPoints = universalElement.getNumberOfPoints();

        initWeights(numberOfPoints);

        dNdKsi = MatrixUtils.createRealMatrix(universalElement.getKsi());
        dNdEta = MatrixUtils.createRealMatrix(universalElement.getEta());
    }

    public void setLoggingLevel(Level level){
        loggingLevel = level;
    }


    public double[][] calculateMatrixH(Node[] node) {
        calculateJacobianMatrix(node);
        calculateReverseJacobian(node);
        calculateDXandDY();
        calculateHMatrix();

        RealMatrix out = MatrixUtils.createRealMatrix(4,4);
        int temp = -1;
        for (int i = 0; i < numberOfPoints*numberOfPoints; i++) {
            if(i%numberOfPoints == 0)
                temp++;
            H[i] = H[i].scalarMultiply(weights[i%numberOfPoints]).scalarMultiply(weights[temp]);

            out = out.add(H[i]);
        }
        //System.out.println("Koniec");
        return out.getData();
    }

    public void calculateHMatrix() {
        H = new RealMatrix[numberOfPoints*numberOfPoints];
        for (int i = 0; i < H.length; i++) {

            H[i] = dNdX.getRowMatrix(i).transpose().multiply(dNdX.getRowMatrix(i));
            H[i] = H[i].add(dNdY.getRowMatrix(i).transpose().multiply(dNdY.getRowMatrix(i)));
            H[i] = H[i].scalarMultiply(globalData.getConductivity()).scalarMultiply(detJacobian[i]);
        }
    }

    public void calculateDXandDY() {
        dNdX = MatrixUtils.createRealMatrix(new double[numberOfPoints*numberOfPoints][4]);
        dNdY = MatrixUtils.createRealMatrix(new double[numberOfPoints*numberOfPoints][4]);

        RealMatrix temp;
        RealMatrix matrix;
        for (int i = 0; i < dNdX.getRowDimension(); i++) {
            for (int j = 0; j < 4; j++) {
                temp = reverseJacobianMatrix[i];
                matrix = MatrixUtils.createRealMatrix(2,1);
                matrix.setEntry(0,0,dNdKsi.getEntry(i,j));
                matrix.setEntry(1,0,dNdEta.getEntry(i,j));
                matrix = temp.multiply(matrix);
                dNdX.setEntry(i,j,matrix.getEntry(0,0));
                dNdY.setEntry(i,j,matrix.getEntry(1,0));
            }

        }
        //System.out.println(Arrays.deepToString(dNdX.getData()));
        //System.out.println(Arrays.deepToString(dNdY.getData()));
    }

    public void calculateReverseJacobian(Node[] nodes) {
        detJacobian = new double[numberOfPoints*numberOfPoints];
        reverseJacobianMatrix = new RealMatrix[numberOfPoints*numberOfPoints];
        for (int i = 0; i < reverseJacobianMatrix.length; i++) {
            detJacobian[i] = (new LUDecomposition(jacobianMatrix[i])).getDeterminant();
            reverseJacobianMatrix[i] = MatrixUtils.inverse(jacobianMatrix[i]);

        }

    }

    public void calculateJacobianMatrix(Node[] node) {
        jacobianMatrix = new RealMatrix[numberOfPoints*numberOfPoints];


        for (int i = 0; i < jacobianMatrix.length; i++) {
            double[][] temp = new double[2][2];
            temp[0][0] = node[0].getX() * dNdKsi.getEntry(i,0) + node[1].getX() * dNdKsi.getEntry(i,1) + node[2].getX() * dNdKsi.getEntry(i,2) + node[3].getX() * dNdKsi.getEntry(i,3);
            temp[0][1] = node[0].getY() * dNdKsi.getEntry(i,0) + node[1].getY() * dNdKsi.getEntry(i,1) + node[2].getY() * dNdKsi.getEntry(i,2) + node[3].getY() * dNdKsi.getEntry(i,3);
            temp[1][0] = node[0].getX() * dNdEta.getEntry(i,0) + node[1].getX() * dNdEta.getEntry(i,1) + node[2].getX() * dNdEta.getEntry(i,2) + node[3].getX() * dNdEta.getEntry(i,3);
            temp[1][1] = node[0].getY() * dNdEta.getEntry(i,0) + node[1].getY() * dNdEta.getEntry(i,1) + node[2].getY() * dNdEta.getEntry(i,2) + node[3].getY() * dNdEta.getEntry(i,3);
            jacobianMatrix[i] = MatrixUtils.createRealMatrix(temp);
        }
    }

    public void initWeights(int n) {
        if(n == 2){
            //points = new double[]{-1 / Math.sqrt(3), 1 / Math.sqrt(3)};
            weights = new double[]{1, 1};
        }
        if (n == 3){
            //points = new double[]{-Math.sqrt((3.0 / 5.0)), 0.0, Math.sqrt((3.0 / 5.0))};
            weights = new double[]{5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
        }
        if(n == 4){
            //points = new double[]{Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), -Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))), Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))),  -Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)))};
            weights = new double[]{(18.0-Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0+Math.sqrt(30.0))/36.0, (18.0-Math.sqrt(30.0))/36.0};
        }

    }
}
