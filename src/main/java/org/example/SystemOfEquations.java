package org.example;

import lombok.Data;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.example.mes.Element;
import org.example.mes.Grid;
import org.example.mes.Node;

import java.util.Arrays;

@Data
public class SystemOfEquations {
    RealMatrix HGlobal;
    Grid grid;

    public SystemOfEquations(Grid grid) {
        this.grid = grid;
    }

    void calculateHGlobal(){
        HGlobal = MatrixUtils.createRealMatrix(16,16);
        for(Element element : grid.getElements()){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    HGlobal.addToEntry(element.getID()[i]-1,element.getID()[j]-1,element.getH()[i][j]+element.getHBC()[i][j]);
                }
            }
        }
        System.out.println(Arrays.deepToString(HGlobal.getData()));
    }
}
