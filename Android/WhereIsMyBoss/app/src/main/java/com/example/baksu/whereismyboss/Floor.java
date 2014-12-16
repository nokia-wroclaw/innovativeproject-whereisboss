package com.example.baksu.whereismyboss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baksu on 2014-11-25.
 */
public class Floor {
    public String name;
    public String id;
    public Room[] rooms;

    public Floor(String name, String id, Room[] rooms){
        this.name = name;
        this.rooms = rooms;
        this.id = id;
    }
}
