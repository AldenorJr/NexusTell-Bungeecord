package br.com.nexus.plugin.storage.database;

import br.com.nexus.plugin.Enum.TellType;
import br.com.nexus.plugin.model.PlayerModel;
import br.com.nexus.plugin.storage.HikariConnect;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RequiredArgsConstructor
public class DatabaseMethod {

    private final HikariConnect hikaridConnect;

    @SneakyThrows
    public void createTable() {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `NexusTell`(`Player` VARCHAR(24), `Notification` VARCHAR(15));");
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public void setProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("INSERT INTO `NexusTell`(`Player`,`Notification`) VALUES (?, ?);");
        preparedStatement.setString(1, proxiedPlayer.getName());
        preparedStatement.setString(2, TellType.habilitado.getTellType());
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public Boolean hasProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusTell` WHERE `Player` = ?;");
        preparedStatement.setString(1, proxiedPlayer.getName());
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    @SneakyThrows
    public PlayerModel getPlayerModelByProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusTell` WHERE `Player` = ?;");
        preparedStatement.setString(1, proxiedPlayer.getName());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return new PlayerModel(proxiedPlayer, TellType.valueOf(rs.getString("Notification")));
    }

    @SneakyThrows
    public void updateNotification(ProxiedPlayer proxiedPlayer, TellType typeTell) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("UPDATE `NexusTell` SET `Notification` = ? WHERE `Player` = ?;");
        preparedStatement.setString(1, typeTell.getTellType());
        preparedStatement.setString(2, proxiedPlayer.getName());
        preparedStatement.executeUpdate();
    }

}
