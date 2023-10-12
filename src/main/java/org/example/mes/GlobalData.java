package org.example.mes;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GlobalData {
    private int simulationTime;
    private int simulationSleepTime;
    private int conductivity;
    private int alfa;
    private int tot;
    private int initialTemp;
    private int density;
    private int specificHeat;
    private int elementNumber;
    private int nodeNumber;
}
