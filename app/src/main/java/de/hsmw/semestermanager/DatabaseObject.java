package de.hsmw.semestermanager;

import android.content.Context;

/**
 * Created by Benjamin on 07.12.2016.
 */

public interface DatabaseObject {

    /**
     * @return Gibt den Anzeigenamen des Datenbankobjekts zurück.
     */
    String getName();

    /**
     * @return Gibt die ID zurück unter der das Objekt in der Datenbank gespeichert ist.
     */
    int getId();

    /**
     * Löscht das DatenbankObjekt mit allen Beziehungen ohne Rückfrage aus der Datenbank.
     *
     * @param c Context für die Datenbankzugriffe.
     */
    void delete(Context c);

    /**
     * Startet einen Intent, um das Objekt zu bearbeiten.
     *
     * @param c Context für Datenbankzugriff
     */
    void edit(Context c);

}
