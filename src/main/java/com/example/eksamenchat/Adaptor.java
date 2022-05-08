package com.example.eksamenchat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class Adaptor {
    private static String url = "jdbc:sqlite:chattDB.db";
    private static Connection conn = null;
    private static String txtFil = "medlemmer.txt";
    protected static String oppdatertFilnavn = "";
    protected static ArrayList<Logg> loggInnføringer = new ArrayList<>(); // denne burde ligge i class Medlem?
    protected static ArrayList<String> alleRom = new ArrayList<>(); // denne burde ligge i class Medlem?
    protected final static ObservableList<Logg> LOGG_RADER = FXCollections.observableArrayList();
    static int sisteId;



    /**
     * Metode for å opprette tabell logg (og database, om denne ikke er opprettet)
     */
    protected static void createLogg() {
        String sql = "CREATE TABLE IF NOT EXISTS Logg " +
                "(Id INTEGER PRIMARY KEY, Tidspunkt DATETIME, Brukernavn VARCHAR(50), KlientIP VARCHAR(20), Rom VARCHAR(50)," +
                "Melding VARCHAR(50) );";
        try (Connection conn = connect() ) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }


    /**
     * Metode for å droppe tabell logg
     */
    protected static void dropLogg() {
        String sql = "DROP TABLE IF EXISTS Logg;";
        try (Connection conn = connect() ) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    /**
     * Metode for INSERT til database. Autoinkrement av ID i DB.
     * Kjører kall på selectNyeste(), som oppdaterer GUI
     * OBS! Har kuttet ut ID, slik at dette autoinkrementeres av DB
     * @param tidspunkt
     * @param bruker
     * @param klientIP
     * @param romNavn
     * @param melding
     */
    protected void insertLogg(Date tidspunkt, String bruker, String klientIP, String romNavn, String melding) {
        String sql = "INSERT INTO Logg (Tidspunkt, Brukernavn, KlientIP, Rom, Melding) VALUES (?,?,?,?,?);";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
 //           pstmt.setInt(1, id);
            pstmt.setDate(1, tidspunkt);
            pstmt.setString(2, bruker);
            pstmt.setString(3, klientIP);
            pstmt.setString(4, romNavn);
            pstmt.setString(5, melding);
            pstmt.executeUpdate();
            // Henter ut høyeste logg ID fra DB
            selectMaksId();
            // Henter de nyeste logginnføringene for visning
            selectNyeste();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    /**
     * Metode for å hente ut de nyeste logginnføringene fra DB
     * En svakhet med denne tilnærmingen er at bruker ikke kan justere LIMIT
     * Finnes nok en måte å gjøre dette på
     *
     */
    protected static void selectNyeste() {
        try (Connection conn = connect() ) {
            String sql = "select * from Logg ORDER BY id DESC LIMIT 10;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            LOGG_RADER.clear(); // sletter mellomlagring
            lagreResultat(rs); // mellomlagrer treff
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }


    /**
     * Metode for å hente ut siste logg ID fra DB
     * Benyttes i visning av logg
     */
    protected static void selectMaksId() {
        try (Connection conn = connect() ) {
            String sql = "select MAX(id) from Logg;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            sisteId = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }


    /**
     * Metode for å mellomlegre respons fra DB
     * @param rs
     */
    private static void lagreResultat(ResultSet rs) {
        try {
            while (rs.next() ) {
                int id = rs.getInt(1);
                Date tid = rs.getDate(2);
                String bruker = rs.getString(3);
                String klientIP = rs.getString(4);
                String romNavn = rs.getString(5);
                String melding = rs.getString(6);
                LOGG_RADER.add(new Logg(id, tid, bruker, klientIP, romNavn, melding));
            }
        } catch (SQLException e) {
            System.out.println("Her" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Hjelpemetode for å koble opp mot DB
     *
     * @return kobling
     */
    protected static Connection connect() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * Hjelpemetode for å koble fra DB
     */
    protected static void disConnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode for å hente ut og vise hele loggen.
     * IKKE I BRUK
     */
    protected static void selectAll() {
        try (Connection conn = connect() ) {
            String sql = "select * from Logg;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            LOGG_RADER.clear(); // sletter mellomlagring
            lagreResultat(rs); // mellomlagrer treff
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }


    /**
     * Metode for å hente ut og vise logginnføring med lik ID
     * IKKE I BRUK
     */
    protected static void selectLoggId(int id) {
        try (Connection conn = connect() ) {
            String sql = "select * from Logg WHERE id = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            LOGG_RADER.clear(); // sletter mellomlagring
            lagreResultat(rs); // mellomlagrer treff
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }
}
