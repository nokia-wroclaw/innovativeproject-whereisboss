package com.example.baksu.whereismyboss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baksu on 2014-12-08.
 */
public class Building {
    public String name;
    public List<Floor> floors = new ArrayList<Floor>();

    public Building(String name, Floor floor){
        this.name = name;
        this.floors.add(floor);
    }

    public void addFloor(Floor floor)
    {
        floors.add(floor);
    }
}
