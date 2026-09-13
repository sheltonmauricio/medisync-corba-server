package mz.hospital.server;

import Hospital.Patient;
import Hospital.PatientServicePOA;
import mz.hospital.server.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientServiceImpl extends PatientServicePOA {

    @Override
    public Patient registerPatient(
            String fullName,
            String birthDate,
            String gender,
            String phone
    ) {
        String sql = """
                INSERT INTO patients (full_name, birth_date, gender, phone)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, fullName);
            statement.setString(2, birthDate);
            statement.setString(3, gender);
            statement.setString(4, phone);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    int id = keys.getInt(1);

                    return new Patient(
                            id,
                            fullName,
                            birthDate,
                            gender,
                            phone
                    );
                }
            }

            throw new RuntimeException("Não foi possível obter o ID do paciente.");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao registar paciente.", e);
        }
    }

    @Override
    public Patient findPatientById(int id) {

        String sql = """
                SELECT id, full_name, birth_date, gender, phone
                FROM patients
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new Patient(
                            result.getInt("id"),
                            result.getString("full_name"),
                            result.getString("birth_date"),
                            result.getString("gender"),
                            result.getString("phone")
                    );
                }
            }

            return new Patient(0, "", "", "", "");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao procurar paciente.", e);
        }
    }

    @Override
    public Patient[] listPatients() {

        String sql = """
                SELECT id, full_name, birth_date, gender, phone
                FROM patients
                ORDER BY id
                """;

        List<Patient> patients = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                patients.add(new Patient(
                        result.getInt("id"),
                        result.getString("full_name"),
                        result.getString("birth_date"),
                        result.getString("gender"),
                        result.getString("phone")
                ));
            }

            return patients.toArray(new Patient[0]);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar pacientes.", e);
        }
    }
}