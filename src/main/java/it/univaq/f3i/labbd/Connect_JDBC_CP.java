package it.univaq.f3i.labbd;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author giuse
 */
public class Connect_JDBC_CP {

    //su richiesta, possiamo anche lavorare in modalità singleton e gestire
    //una connessione locale unica (sempre prelvata dal pool)
    private Connection connection;
    //il pool delle connessioni
    private final BasicDataSource dataSource;

    public Connect_JDBC_CP(String connection_string, String username, String password) {
        //creaimo il più semplice dei DataSource
        //per la configurazione completa si veda 
        //https://commons.apache.org/proper/commons-dbcp/apidocs/org/apache/commons/dbcp2/BasicDataSource.html
        dataSource = new BasicDataSource();
        dataSource.setUrl(connection_string);
        if (username != null && password != null) {
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        }
        //configuriamo alcuni parametri relativi al numero di connessioni da tenere nel pool
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxTotal(25);
        //è possibile anche usare un PoolingDataSource, più complesso da configurare
        //ma più versatile
        //
        this.connection = null;

    }

    //creiamo e restituiamo una singola connessione locale (singleton)
    //(che potrà essere chiusa da questo modulo)
    public Connection getConnection() throws ApplicationException {
        if (connection == null) {
            connection = connect();
        }
        return connection;
    }

    //questo metodo restituisce una nuova connessione a ogni chiamata
    //(che andrà chiusa dal ricevente!)
    public Connection newConnection() throws ApplicationException {
        return connect();

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    //connessione al database
    private Connection connect() throws ApplicationException {
        System.out.println("\n**** APERTURA CONNESSIONE ***************************");
        try {
            //connessione al database 
            return dataSource.getConnection();
        } catch (SQLException ex) {
            //Usiamo un'eccezione user-defined per trasportare e gestire più
            //agevolmente tutte le eccezioni lagate all'uso del database
            throw new ApplicationException("Errore di connessione", ex);
        }
    }

    //disconnessione della connessione locale (singleton) se presente
    public void disconnect() throws ApplicationException {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                System.out.println("\n**** CHIUSURA CONNESSIONE (modulo connect) **********");
                this.connection.close();
            }
        } catch (SQLException ex) {
            throw new ApplicationException("Errore di disconnessione", ex);
        }
    }

}
