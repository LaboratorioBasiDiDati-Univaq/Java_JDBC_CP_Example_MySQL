package it.univaq.f3i.labbd;

/**
 *
 * @author Giuseppe Della Penna
 *
 * In questo esempio l'accesso al DBMS viene effttuato tramite connection
 * pooling con Apache DBCP. Ogni modulo dell'applicazione lavora su una
 * connessione separata. Da notare che questo ha poco senso se l'applicazione
 * non esegue operazioni parallele sulla base di dati, ma l'esempio ha il solo
 * scopo di mostrare come si implementa il connection pooling in un'applicazione
 * JavaSE, anche se qui è utile/necessario tipicamente solo se l'applicazione è
 * multithreaded.
 *
 * Questo esempio lavora sul database "campionati" e richiede che esso sia
 * popolato con i dati e le procedure sviluppate a lezione, nonchè che sia
 * presente nel DBMS un utente specifico (vedi qui sotto) con accesso al
 * database.
 *
 * Il codice è può ricreare il database e popolarlo, ma in questo caso è
 * necessario che l'utente con cui si accede abbia i privilegi globali SUPER
 * (per creare le funzioni) nonchè quelli di creazione tabelle, procedure e
 * foreign key sul datbase campionati
 */
public class JDBC_Example_MySQL_Main extends JDBC_Example {

    //attenzione: a partire dal connector/J 8 il nome del driver è cambiato
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "campionati";
    //il parametro noAccessToProcedureBodies nella connection string è usato per invocare le procedure senza avere permessi avanzati
    //i parametri connectionTimeZone e forceConnectionTimeZoneToSession servono ad allineare la timezone del driver con quella impostata sul server
    //vedi https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-time-instants.html
    private static final String CONNECTION_STRING
            = "jdbc:mysql://localhost:3306/" + DB_NAME + "?noAccessToProcedureBodies=true" + "&connectionTimeZone=LOCAL&forceConnectionTimeZoneToSession=false";
    //l'utente deve avere i permessi INSERT, UPDATE, SELECT ed EXECUTE sul database
    private static final String DB_USER = "campionatiUser";
    private static final String DB_PASSWORD = "campionatiPwd";

    public JDBC_Example_MySQL_Main() {
        super(CONNECTION_STRING, DB_USER, DB_PASSWORD);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JDBC_Example_MySQL_Main instance = new JDBC_Example_MySQL_Main();
        //impostare il primo parametro a true per ricreare l'intero database, comprese le procedure
        //in questo caso però sono necessari ulteriori privilegi per l'utente connesso
        instance.run(false, true, true);
    }
}
