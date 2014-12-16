package com.example.baksu.whereismyboss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baksu on 2014-12-08.
 */
public class Building {
    public String name;
    public String id;
    public Floor[] floors;

    public Building(String name,String id,  Floor[] floor){
        this.name = name;
        this.floors = floor;
        this.id = id;
    }

}
