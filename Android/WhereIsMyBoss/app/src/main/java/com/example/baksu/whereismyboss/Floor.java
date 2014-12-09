package com.example.baksu.whereismyboss;

import java.util.List;

/**
 * Created by Baksu on 2014-11-25.
 */
public class Floor {
    public String name;
    public List<String> rooms;

    public Floor(String name, List<String> rooms){
        this.name = name;
        this.rooms = rooms;
    }
}
