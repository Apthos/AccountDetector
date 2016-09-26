package net.apthos.phobos.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.apthos.phobos.files.FileUtils;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalTime;

public class MySQL {

    private HikariDataSource DataSource;
    boolean ESTABLISHED = false;

    public MySQL() {
        DatabseCheck();
        if (!ESTABLISHED) {
            // Todo: Console Report //
            return;
        }
        setupDataSource();
    }

    private void DatabseCheck() {
        String l[] = FileUtils.getLoginData();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://" + l[0] + ":" + l[1] +
                            "/mysql?zeroDateTimeBehavior=convertToNull", l[3], l[4]);
            ResultSet resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equalsIgnoreCase(l[2])) {
                    ESTABLISHED = true;
                    return;
                }
            }
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            ESTABLISHED = false;
        }
        createDatabase(l[2], l);
        ESTABLISHED = true;
    }

    private void createDatabase(String dn, String l[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://"
                            + l[0] + ":" + l[1] +
                            "/mysql?zeroDateTimeBehavior=convertToNull", l[3], l[4]);
            PreparedStatement CreateDB = connection.prepareStatement(
                    "CREATE DATABASE " + dn + ";");
            CreateDB.executeUpdate();
            CreateDB.close();
            connection.close();
            connection = DriverManager.getConnection("jdbc:mysql://" + l[0] + ":" +
                    l[1] + "/" + l[2], l[3], l[4]);
            PreparedStatement CT = connection.prepareStatement
                    (
                            "CREATE TABLE `" + dn + "`.`Registry` (" +
                                    " `UUID` VARCHAR(64) NOT NULL ," +
                                    " `IP` VARCHAR(64) NOT NULL ," +
                                    " `date` DATETIME NOT NULL );"
                    );
            CT.executeUpdate();
            connection.close();
            CT.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDataSource() {
        String l[] = FileUtils.getLoginData();
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", l[0]);
        config.addDataSourceProperty("port", l[1]);
        config.addDataSourceProperty("databaseName", l[2]);
        config.addDataSourceProperty("user", l[3]);
        config.addDataSourceProperty("password", l[4]);
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "1024");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        DataSource = new HikariDataSource(config);
    }

    public void createPlayer(Player player) {
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO AccountDetector.Registry (UUID, IP, date) VALUES " +
                            "(? , ? , ?)"
            );
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getAddress().toString());
            statement.setTime(3, Time.valueOf(LocalTime.now()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
