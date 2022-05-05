package com.example.eksamenchat;

import java.sql.*;
import java.util.ArrayList;

public class Adaptor {
    private static String url = "jdbc:sqlite:chattDB.db";
    private static Connection conn = null;
    private static String txtFil = "medlemmer.txt";
    protected static String oppdatertFilnavn = "";
    protected static ArrayList<Logg> loggInnføringer = new ArrayList<>(); // denne burde ligge i class Medlem?


    private Adaptor() {

    }

    /**
     *
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
     * Metode for å droppe de tre tabellene:
     *  - Medlem (medlemsregister PlattFot)
     *  - Kontingent (innbetalinger)
     *  - Deltagelse (deltagelser årlig tur)
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
     * OBS! Har kuttet ut ID, slik at dette autoinkrementeres av DB
     * @param tidspunkt
     * @param bruker
     * @param klientIP
     * @param romNavn
     * @param melding
     */
    protected static void insertLogg(Date tidspunkt, String bruker, String klientIP, String romNavn, String melding) {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    protected static void selectAll() {
        try (Connection conn = connect() ) {
            String sql = "select * from Logg;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //    loggInnføringer.clear(); // sletter mellomlagring
            lagreLogg(rs); // mellomlagrer treff

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    protected static void selectLoggId(int id) {
        try (Connection conn = connect() ) {
            String sql = "select * from Logg WHERE id = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
        //    loggInnføringer.clear(); // sletter mellomlagring
            lagreLogg(rs); // mellomlagrer treff

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    private static void lagreLogg(ResultSet rs) {
        try {
            while (rs.next() ) {
                int id = rs.getInt(1);
                Date tid = rs.getDate(2);
                String bruker = rs.getString(3);
                String klientIP = rs.getString(4);
                String romNavn = rs.getString(5);
                String melding = rs.getString(6);
                loggInnføringer.add(new Logg(id, bruker, klientIP, romNavn, melding));
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
}
