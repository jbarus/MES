package org.example;

import org.example.integration.MatrixH;
import org.example.integration.MatrixHBC;
import org.example.jakobian.UniversalElement;
import org.example.mes.*;


import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader("./src/main/resources/Test1_4_4.txt");
        Grid grid = new Grid(9,16);
        GlobalData data = new GlobalData();
        dataLoader.loadGlobalData(data);
        dataLoader.loadGrid(grid);
        UniversalElement universalElement = new UniversalElement(3);
        MatrixH matrixH = new MatrixH(universalElement, data);
        MatrixHBC matrixHBC = new MatrixHBC(universalElement);
        //Node[] nodes = new Node[]{new Node(0.0,0.0,0),new Node(0.025,0.0,0),new Node(0.025,0.025,0),new Node(0.0,0.025,0)};
        for(Element element : grid.getElements()){
            Node[] node = new Node[4];
            for (int i = 0; i < 4; i++) {
                node[i] = grid.getNodes()[element.getID()[i]-1];
            }
            element.setH(matrixH.calculateMatrixH(node));
            double[][] temp = new double[16][1];
            element.setHBC(matrixHBC.calculate(node, temp));
            System.out.println(Arrays.deepToString(temp));
            //System.out.println(Arrays.deepToString(element.getH()));
            //System.out.println("Początek");
            //System.out.println(Arrays.deepToString(element.getHBC()));
            //System.out.println("Koniec");
        }

        SystemOfEquations systemOfEquations = new SystemOfEquations(grid);
        systemOfEquations.calculateHGlobal();



    }
}