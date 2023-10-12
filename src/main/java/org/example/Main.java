package org.example;

import org.example.mes.DataLoader;
import org.example.mes.GlobalData;
import org.example.mes.Grid;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader("./src/main/resources/Test1_4_4.txt");
        GlobalData data = new GlobalData();
        dataLoader.loadGlobalData(data);
        Grid grid = new Grid(data.getElementNumber(),data.getNodeNumber());
        //System.out.println(grid.getNodes().length);
        dataLoader.loadGrid(grid);
        dataLoader.testFile("./src/main/resources/Test1_4_4.txt");
        System.out.println(data.getSimulationSleepTime());
        System.out.println("   ");
    }
}