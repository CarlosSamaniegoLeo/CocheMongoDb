/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.cochesmongo1;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class ConnectionDB {
    public static MongoClient conectar()
    {

        try {

            final MongoClient conexion = new MongoClient(new MongoClientURI("mongodb://root:password@localhost:27017/?authSource=admin"));

            System.out.println("Conectada correctamente a la BD");

            return conexion;
        }
        catch (Exception e) {
            System.out.println("Conexion Fallida");
            System.out.println(e);
            return null;
        }
    }
    public static void desconectar(MongoClient con)
    {
        con.close();
    }


}
