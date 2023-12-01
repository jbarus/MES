package org.example.integration;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.example.jakobian.UniversalElement;
import org.example.mes.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixHBC {
    RealMatrix[] surface;
    List<RealMatrix> HbcPartial;
    List<RealMatrix> PVector;
    Node[] nodes;
    double[] det;
    UniversalElement universalElement;
    public MatrixHBC(UniversalElement universalElement){
        this.universalElement = universalElement;
        surface = new RealMatrix[4];

        det = new double[4];

        for (int i = 0; i < universalElement.getSurface().length; i++) {
            surface[i] = MatrixUtils.createRealMatrix(universalElement.getSurface()[i]);
        }
        //for(RealMatrix r : surface)
            //System.out.println(Arrays.deepToString(r.getData()));

    }

    public double[][] calculate(Node[] nodes, double[][] vectorP){
        this.nodes = nodes;
        HbcPartial = new ArrayList<>();
        PVector = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if(i == 3){
                det[i] = Math.sqrt(Math.pow(nodes[0].getX()-nodes[i].getX(),2.0)+Math.pow(nodes[0].getY()-nodes[i].getY(),2.0));
            }else{
                det[i] = Math.sqrt(Math.pow(nodes[i+1].getX()-nodes[i].getX(),2.0)+Math.pow(nodes[i+1].getY()-nodes[i].getY(),2.0));
            }
        }
        //System.out.println(Arrays.toString(det));

        RealMatrix temp = MatrixUtils.createRealMatrix(4,4);
        RealMatrix temp2 = MatrixUtils.createRealMatrix(4,1);
        if(nodes[0].getBC() == nodes[1].getBC() && nodes[0].getBC() != 0 && nodes[1].getBC() != 0){
            for (int i = 0; i < universalElement.getNumberOfPoints(); i++){
                temp = temp.add(surface[0].getRowMatrix(i).transpose().multiply(surface[0].getRowMatrix(i)).scalarMultiply(universalElement.getWeights()[i]));
                temp2 = temp2.add(surface[0].getRowMatrix(i).transpose().scalarMultiply(1200.0).scalarMultiply(universalElement.getWeights()[i]));
            }
            temp2 = temp2.scalarMultiply(300).scalarMultiply(det[0]/2);
            temp = temp.scalarMultiply(300).scalarMultiply(det[0]/2);
            HbcPartial.add(temp);
            PVector.add(temp2);
        }
        temp = MatrixUtils.createRealMatrix(4,4);
        temp2 = MatrixUtils.createRealMatrix(4,1);
        if(nodes[1].getBC() == nodes[2].getBC() && nodes[1].getBC() != 0 && nodes[2].getBC() != 0){
            for (int i = 0; i < universalElement.getNumberOfPoints(); i++){
                temp = temp.add(surface[1].getRowMatrix(i).transpose().multiply(surface[1].getRowMatrix(i)).scalarMultiply(universalElement.getWeights()[i]));
                temp2 = temp2.add(surface[1].getRowMatrix(i).transpose().scalarMultiply(1200.0).scalarMultiply(universalElement.getWeights()[i]));
            }
            temp2 = temp2.scalarMultiply(300).scalarMultiply(det[0]/2);
            temp = temp.scalarMultiply(300).scalarMultiply(det[1]/2);
            HbcPartial.add(temp);
            PVector.add(temp2);
        }
        temp = MatrixUtils.createRealMatrix(4,4);
        temp2 = MatrixUtils.createRealMatrix(4,1);
        if(nodes[2].getBC() == nodes[3].getBC() && nodes[2].getBC() != 0 && nodes[3].getBC() != 0){
            for (int i = 0; i < universalElement.getNumberOfPoints(); i++){
                temp = temp.add(surface[2].getRowMatrix(i).transpose().multiply(surface[2].getRowMatrix(i)).scalarMultiply(universalElement.getWeights()[universalElement.getNumberOfPoints()-1-i]));
                temp2 = temp2.add(surface[2].getRowMatrix(i).transpose().scalarMultiply(1200.0).scalarMultiply(universalElement.getWeights()[universalElement.getNumberOfPoints()-1-i]));
            }
            temp2 = temp2.scalarMultiply(300).scalarMultiply(det[0]/2);
            temp = temp.scalarMultiply(300).scalarMultiply(det[2]/2);
            HbcPartial.add(temp);
            PVector.add(temp2);
        }
        temp = MatrixUtils.createRealMatrix(4,4);
        temp2 = MatrixUtils.createRealMatrix(4,1);
        if(nodes[3].getBC() == nodes[0].getBC() && nodes[3].getBC() != 0 && nodes[0].getBC() != 0){
            for (int i = 0; i < universalElement.getNumberOfPoints(); i++){
                temp = temp.add(surface[3].getRowMatrix(i).transpose().multiply(surface[3].getRowMatrix(i)).scalarMultiply(universalElement.getWeights()[universalElement.getNumberOfPoints()-1-i]));
                temp2 = temp2.add(surface[3].getRowMatrix(i).transpose().scalarMultiply(1200.0).scalarMultiply(universalElement.getWeights()[universalElement.getNumberOfPoints()-1-i]));
            }
            temp2 = temp2.scalarMultiply(300).scalarMultiply(det[0]/2);
            temp = temp.scalarMultiply(300).scalarMultiply(det[3]/2);
            HbcPartial.add(temp);
            PVector.add(temp2);
        }

        //System.out.println(HbcPartial.size());
        //HbcPartial.forEach(System.out::println);

        RealMatrix result = MatrixUtils.createRealMatrix(4,4);
        RealMatrix result2 = MatrixUtils.createRealMatrix(4,1);
        for (int i = 0; i <HbcPartial.size(); i++) {
            result = result.add(HbcPartial.get(i));
            result2 = result2.add(PVector.get(i));
        }
        vectorP = result2.getData();
        //System.out.println(Arrays.deepToString(vectorP));

        return result.getData();
    }
}
