package org.example.mes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Grid {
    private Element[] elements;
    private Node[] nodes;

    public Grid(int nE, int nN) {
        elements = new Element[nE];
        nodes = new Node[nN];
    }
}
