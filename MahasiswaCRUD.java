/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.mahasiswacrud;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class MahasiswaCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/datamahasiswa";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createTable(connection); // Buat tabel jika belum ada

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Tambah Mahasiswa");
                System.out.println("2. Tampilkan Mahasiswa");
                System.out.println("3. Update Mahasiswa");
                System.out.println("4. Hapus Mahasiswa");
                System.out.println("0. Keluar");

                System.out.print("Pilih menu (0-4): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Membuang karakter newline dari buffer

                switch (choice) {
                    case 1 -> tambahMahasiswa(connection, scanner);
                    case 2 -> tampilkanMahasiswa(connection);
                    case 3 -> updateMahasiswa(connection, scanner);
                    case 4 -> hapusMahasiswa(connection, scanner);
                    case 0 -> {
                        System.out.println("Program selesai.");
                        System.exit(0);
                    }
                    default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            }
        } catch (SQLException e) {
        }
    }

    // Fungsi untuk membuat tabel mahasiswa jika belum ada
    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS mahasiswa (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nama VARCHAR(255)," +
                "nim VARCHAR(20)," +
                "alamat VARCHAR(255)," +
                "telepon VARCHAR(15)," +
                "jenis_kelamin VARCHAR(10)" +
                ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    // Fungsi untuk menambahkan data mahasiswa
    private static void tambahMahasiswa(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Nama: ");
        String nama = scanner.nextLine();

        System.out.print("NIM: ");
        String nim = scanner.nextLine();

        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();

        System.out.print("Nomor Telepon: ");
        String telepon = scanner.nextLine();

        System.out.print("Jenis Kelamin: ");
        String jenisKelamin = scanner.nextLine();

        String insertDataSQL = "INSERT INTO mahasiswa (nama, nim, alamat, telepon, jenis_kelamin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, nim);
            preparedStatement.setString(3, alamat);
            preparedStatement.setString(4, telepon);
            preparedStatement.setString(5, jenisKelamin);

            preparedStatement.executeUpdate();
            System.out.println("Mahasiswa berhasil ditambahkan.");
        }
    }

    // Fungsi untuk menampilkan data mahasiswa
    private static void tampilkanMahasiswa(Connection connection) throws SQLException {
        String selectDataSQL = "SELECT * FROM mahasiswa";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectDataSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\nDaftar Mahasiswa:");
            System.out.printf("%-5s | %-20s | %-15s | %-30s | %-15s | %-15s%n", "ID", "Nama", "NIM", "Alamat", "Telepon", "Jenis Kelamin");
            System.out.println("--------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String nim = resultSet.getString("nim");
                String alamat = resultSet.getString("alamat");
                String telepon = resultSet.getString("telepon");
                String jenisKelamin = resultSet.getString("jenis_kelamin");

                System.out.printf("%-5s | %-20s | %-15s | %-30s | %-15s | %-15s%n", id, nama, nim, alamat, telepon, jenisKelamin);
            }
        }
    }

    // Fungsi untuk mengupdate data mahasiswa
    private static void updateMahasiswa(Connection connection, Scanner scanner) throws SQLException {
        tampilkanMahasiswa(connection);

        System.out.print("\nMasukkan ID Mahasiswa yang akan diupdate: ");
        int idToUpdate = scanner.nextInt();
        scanner.nextLine(); // Membuang karakter newline dari buffer

        System.out.print("Nama Baru: ");
        String newNama = scanner.nextLine();

        System.out.print("NIM Baru: ");
        String newNim = scanner.nextLine();

        System.out.print("Alamat Baru: ");
        String newAlamat = scanner.nextLine();

        System.out.print("Nomor Telepon Baru: ");
        String newTelepon = scanner.nextLine();

        System.out.print("Jenis Kelamin Baru: ");
        String newJenisKelamin = scanner.nextLine();

        String updateDataSQL = "UPDATE mahasiswa SET nama=?, nim=?, alamat=?, telepon=?, jenis_kelamin=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateDataSQL)) {
            preparedStatement.setString(1, newNama);
            preparedStatement.setString(2, newNim);
            preparedStatement.setString(3, newAlamat);
            preparedStatement.setString(4, newTelepon);
            preparedStatement.setString(5, newJenisKelamin);
            preparedStatement.setInt(6, idToUpdate);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data mahasiswa berhasil diupdate.");
            } else {
                System.out.println("ID mahasiswa tidak ditemukan.");
            }
        }
    }

    // Fungsi untuk menghapus data mahasiswa
    private static void hapusMahasiswa(Connection connection, Scanner scanner) throws SQLException {
        tampilkanMahasiswa(connection);

        System.out.print("\nMasukkan ID Mahasiswa yang akan dihapus: ");
        int idToDelete = scanner.nextInt();

        String deleteDataSQL = "DELETE FROM mahasiswa WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteDataSQL)) {
            preparedStatement.setInt(1, idToDelete);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data mahasiswa berhasil dihapus.");
            } else {
                System.out.println("ID mahasiswa tidak ditemukan.");
            }
        }
    }
}