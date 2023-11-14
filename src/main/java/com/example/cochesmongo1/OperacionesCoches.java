package com.example.cochesmongo1;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class OperacionesCoches {

    private MongoCollection<Document> cochesCollection;

    public OperacionesCoches(MongoCollection<Document> cochesCollection) {
        this.cochesCollection = cochesCollection;
    }

    public List<Coche> obtenerCoches() {
        List<Coche> almacenados = new ArrayList<>();
        MongoCursor<Document> resultSet = cochesCollection.find().iterator();

        while (resultSet.hasNext()) {
            Document doc = resultSet.next();
            String matricula = doc.getString("matricula");
            String marca = doc.getString("marca");
            String modelo = doc.getString("modelo");
            String tipo = doc.getString("tipo");

            Coche almacenado = new Coche(matricula, marca, modelo, tipo);
            almacenados.add(almacenado);
        }

        return almacenados;
    }

    public void eliminarCoche(String matricula) {
        Bson filter = Filters.eq("matricula", matricula);
        cochesCollection.deleteOne(filter);
    }

    public void guardarCoche(String matricula, String marca, String modelo, String tipo) {
        if (verificarMatricula(matricula)) {
            // Manejar error de matrícula duplicada
            return;
        }

        Document nuevoCoche = new Document()
                .append("matricula", matricula)
                .append("marca", marca)
                .append("modelo", modelo)
                .append("tipo", tipo);

        cochesCollection.insertOne(nuevoCoche);
    }

    public void modificarCoche(String matricula, String nuevaMarca, String nuevoModelo, String nuevoTipo) {
        Bson filtro = Filters.eq("matricula", matricula);
        Document cocheExistente = cochesCollection.find(filtro).first();

        if (cocheExistente != null) {
            Document newDatos = new Document()
                    .append("marca", nuevaMarca)
                    .append("modelo", nuevoModelo)
                    .append("tipo", nuevoTipo);

            cochesCollection.updateOne(filtro, new Document("$set", newDatos));
        } else {
            // Manejar error de coche no encontrado
        }
    }

    public boolean verificarMatricula(String matricula) {
        Bson filter = Filters.eq("matricula", matricula);
        return cochesCollection.find(filter).iterator().hasNext();
    }
}
