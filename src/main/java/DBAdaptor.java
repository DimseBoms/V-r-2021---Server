import java.sql.*;
import java.util.*;

public class DBAdaptor {

    private String url = "jdbc:sqlite:testdb2.db";
    private Connection conn = null;
    private ArrayList<String> brukere = new ArrayList<>();
    private ArrayList<String> rekker = new ArrayList<>();
    private int sisteId;
    private String telefonnummerFunnet;
    private   boolean nrEksisterer;
    private boolean ePostEksisterer;


    public DBAdaptor() {
        this.nrEksisterer = false;
        this.ePostEksisterer = false;
        this.createBruker();
        this.createLottorekke();
    }

    /**
     * Hjelpemetode for å koble opp mot DB
     *
     * @return kobling
     */
    protected Connection connect() {
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
    protected void disConnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode for å opprette tabell for brukere
     */
    protected void createBruker() {
        String sql = "CREATE TABLE IF NOT EXISTS Bruker " +
                "(Id INTEGER PRIMARY KEY, Fornavn varchar(20), Etternavn varchar(20)," +
                "Telefon varchar(16) NOT NULL, Epost varchar(50) );";
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


    protected void insertBruker(String fornavn, String etternavn, String telefon, String epost) {
        String sql = "INSERT INTO Bruker (Fornavn, Etternavn, Telefon, Epost) VALUES (?,?,?,?);";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            //           pstmt.setInt(1, id);
            pstmt.setString(1, fornavn);
            pstmt.setString(2, etternavn);
            pstmt.setString(3, telefon);
            pstmt.setString(4, epost);
            pstmt.executeUpdate();
            // Henter ut høyeste logg ID fra DB
            //        selectMaksId();
            // Henter de nyeste logginnføringene for visning
            //        selectNyeste();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    protected void selectAlleBrukere() {
        try (Connection conn = connect() ) {
            String sql = "select * from Bruker;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //       LOGG_RADER.clear(); // sletter mellomlagring
            lagreBrukere(rs); // mellomlagrer treff
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    /**
     * Metode for å kontrollere et telefonnummer mot tlf i DB
     * Dersom count != 0 finnes nummer fra før
     * @param tlf = nytt telefonnr som skal kontrolleres mot DB
     */
    protected void selectTelefonnummer(String tlf) {
        try (Connection conn = connect() ) {
            String sql = "select COUNT(*) from Bruker WHERE Telefon = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tlf);
            ResultSet rs = pstmt.executeQuery();
            int count = rs.getInt(1);
            if(count>0) {
                this.nrEksisterer = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    /**
     * Metode for å kontrollere et telefonnummer mot tlf i DB
     * Dersom count != 0 finnes nummer fra før
     * @param epost = ny epost som skal kontrolleres mot DB
     */
    protected void selectEpost(String epost) {
        try (Connection conn = connect() ) {
            String sql = "select COUNT(*) from Bruker WHERE Epost = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, epost);
            ResultSet rs = pstmt.executeQuery();
            int count = rs.getInt(1);
            if(count> 0){
                this.ePostEksisterer=true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    protected int selectBrukerId(String epost, String tlf) {
        int id=0;
        try (Connection conn = connect() ) {
            String sql = "select Id from Bruker WHERE Epost = ? AND Telefon = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, epost);
            pstmt.setString(2, tlf);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return id;

    }

    protected boolean sjekkSamsvar(String epost, String tlf) {
        try (Connection conn = connect() ) {
            String sql = "select COUNT(*) from Bruker WHERE Epost = ? AND Telefon = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, epost);
            pstmt.setString(2, tlf);
            ResultSet rs = pstmt.executeQuery();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return false;
    }

    private void lagreBrukere(ResultSet rs) {
        try {
            while (rs.next() ) {
                int id = rs.getInt(1); // TRENGER VI BRUKERID ID VISNING?
                String fornavn = rs.getString(2);
                String etternavn = rs.getString(3);
                String telefonnummer = rs.getString(4);
                String epost = rs.getString(5);
                new Bruker(fornavn, etternavn, telefonnummer, epost);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Metode for å opprette tabell for rekker (pr bruker)
     * Bør man velge å legge inn en rekke som Array/BLOB, eller som 7 heltall?
     * SQLite har ingen egen datatype for array, men BLOB vil fungere.
     * Tror separate heltall vil være enklere å forholde seg til videre i prosjektet.
     * Konklusjon: Vi velger å etablere en rekke som 7 separerte heltall i hver sin kolonne.
     * Hver rad i tabellen representerer en enkel rekke fra en bestemt bruker
     * Dersom bruker oppretter flere rekker i samme request, blir hver rekke lagt på egen rad i DB
     * 1: Id
     * 2: Bruker (BrukerId)
     * 3: DatoOgTid
     * 4: antallRette
     * 5: Tall_1
     * 6: Tall_2
     * 7: ...
     */
    protected void createLottorekke() {
        String sql = "CREATE TABLE IF NOT EXISTS Lottorekke " +
                "(Id INTEGER PRIMARY KEY, BrukerId INTEGER, DatoOgTid DATETIME, AntallRette INTEGER, Tall_1 INTEGER," +
                "Tall_2 INTEGER, Tall_3 INTEGER, Tall_4 INTEGER, Tall_5 INTEGER, Tall_6 INTEGER, Tall_7 INTEGER );";
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

    protected void insertLottorekke(int brukerId, Timestamp tidspunkt, int antallRette, int t1, int t2, int t3, int t4, int t5, int t6, int t7) {
        String sql = "INSERT INTO Lottorekke (BrukerId, DatoOgTid, AntallRette, Tall_1, Tall_2, Tall_3, Tall_4, Tall_5, Tall_6, Tall_7 ) VALUES (?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            //           pstmt.setInt(1, id);
            pstmt.setInt(1, brukerId);
            pstmt.setTimestamp(2, tidspunkt);
            pstmt.setInt(3, antallRette);
            pstmt.setInt(4, t1);
            pstmt.setInt(5, t2);
            pstmt.setInt(6, t3);
            pstmt.setInt(7, t4);
            pstmt.setInt(8, t5);
            pstmt.setInt(9, t6);
            pstmt.setInt(10, t7);

            pstmt.executeUpdate();
            // Henter ut høyeste logg ID fra DB
            //        selectMaksId();
            // Henter de nyeste logginnføringene for visning
            //        selectNyeste();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    protected void selectAlleLottorekker() {
        try (Connection conn = connect() ) {
            String sql = "select * from Lottorekke;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //       LOGG_RADER.clear(); // sletter mellomlagring
            lagreLottorekker(rs); // mellomlagrer treff
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }

    private void lagreLottorekker(ResultSet rs) {
        try {
            while (rs.next() ) {
                int id = rs.getInt(1); // TRENGER VI BRUKERID ID VISNING?
                int brukerId = rs.getInt(2);
                Timestamp tidspunkt = rs.getTimestamp(3);
                int antallRette = rs.getInt(4);
                int t1 = rs.getInt(5);
                int t2 = rs.getInt(6);
                int t3 = rs.getInt(7);
                int t4 = rs.getInt(8);
                int t5 = rs.getInt(9);
                int t6 = rs.getInt(10);
                int t7 = rs.getInt(11);
                new Lottorekke(brukerId, tidspunkt, antallRette, t1, t2, t3, t4, t5, t6, t7);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode for å droppe tabell Bruker
     */
    protected void dropBruker() {
        String sql = "DROP TABLE IF EXISTS Bruker;";
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
     * Metode for å droppe tabell Lottorekke
     */
    protected void dropLottorekke() {
        String sql = "DROP TABLE IF EXISTS Lottorekke;";
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
     * Metode for å hente ut siste brukerID fra tabell Bruker i DB
     */
    private void selectMaksId() {
        try (Connection conn = connect() ) {
            String sql = "select MAX(id) from Bruker;";
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

    private int getSisteId() {return this.sisteId;}

    private String getTelefonnummerFunnet() {return this.telefonnummerFunnet;}

    public  boolean isNrEksisterer() {
        return nrEksisterer;
    }

    public  boolean isEpostEksisterer() {
        return ePostEksisterer;
    }

}