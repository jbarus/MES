package org.example.mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class DataLoader {
    Scanner scanner;

    public DataLoader(String path) {
        File file = new File(path);
        try {
            this.scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGlobalData(GlobalData data) throws IOException {
        scanner.next();
        data.setSimulationTime(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setSimulationSleepTime(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setConductivity(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setAlfa(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setTot(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setInitialTemp(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setDensity(Integer.parseInt(scanner.next()));
        scanner.next();
        data.setSpecificHeat(Integer.parseInt(scanner.next()));
        scanner.next();
        scanner.next();
        data.setNodeNumber(Integer.parseInt(scanner.next()));
        scanner.next();
        scanner.next();
        data.setElementNumber(Integer.parseInt(scanner.next()));


    }

    public void loadGrid(Grid grid){
        scanner.next();
        int i = 0;
        Node[] nodes = grid.getNodes();

        while(i<(grid.getNodes().length)*3){
            if(i%3!=0){
                Node node = new Node();
                node.setX(Double.parseDouble(scanner.next().replace(",","")));
                node.setY(Double.parseDouble(scanner.next().replace(",","")));
                nodes[i/3] = node;
                i+=2;
            }
            else{
                scanner.next();
                i++;
            }

        }
        scanner.next();
        scanner.next();
        Element[] elements = grid.getElements();
        int j = 0;
        while(j<(grid.getElements().length)*5){
            if(j%5!=0){
                Element element = new Element();
                element.setID(new double[4]);
                element.ID[0] = Double.parseDouble(scanner.next().replace(",",""));
                element.ID[1] = Double.parseDouble(scanner.next().replace(",",""));
                element.ID[2] = Double.parseDouble(scanner.next().replace(",",""));
                element.ID[3] = Double.parseDouble(scanner.next().replace(",",""));

                elements[j/5] = element;
                j+=4;
            }
            else{
                scanner.next();
                j++;
            }
        }

    }

    public void testFile(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()){
            System.out.println(scanner.next());
        }
    }
}
