package org.example.mes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Grid {
    private Element[] elements;
    private Node[] nodes;

    public Grid(int nE, int nN) {
        elements = new Element[nE];
        nodes = new Node[nN];
    }
}
