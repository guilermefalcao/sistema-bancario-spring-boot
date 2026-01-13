package com.exemplo.conta.backend_conta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleTestRunner {

    public static void main(String[] args) {

        // Configuração da conexão Oracle
        String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
        String user = "CONTA_APP";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE")) {

            System.out.println("Conexão Oracle OK!");
            System.out.println("---------------------------");

            while (rs.next()) {
                int id = rs.getInt("ID_CLIENTE");
                String nome = rs.getString("NOME");
                String cpf = rs.getString("CPF");
                java.sql.Timestamp dataCadastro = rs.getTimestamp("DATA_CADASTRO");

                System.out.println(
                        "ID_CLIENTE: " + id +
                        ", NOME: " + nome +
                        ", CPF: " + cpf +
                        ", DATA_CADASTRO: " + dataCadastro
                );
            }

        } catch (Exception e) {
            System.err.println("Erro ao conectar no Oracle:");
            e.printStackTrace();
        }
    }
}
