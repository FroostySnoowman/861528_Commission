package org.frostyservices.SQL;

import org.frostyservices.Main;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.PooledConnection;


public class SQL {
    public PooledConnection connectionPool;
    public MysqlConnectionPoolDataSource poolSource;
    private final Main main;

    public SQL(Main main) {
        this.main = main;
    }

    public void Connect() {
        try {
            String hostname = Main.configurations.getHostname();
            Integer port = Main.configurations.getPort();
            String database = Main.configurations.getDatabase();
            String username = Main.configurations.getUsername();
            String password = Main.configurations.getPassword();
            this.poolSource = new MysqlConnectionPoolDataSource();
            this.poolSource
                    .setURL(
                            "jdbc:mysql://"
                                    + hostname
                                    + ":"
                                    + port
                                    + "/"
                                    + database
                                    + "?autoReconnect=true"
                    );
            this.poolSource.setUser(username);
            this.poolSource.setPassword(password);
            this.poolSource.setAllowMultiQueries(true);
            this.connectionPool = this.poolSource.getPooledConnection();
            this.CreateSchema();
        } catch (SQLException var2) {
            Main.logger.severe("Failed to connect to DB: " + var2.getMessage());
        }
    }

    public void Disconnect() {
        try {
            this.connectionPool.close();
            Main.logger.info("DB Disconnected");
        } catch (SQLException var2) {
            Main.logger.severe("Failed to disconnect from DB: " + var2.getMessage());
        }
    }

    public void CreateSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS `stats` (\n"
                + "`DiscordID` varchar(255) NOT NULL,\n"
                + "`Project` varchar(255),\n"
                + "`Earnings` varchar(255) NOT NULL,\n"
                + "`Time` varchar(255) NOT NULL\n"
                + ");\n";

        String sql2 = "CREATE TABLE IF NOT EXISTS `projects` (\n"
                + "`ID` INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "`Name` varchar(255) NOT NULL,\n"
                + "`Status` varchar(255) NOT NULL\n"
                + ");\n";

        String sql3 = "CREATE TABLE IF NOT EXISTS `users` (\n"
                + "`DiscordID` varchar(255) NOT NULL,\n"
                + "`Project` varchar(255) NOT NULL,\n"
                + "`TimeIn` varchar(255) NOT NULL,\n"
                + "`TimeOut` varchar(255) NOT NULL,\n"
                + "`Recorded` varchar(255) NOT NULL\n"
                + ");\n";

        try {
            try (Connection con = this.connectionPool.getConnection()) {
                Statement stmt = this.connectionPool.getConnection().createStatement();
                stmt.executeUpdate(sql);
                stmt.executeUpdate(sql2);
                stmt.executeUpdate(sql3);
                stmt.close();
                con.close();
                Main.logger.info("Schema check passed!");
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
            Main.logger.severe("DB Schema creation failed: " + var7.getMessage());
        }
    }
}