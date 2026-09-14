package mz.hospital.server;

import Hospital.Appointment;
import Hospital.AppointmentServicePOA;
import mz.hospital.server.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceImpl extends AppointmentServicePOA {

    @Override
    public Appointment scheduleAppointment(
            int patientId,
            String doctor,
            String appointmentDate,
            String specialty
    ) {
        String sql = """
                INSERT INTO appointments
                (patient_id, doctor, appointment_date, specialty)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, patientId);
            statement.setString(2, doctor);
            statement.setString(3, appointmentDate);
            statement.setString(4, specialty);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Appointment(
                            keys.getInt(1),
                            patientId,
                            doctor,
                            appointmentDate,
                            specialty
                    );
                }
            }

            throw new RuntimeException(
                    "Não foi possível obter o ID da consulta."
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao marcar consulta.",
                    e
            );
        }
    }

    @Override
    public Appointment findAppointmentById(int id) {

        String sql = """
                SELECT id, patient_id, doctor, appointment_date, specialty
                FROM appointments
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new Appointment(
                            result.getInt("id"),
                            result.getInt("patient_id"),
                            result.getString("doctor"),
                            result.getString("appointment_date"),
                            result.getString("specialty")
                    );
                }
            }

            return new Appointment(0, 0, "", "", "");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao procurar consulta.",
                    e
            );
        }
    }

    @Override
    public Appointment[] listAppointments() {

        String sql = """
                SELECT id, patient_id, doctor, appointment_date, specialty
                FROM appointments
                ORDER BY id
                """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                appointments.add(new Appointment(
                        result.getInt("id"),
                        result.getInt("patient_id"),
                        result.getString("doctor"),
                        result.getString("appointment_date"),
                        result.getString("specialty")
                ));
            }

            return appointments.toArray(new Appointment[0]);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao listar consultas.",
                    e
            );
        }
    }
}