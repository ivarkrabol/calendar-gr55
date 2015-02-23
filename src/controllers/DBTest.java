package controllers;

import exceptions.DBException;
import models.Room;

import java.io.IOException;
import java.sql.SQLException;

public class DBTest {

    public static void main(String[] args) throws IOException, SQLException, DBException {
        System.out.println(Room.getByID(3));
        System.out.println(Room.getByID(3));
        System.out.println(Room.getByID(4));
        System.out.println(Room.getByID(4));
        System.out.println(Room.getByID(5));
    }
}
