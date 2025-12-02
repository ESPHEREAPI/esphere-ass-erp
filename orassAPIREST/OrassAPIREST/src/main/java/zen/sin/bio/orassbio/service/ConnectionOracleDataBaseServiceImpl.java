/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import zen.sin.bio.orassbio.dtos.RenouvellementDto;
import zen.sin.bio.orassbio.entites.Expirations;
import zen.sin.bio.orassbio.entites.Expirations04;
import zen.sin.bio.orassbio.entites.Expirations30;
import zen.sin.bio.orassbio.entites.SmsProduction;
import zen.sin.bio.orassbio.entites.ZenEvenement;
import zen.sin.bio.orassbio.entites.ZenGarantie;
import zen.sin.bio.orassbio.entites.ZenRenouvellement;
import zen.sin.bio.orassbio.entites.ZenSinistreLn;
import zen.sin.bio.orassbio.exception.GlobalException;
import zen.sin.bio.orassbio.mappers.BiometrieMapperImpl;

/**
 *
 * @author USER01
 */
//@Service
@Component
@AllArgsConstructor
public class ConnectionOracleDataBaseServiceImpl implements ConnectionOracleDataBaseService {

    private BiometrieMapperImpl mappers;

    private static java.sql.Connection con;

    @Override
    public java.sql.Connection connectionOracle() {
        java.sql.Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@35.204.126.17:1521/ZINSDB", "ADMINBIO2", "ADMINBIO2");
//            String dbURL3 = "jdbc:oracle:oci:@ZINSDB";
//            Properties properties = new Properties();
//            properties.put("user", "ADMINBIO2");
//            properties.put("password", "ADMINBIO2");
//            properties.put("defaultRowPrefetch", "20");
//ip 82.165.166.219
//            connection = DriverManager.getConnection(dbURL3, properties);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
//        catch (ClassNotFoundException ex) {
//            Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return connection;
    }

