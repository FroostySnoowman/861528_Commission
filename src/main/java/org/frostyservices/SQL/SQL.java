package org.frostyservices.SQL;

import org.frostyservices.Main;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.*;
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
            String hostname = Main.configurations.getConfigValue("MySQL_Hostname");
            Integer port = Integer.valueOf(Main.configurations.getConfigValue("MySQL_Port"));
            String database = Main.configurations.getConfigValue("MySQL_Database");
            String username = Main.configurations.getConfigValue("MySQL_Username");
            String password = Main.configurations.getConfigValue("MySQL_Password");
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

    public String getLeaderboard() {
        Main.logger.info("Here 1");
        StringBuilder leaderboardMessage = new StringBuilder();

        Main.logger.info("Here 2");
        try {
            try (Connection con = this.connectionPool.getConnection()) {
                Main.logger.info("Here 3");
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM stats ORDER BY Earnings DESC");
                ResultSet rs = pstmt.executeQuery();

                Main.logger.info("Here 4");
                int rank = 1;

                Main.logger.info("Here 5");
                while (rs.next() && rank <= 10) {
                    String discordID = rs.getString("DiscordID");
                    String earnings = rs.getString("Earnings");

                    Main.logger.info("Here 6");
                    leaderboardMessage.append(rank).append(". ").append(discordID).append(" - ").append(earnings).append("\n");

                    Main.logger.info("Here 7");
                    rank++;
                }

                Main.logger.info("Here 8");
                pstmt.close();
                rs.close();
            }
        } catch (SQLException var9) {
            Main.logger.severe("Failed to get the leaderboard");
        }

        Main.logger.info("Here 9");
        Main.logger.info(leaderboardMessage.toString());
        return leaderboardMessage.toString();
    }
}