    @Override
    public List<Expirations> vuesExpiration() {
        List<Expirations> listeExpirations = new ArrayList<>();
        try {

            Expirations expirations = null;
            if (this.con == null) {
                this.con = connectionOracle();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Expirations ");
            while (rs.next()) {
                expirations = new Expirations();

                expirations.setTELEASSU(rs.getString(1));
                expirations.setCODEINTE(rs.getInt(2));
                expirations.setNUMEPOLI(rs.getLong(3));
                expirations.setNUMEIMMA(rs.getString(4));
                expirations.setNUMEAVEN(rs.getInt(5));
                expirations.setNOMASSURE(rs.getString(6));
                expirations.setDATEECHE(rs.getDate(7));
                expirations.setLANGUE(rs.getString(8));
                listeExpirations.add(expirations);
                System.out.println(expirations);
            }
            listeExpirations.forEach(System.out::print);

        } catch (SQLException ex) {
            this.deconect();
            // listeExpirations = this.vuesExpiration();
            Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeExpirations;
    }

    @Override
    public List<Expirations30> vuesExpiration30() {
        List<Expirations30> listeExpirations = new ArrayList<>();
        try {

            Expirations30 expirations = null;
            if (this.con == null) {
                this.con = connectionOracle();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Expirations30 ");
            while (rs.next()) {
                expirations = new Expirations30();

                expirations.setTELEASSU(rs.getString(1));
                expirations.setCODEINTE(rs.getInt(2));
                expirations.setNUMEPOLI(rs.getLong(3));
                expirations.setNUMEIMMA(rs.getString(4));
                expirations.setNUMEAVEN(rs.getInt(5));
                expirations.setNOMASSURE(rs.getString(6));
                expirations.setDATEECHE(rs.getDate(7));
                expirations.setLANGUE(rs.getString(8));
                listeExpirations.add(expirations);
                System.out.println(expirations);
            }
            listeExpirations.forEach(System.out::print);

        } catch (SQLException ex) {
            this.deconect();
            // listeExpirations = this.vuesExpiration30();
            Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeExpirations;

    }

    @Override
    public List<Expirations04> vuesExpiration04() {
        List<Expirations04> listeExpirations = new ArrayList<>();
        try {

            Expirations04 expirations = null;
            if (this.con == null) {
                this.con = connectionOracle();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Expirations04 ");
            while (rs.next()) {
                expirations = new Expirations04();

                expirations.setTELEASSU(rs.getString(1));
                expirations.setCODEINTE(rs.getInt(2));
                expirations.setNUMEPOLI(rs.getLong(3));
                expirations.setNUMEIMMA(rs.getString(4));
                expirations.setNUMEAVEN(rs.getInt(5));
                expirations.setNOMASSURE(rs.getString(6));
                expirations.setDATEECHE(rs.getDate(7));
                expirations.setLANGUE(rs.getString(8));
                listeExpirations.add(expirations);
                System.out.println(expirations);
            }
            listeExpirations.forEach(System.out::print);

        } catch (SQLException ex) {
            this.deconect();
            //listeExpirations = this.vuesExpiration04();
            Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listeExpirations;

    }

    @Override
    public List<SmsProduction> vuesSmsProduction() {

        List<SmsProduction> listesmsProduction = new ArrayList<>();
        SmsProduction smsProduction = null;
        try {
            if (this.con == null) {
                this.con = connectionOracle();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM SMS_PRODUCTION ");
            while (rs.next()) {
                smsProduction = new SmsProduction();
                smsProduction.setBRANCHE(rs.getNString("BRANCHE"));
                smsProduction.setBUREAU(rs.getString("BUREAU"));
                smsProduction.setCHARGER_CLI(rs.getString("CHARGER_CLI"));
                smsProduction.setCHARGER_CLI_TEL(rs.getString("CHARGER_CLI_TEL"));
                smsProduction.setCODECATE(rs.getInt("CHARGER_CLI_TEL"));
                smsProduction.setCODEINTE(rs.getInt("CODEINTE"));
                smsProduction.setDATEECHE(rs.getDate("DATEECHE"));
                smsProduction.setDATEEFFE(rs.getDate("DATEEFFE"));
                smsProduction.setDATEEMIS(rs.getDate("DATEEMIS"));
                smsProduction.setLANGUE(rs.getString("LANGUE"));
                smsProduction.setLIBECATE(rs.getString("LIBECATE"));
                smsProduction.setNOMASSURE(rs.getString("NOMASSURE"));
                smsProduction.setNUMEAVEN(rs.getInt("NUMEAVEN"));
                smsProduction.setNUMEPOLI(rs.getLong("NUMEPOLI"));
                smsProduction.setPRIMNETT(rs.getLong("PRIMNETT"));
                smsProduction.setPRIMTOTA(rs.getLong("PRIMTOTA"));
                smsProduction.setTELEASSU(rs.getString("TELEASSU"));
                listesmsProduction.add(smsProduction);

            }
            listesmsProduction.forEach(System.out::print);
        } catch (SQLException e) {
            e.printStackTrace();
            this.deconect();
            listesmsProduction = this.vuesSmsProduction();

        }
        return listesmsProduction;
    }

    @Override
    public List<ZenRenouvellement> listeRenouvellement() {
        List<ZenRenouvellement> listeRenouvellement = new ArrayList<>();
        ZenRenouvellement zenRenouvellement = null;
        try {
            if (this.con == null) {
                this.con = connectionOracle();
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM ZEN_RENOUVELLEMENT ");
            while (rs.next()) {
                zenRenouvellement = new ZenRenouvellement();
                zenRenouvellement.setREF_DIGITAL(rs.getString(1));
                zenRenouvellement.setPOLICEAV(rs.getString(2));
                zenRenouvellement.setCODEINTE(rs.getInt(3));
                zenRenouvellement.setNUMEPOLI(rs.getLong(4));
                zenRenouvellement.setNUMEAVEN(rs.getInt(5));
                zenRenouvellement.setTYPE_AVENANT(rs.getString(6));
                zenRenouvellement.setDATEEFFE(rs.getDate("DATEEFFE"));
                zenRenouvellement.setDATEECHE(rs.getDate("DATEECHE"));
                zenRenouvellement.setCODECATE(rs.getInt(9));
                zenRenouvellement.setLIBECATE(rs.getString(10));
                zenRenouvellement.setNOM(rs.getString(12));
                zenRenouvellement.setTELEASSU(rs.getString(13));
                zenRenouvellement.setVILLE(rs.getString(14));
                zenRenouvellement.setENERGIE(rs.getString(15));
                zenRenouvellement.setPUISSANCE_FISC(rs.getInt(16));
                zenRenouvellement.setMERQUEVEHI(rs.getString(17));
                zenRenouvellement.setTYPEVEHI(rs.getString(18));
                zenRenouvellement.setPOIDS(rs.getInt(19));
                zenRenouvellement.setNBREPLACE(rs.getInt(20));
                zenRenouvellement.setNBRECARTE(rs.getNString(21));
                zenRenouvellement.setANNEPERCOND(rs.getString(22));
                zenRenouvellement.setDATENAISCOND(rs.getDate(23));
                zenRenouvellement.setDATEMISECIRC(rs.getDate(24));
                zenRenouvellement.setPOSSEDEVIGNETTE(rs.getString(25));
                zenRenouvellement.setDUREE_COURANTE(rs.getInt(26));
                zenRenouvellement.setDUREETOTALE(rs.getInt(27));
                zenRenouvellement.setAVECREMORQ(rs.getString(28));
                zenRenouvellement.setLIQUIDINFLAM(rs.getString(29));
                zenRenouvellement.setUSAGE(rs.getString(30));
                zenRenouvellement.setNUMEIMMA(rs.getString(31));
                zenRenouvellement.setDOUBLECOMMANDE(rs.getString(33));
                zenRenouvellement.setPRIMNETT(rs.getLong(34));
                zenRenouvellement.setPRIMTOTA(rs.getLong(35));
                listeRenouvellement.add(zenRenouvellement);

            }
            listeRenouvellement.forEach(System.out::print);
        } catch (SQLException e) {
            e.printStackTrace();
            this.deconect();
            // listeRenouvellement = this.listeRenouvellement();

        }
        return listeRenouvellement;
    }

    @Override
    public List<ZenRenouvellement> listeRenouvellement(int police) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public RenouvellementDto listeRenouvellementByPolice(String police) {
        if (police.contains("-") == false) {
            return new RenouvellementDto();
        }
        ZenRenouvellement zenRenouvellement = null;
        List<ZenRenouvellement> listeZenRenouvellements = new ArrayList<>();
        RenouvellementDto renouvellementDto = null;
        List<ZenGarantie> listeGaranties = new ArrayList<>();
        List<Integer> listeAvenants = new ArrayList<>();
        ZenGarantie zenGarantie = null;
        try {
            if (this.con == null) {
                this.con = connectionOracle();

            }

            String[] data = police.split("-");
            int agence = 0;
            int num_police = 0;
            if (data.length > 0) {
                agence = Integer.parseInt(data[0]);
                num_police = Integer.parseInt(data[1]);
            }
            Statement stmt = con.createStatement();
            String q1 = "select * from ZEN_RENOUVELLEMENT WHERE NUMEPOLI = '" + num_police + "' AND CODEINTE = '" + agence + "' ORDER BY NUMEAVEN ASC";
            ResultSet rs = stmt.executeQuery(q1);
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.println("COLONNE " + i + " : " + meta.getColumnName(i));
            }

            while (rs.next()) {
                //System.out.println(rs.getMetaData().getColumnTypeName(39));

                zenRenouvellement = new ZenRenouvellement();
                zenRenouvellement.setREF_DIGITAL(rs.getString(1));
                zenRenouvellement.setPOLICEAV(rs.getString(2));
                zenRenouvellement.setCODEINTE(rs.getInt(3));
                zenRenouvellement.setNUMEPOLI(rs.getLong(4));
                zenRenouvellement.setNUMEAVEN(rs.getInt(5));
                zenRenouvellement.setTYPE_AVENANT(rs.getString(6));
                zenRenouvellement.setDATEEFFE(rs.getDate(7));
                zenRenouvellement.setDATEECHE(rs.getDate(8));
                zenRenouvellement.setCODECATE(rs.getInt(9));
                zenRenouvellement.setLIBECATE(rs.getString(10));
                zenRenouvellement.setNOM(rs.getString(12));
                zenRenouvellement.setTELEASSU(rs.getString(13));
                zenRenouvellement.setVILLE(rs.getString(14));
                zenRenouvellement.setENERGIE(rs.getString(15));
                zenRenouvellement.setPUISSANCE_FISC(rs.getInt(16));
                zenRenouvellement.setMERQUEVEHI(rs.getString(17));
                zenRenouvellement.setTYPEVEHI(rs.getString(18));
                zenRenouvellement.setPOIDS(rs.getInt(19));
                zenRenouvellement.setNBREPLACE(rs.getInt(20));
                zenRenouvellement.setNBRECARTE(rs.getNString(21));
                zenRenouvellement.setANNEPERCOND(rs.getString(22));
                zenRenouvellement.setDATENAISCOND(rs.getDate(23));
                zenRenouvellement.setDATEMISECIRC(rs.getDate(24));
                zenRenouvellement.setPOSSEDEVIGNETTE(rs.getString(25));
                zenRenouvellement.setDUREE_COURANTE(rs.getInt(26));
                zenRenouvellement.setDUREETOTALE(rs.getInt(27));
                zenRenouvellement.setAVECREMORQ(rs.getString(28));
                zenRenouvellement.setLIQUIDINFLAM(rs.getString(29));
                zenRenouvellement.setUSAGE(rs.getString(30));
                zenRenouvellement.setNUMEIMMA(rs.getString(31));
                zenRenouvellement.setDOUBLECOMMANDE(rs.getString(33));
                zenRenouvellement.setPRIMNETT(rs.getLong(34));
                zenRenouvellement.setPRIMTOTA(rs.getLong(35));
                zenRenouvellement.setGENRE(rs.getString(36));
                  zenRenouvellement.setZONE_(rs.getString(37));
                zenRenouvellement.setSOUS_CATEGORIE(rs.getString(38));
                zenRenouvellement.setAVEC_RC_ELEVE(rs.getString(39));
                zenRenouvellement.setCYLINDREE(rs.getString(40));
                zenRenouvellement.setNOM_BUREAU(rs.getString(41));

                //zenRenouvellement.setCYLINDREE(rs==null ? "" : rs.getString(39));
//                zenRenouvellement.setZONE_(rs.getString("ZONE_"));
//                zenRenouvellement.setSOUS_CATEGORIE(rs.getString("SOUS_CATEGORIE"));
//                zenRenouvellement.setAVEC_RC_ELEVE(rs.getString("AVEC_RC_ELEVE"));
//                zenRenouvellement.setNOM_BUREAU(rs.getString("NOM_BUREAU"));
//                zenRenouvellement.setCYLINDREE(rs.getString("CYLINDREE"));
//                zenRenouvellement.setGENRE(rs.getString("GENRE"));
                listeZenRenouvellements.add(zenRenouvellement);
                if (zenRenouvellement.getNUMEAVEN() != null) {
                    listeAvenants.add(zenRenouvellement.getNUMEAVEN());
                }

            }
            if (listeZenRenouvellements.size() > 1) {
                int maxAvenant = listeAvenants.stream()
                        .mapToInt(Integer::intValue)
                        .max()
                        .orElse(-1);
                if (maxAvenant != -1) {
                    zenRenouvellement = listeZenRenouvellements.stream()
                            .filter(zen -> zen.getNUMEAVEN() == maxAvenant)
                            .collect(Collectors.toList())
                            .get(0);

                }

            }
            ////            listeRenouvellement.forEach(System.out::print);
//            Statement stmt2 = con.createStatement();
            if (zenRenouvellement != null && zenRenouvellement.getNUMEAVEN() != null) {

                q1 = "SELECT * FROM ZEN_GARANTIE  WHERE NUMEPOLI = '" + num_police + "' AND CODEINTE = '" + agence + "'";
                rs = stmt.executeQuery(q1);
                while (rs.next()) {
                    zenGarantie = new ZenGarantie();
                    zenGarantie.setCODEGARA(rs.getNString("CODEGARA"));
                    zenGarantie.setLIBEGARA(rs.getNString("LIBEGARA"));
                    zenGarantie.setMONTGARA(rs.getLong("MONTGARA"));
                    listeGaranties.add(zenGarantie);
                }

            } else if (zenRenouvellement != null) {

                q1 = "SELECT * FROM ZEN_GARANTIE  WHERE NUMEPOLI = '" + num_police + "' AND CODEINTE = '" + agence + "' AND AVENMODI= '" + zenRenouvellement.getNUMEAVEN() + "'";
                rs = stmt.executeQuery(q1);
                while (rs.next()) {
                    zenGarantie = new ZenGarantie();
                    zenGarantie.setCODEGARA(rs.getNString("CODEGARA"));
                    zenGarantie.setLIBEGARA(rs.getNString("LIBEGARA"));
                    zenGarantie.setMONTGARA(rs.getLong("MONTGARA"));
                    listeGaranties.add(zenGarantie);
                }

            }
            renouvellementDto = zenRenouvellement == null ? new RenouvellementDto() : mappers.formOZenRenouvellement(zenRenouvellement, listeGaranties);
        } catch (SQLException e) {
            e.printStackTrace();
            this.deconect();
            //  renouvellementDto = this.listeRenouvellementByPolice(police);

        }

        return renouvellementDto;
    }

    @Override
    public List<ZenRenouvellement> listeRenouvellementAllPolice(String police) {
        if (police.contains("-") == false) {
            return null;
        }
        ZenRenouvellement zenRenouvellement = null;
        RenouvellementDto renouvellementDto = null;
        List<ZenRenouvellement> listesRenouvellement = new ArrayList<>();
        ZenGarantie zenGarantie = null;
        try {
            if (this.con == null) {
                this.con = connectionOracle();

            }

            String[] data = police.split("-");
            int agence = 0;
            int num_police = 0;
            if (data.length > 0) {
                agence = Integer.parseInt(data[0]);
                num_police = Integer.parseInt(data[1]);
            }
            Statement stmt = con.createStatement();
            String q1 = "select * from ZEN_RENOUVELLEMENT  WHERE NUMEPOLI = '" + num_police + "' AND CODEINTE = '" + agence + "'";
            ResultSet rs = stmt.executeQuery(q1);

            while (rs.next()) {

                zenRenouvellement = new ZenRenouvellement();
                zenRenouvellement.setREF_DIGITAL(rs.getString(1));
                zenRenouvellement.setPOLICEAV(rs.getString(2));
                zenRenouvellement.setCODEINTE(rs.getInt(3));
                zenRenouvellement.setNUMEPOLI(rs.getLong(4));
                zenRenouvellement.setNUMEAVEN(rs.getInt(5));
                zenRenouvellement.setTYPE_AVENANT(rs.getString(6));
                zenRenouvellement.setDATEEFFE(rs.getDate("DATEEFFE"));
                zenRenouvellement.setDATEECHE(rs.getDate("DATEECHE"));
                zenRenouvellement.setCODECATE(rs.getInt(9));
                zenRenouvellement.setLIBECATE(rs.getString(10));
                zenRenouvellement.setNOM(rs.getString(12));
                zenRenouvellement.setTELEASSU(rs.getString(13));
                zenRenouvellement.setVILLE(rs.getString(14));
                zenRenouvellement.setENERGIE(rs.getString(15));
                zenRenouvellement.setPUISSANCE_FISC(rs.getInt(16));
                zenRenouvellement.setMERQUEVEHI(rs.getString(17));
                zenRenouvellement.setTYPEVEHI(rs.getString(18));
                zenRenouvellement.setPOIDS(rs.getInt(19));
                zenRenouvellement.setNBREPLACE(rs.getInt(20));
                zenRenouvellement.setNBRECARTE(rs.getNString(21));
                zenRenouvellement.setANNEPERCOND(rs.getString(22));
                zenRenouvellement.setDATENAISCOND(rs.getDate(23));
                zenRenouvellement.setDATEMISECIRC(rs.getDate(24));
                zenRenouvellement.setPOSSEDEVIGNETTE(rs.getString(25));
                zenRenouvellement.setDUREE_COURANTE(rs.getInt(26));
                zenRenouvellement.setDUREETOTALE(rs.getInt(27));
                zenRenouvellement.setAVECREMORQ(rs.getString(28));
                zenRenouvellement.setLIQUIDINFLAM(rs.getString(29));
                zenRenouvellement.setUSAGE(rs.getString(30));
                zenRenouvellement.setNUMEIMMA(rs.getString(31));
                zenRenouvellement.setDOUBLECOMMANDE(rs.getString(33));
                zenRenouvellement.setPRIMNETT(rs.getLong(34));
                zenRenouvellement.setPRIMTOTA(rs.getLong(35));
                zenRenouvellement.setGENRE(rs.getString(36));
                zenRenouvellement.setZONE_(rs.getString(37));
                zenRenouvellement.setSOUS_CATEGORIE(rs.getString(38));
                zenRenouvellement.setAVEC_RC_ELEVE(rs.getString(39));
                zenRenouvellement.setCYLINDREE(rs.getString(40));
                zenRenouvellement.setNOM_BUREAU(rs.getString(41));
                listesRenouvellement.add(zenRenouvellement);

            }

        } catch (SQLException e) {

            e.printStackTrace();
            this.deconect();
            // listesRenouvellement = this.listeRenouvellementAllPolice(police);

        }

        return listesRenouvellement;
    }

    @Override
    public void deconect() {
        if (this.con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                con = null;
                Logger.getLogger(ConnectionOracleDataBaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Boolean checkFormatForNumberSinistre(String numberSinistre) {
        char caractereRecherche = '-';

        long compteur = numberSinistre.chars()
                .filter(c -> c == caractereRecherche)
                .count();
        if (compteur == 2l) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<ZenSinistreLn> listeSinistresByNumber(String numberSinistre) {
        ZenSinistreLn sinistreLn = null;

        List<ZenSinistreLn> listesZenSinistreLn = new ArrayList<>();
        if (numberSinistre.contains("-") == false || checkFormatForNumberSinistre(numberSinistre) == Boolean.FALSE) {
            sinistreLn = new ZenSinistreLn();
            GlobalException excp = new GlobalException(zen.sin.bio.orassbio.exception.Error.CONTROL_FORMAT.getCode(), zen.sin.bio.orassbio.exception.Error.NORESULTS_FOUND.getCode());
            excp.setParam(Arrays.asList("agence", "-", "exercice", "-", "numero sinistre").toArray());
            sinistreLn.setError(excp);
            listesZenSinistreLn.add(sinistreLn);
            return listesZenSinistreLn;
        }

        try {

            if (this.con == null || this.connectionOracle().isClosed()) {
                this.con = connectionOracle();

            }

            String[] data = numberSinistre.split("-");
            int agence = 0;
            short exersini = Short.valueOf("0");
            int numesini = 0;

            if (data.length > 0) {
                agence = Integer.parseInt(data[0]);
                exersini = Short.parseShort(data[1]);
                numesini = Integer.parseInt(data[2]);
            }
            Statement stmt = con.createStatement();
            String q1 = "select * from ZEN_SINISTRE_LN  WHERE CODEINTE = '" + agence + "' AND EXERSINI = '" + exersini + "' AND NUMESINI = '" + numesini + "'";
            ResultSet rs = stmt.executeQuery(q1);
            while (rs.next()) {
                sinistreLn = new ZenSinistreLn();
                sinistreLn.setAssure(rs.getNString("ASSURE"));
                sinistreLn.setCodeinte(rs.getInt("CODEINTE"));
                sinistreLn.setNumesini(rs.getInt("NUMESINI"));
                sinistreLn.setNumepoli(rs.getLong("NUMEPOLI"));
                sinistreLn.setIntepoli(rs.getInt("INTEPOLI"));
                sinistreLn.setExersini(rs.getShort("EXERSINI"));
                sinistreLn.setMarque(rs.getString("MARQUE"));
                sinistreLn.setNumeimma(rs.getString("NUMEIMMA"));
                sinistreLn.setSort(rs.getString("SORT"));
                sinistreLn.setDateDeclaration(rs.getDate("DATE_DECLARATION"));
                sinistreLn.setDateSurvenance(rs.getDate("DATE_SURVENANCE"));
                System.out.println(sinistreLn.getAssure() + sinistreLn.getCodeinte());
                listesZenSinistreLn.add(chargeEvenementSinistre(sinistreLn));
            }

        } catch (Exception e) {
            sinistreLn = new ZenSinistreLn();
            GlobalException excp = new GlobalException(e.getLocalizedMessage(), e.getMessage() + " " + e.toString());
            sinistreLn.setError(excp);
            listesZenSinistreLn.add(sinistreLn);
            return listesZenSinistreLn;
        }
        return listesZenSinistreLn;
    }

    @Override
    public List<ZenSinistreLn> listesSinistreByPolice(String police) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ZenSinistreLn> listesAllSinistre() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ZenSinistreLn chargeEvenementSinistre(ZenSinistreLn sinistreLn) {
        try {
            List<ZenEvenement> listeEvenements = new ArrayList<>();
            ZenEvenement zenEvenement = null;
            Statement stmt = con.createStatement();
            String q1 = "select * from ZEN_EVENEMENT  WHERE CODEINTE = '" + sinistreLn.getCodeinte() + "' AND EXERSINI = '" + sinistreLn.getExersini() + "' AND NUMESINI = '" + sinistreLn.getNumesini() + "'";
            ResultSet rs = stmt.executeQuery(q1);
            while (rs.next()) {
                zenEvenement = new ZenEvenement();
                zenEvenement.setLibeeven(rs.getString("LIBEEVEN"));
                zenEvenement.setDateeven(rs.getDate("DATEEVEN"));
                zenEvenement.setObseeven(rs.getString("OBSEEVEN"));
                zenEvenement.setTypevehi(rs.getString("TYPEVEHI"));
                zenEvenement.setMONTREGL(rs.getInt("MONTREGL"));

                listeEvenements.add(zenEvenement);

            }
            sinistreLn.setListeEvenement(listeEvenements);

        } catch (Exception e) {
            GlobalException excp = new GlobalException(e.getLocalizedMessage(), e.getMessage() + " " + e.toString());
            sinistreLn.setError(excp);
        }
        return sinistreLn;
    }
}
