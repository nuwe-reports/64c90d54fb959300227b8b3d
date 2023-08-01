package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeAll
    void setUp() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        r1 = new Room("Dermatology");
        a1 = new Appointment(p1, d1, r1,
                LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
        a2 = new Appointment(p1, d1, r1,
                LocalDateTime.parse("19:30 25/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:30 25/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
        a3 = new Appointment(p1, d1, r1,
                LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }

    @Test
    void testSaveDoctor() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        Doctor savedDoctor = entityManager.persistAndFlush(d1);
        entityManager.clear();

        Doctor foundDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
        assertThat(foundDoctor.getFirstName()).isEqualTo(d1.getFirstName());
    }


    @Test
    void testFindDoctorById() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor savedDoctor = entityManager.persistAndFlush(d1);
        entityManager.clear();

        Optional<Doctor> foundDoctorOptional = Optional.ofNullable(entityManager.find(Doctor.class, savedDoctor.getId()));
        assertThat(foundDoctorOptional).isPresent();

        Doctor foundDoctor = foundDoctorOptional.get();
        assertThat(foundDoctor.getId()).isEqualTo(savedDoctor.getId());
        assertThat(foundDoctor.getFirstName()).isEqualTo("Perla");
        assertThat(foundDoctor.getLastName()).isEqualTo("Amalia");
        assertThat(foundDoctor.getAge()).isEqualTo(24);
        assertThat(foundDoctor.getEmail()).isEqualTo("p.amalia@hospital.accwe");
    }

    @Test
    void testUpdateDoctor() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor savedDoctor = entityManager.persistAndFlush(d1);
        entityManager.clear();

        Doctor foundDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
        foundDoctor.setFirstName("PruebaTest");
        entityManager.flush();
        entityManager.clear();

        Doctor updatedDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
        assertThat(updatedDoctor.getFirstName()).isEqualTo("PruebaTest");
    }

    @Test
    void testDeleteDoctor() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor savedDoctor = entityManager.persistAndFlush(d1);

        entityManager.remove(savedDoctor);
        entityManager.flush();

        Doctor deletedDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
        assertThat(deletedDoctor).isNull();
    }

    @Test
    void testSavePatient() {
        p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");

        Patient savedPatient = entityManager.persistAndFlush(p1);
        entityManager.clear();

        Patient foundPatient = entityManager.find(Patient.class, savedPatient.getId());
        assertThat(foundPatient.getFirstName()).isEqualTo(savedPatient.getFirstName());
    }

    @Test
    void testFindPatientById() {
        p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient savedPatient = entityManager.persistAndFlush(p1);
        entityManager.clear();

        Optional<Patient> foundPatientOptional = Optional.ofNullable(entityManager.find(Patient.class, savedPatient.getId()));
        assertThat(foundPatientOptional).isPresent();

        Patient foundPatient = foundPatientOptional.get();
        assertThat(foundPatient.getId()).isEqualTo(savedPatient.getId());
        assertThat(foundPatient.getFirstName()).isEqualTo("Juan");
        assertThat(foundPatient.getLastName()).isEqualTo("Carlos");
        assertThat(foundPatient.getAge()).isEqualTo(34);
        assertThat(foundPatient.getEmail()).isEqualTo("j.carlos@hospital.accwe");
    }

    @Test
    void testUpdatePatient() {
        p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient savedPatient = entityManager.persistAndFlush(p1);
        entityManager.clear();

        Patient foundPatient = entityManager.find(Patient.class, savedPatient.getId());
        foundPatient.setFirstName("Juan");
        entityManager.flush();
        entityManager.clear();

        Patient updatedPatient = entityManager.find(Patient.class, savedPatient.getId());
        assertThat(updatedPatient.getFirstName()).isEqualTo("Juan");
    }

    @Test
    void testDeletePatient() {
        p1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient savedPatient = entityManager.persistAndFlush(p1);

        entityManager.remove(savedPatient);
        entityManager.flush();

        Patient deletedPatient = entityManager.find(Patient.class, savedPatient.getId());
        assertThat(deletedPatient).isNull();
    }

    @Test
    void testSaveRoom() {
        r1 = new Room("Dermatology");

        Room savedRoom = entityManager.persistAndFlush(r1);
        entityManager.clear();

        Room foundRoom = entityManager.find(Room.class, savedRoom.getRoomName());
        assertThat(foundRoom.getRoomName()).isEqualTo(savedRoom.getRoomName());
    }

    @Test
    void testFindRoomByName() {
        r1 = new Room("Dermatology");
        Room savedRoom = entityManager.persistAndFlush(r1);
        entityManager.clear();

        Optional<Room> foundRoomOptional = Optional.ofNullable(entityManager.find(Room.class, savedRoom.getRoomName()));
        assertThat(foundRoomOptional).isPresent();

        Room foundRoom = foundRoomOptional.get();
        assertThat(foundRoom.getRoomName()).isEqualTo(savedRoom.getRoomName());
        assertThat(foundRoom.getRoomName()).isEqualTo("Dermatology");
    }

    @Test
    void testUpdateRoom() {
        r1 = new Room("Dermatology");
        Room savedRoom = entityManager.persistAndFlush(r1);
        entityManager.clear();

        Room foundRoom = entityManager.find(Room.class, savedRoom.getRoomName());
        entityManager.clear();

        Room updatedRoom = entityManager.find(Room.class, savedRoom.getRoomName());
        assertThat(updatedRoom.getRoomName()).isEqualTo("Dermatology");
    }

    @Test
    void testDeleteRoom() {
        r1 = new Room("Dermatology");
        Room savedRoom = entityManager.persistAndFlush(r1);

        entityManager.remove(savedRoom);

        Room deletedRoom = entityManager.find(Room.class, savedRoom.getRoomName());
        assertThat(deletedRoom).isNull();
    }

    @Test
    void testSaveAppointment() {
        Appointment savedAppointment = entityManager.persistAndFlush(a1);

        Appointment foundAppointment = entityManager.find(Appointment.class, savedAppointment.getId());
        assertThat(foundAppointment).isEqualTo(savedAppointment);
    }

    @Test
    void testFindAppointmentById() {
        // Creamos las entidades relacionadas
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Patient patient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Room room = new Room("Dermatology");

        // Persistimos las entidades relacionadas para asegurarnos de que estén en estado "managed"
        entityManager.persistAndFlush(doctor);
        entityManager.persistAndFlush(patient);
        entityManager.persistAndFlush(room);

        // Creamos la entidad Appointment con las entidades relacionadas ya persistidas
        Appointment appointment = new Appointment(patient, doctor, room,
                LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

        // Persistimos la entidad Appointment
        entityManager.persistAndFlush(appointment);
        long appointmentId = appointment.getId();

        // Limpiamos el contexto de persistencia para asegurarnos de que todas las entidades estén desvinculadas
        entityManager.clear();

        // Buscamos la entidad Appointment por su ID
        Optional<Appointment> foundAppointmentOptional = Optional.ofNullable(entityManager.find(Appointment.class, appointmentId));
        assertThat(foundAppointmentOptional).isPresent();

        // Obtenemos la entidad encontrada
        Appointment foundAppointment = foundAppointmentOptional.get();

        // Verificamos que los atributos de la entidad encontrada coincidan con los de la entidad original
        assertThat(foundAppointment.getId()).isEqualTo(appointment.getId());
        assertThat(foundAppointment.getStartsAt()).isEqualTo(appointment.getStartsAt());
        assertThat(foundAppointment.getFinishesAt()).isEqualTo(appointment.getFinishesAt());
        assertThat(foundAppointment.getPatient().getFirstName()).isEqualTo(appointment.getPatient().getFirstName());
        assertThat(foundAppointment.getDoctor().getFirstName()).isEqualTo(appointment.getDoctor().getFirstName());
        assertThat(foundAppointment.getRoom().getRoomName()).isEqualTo(appointment.getRoom().getRoomName());
    }

    @Test
    void testOverlaps() {
        Appointment overlappingAppointment = new Appointment(p1, d1, r1,
                LocalDateTime.parse("19:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

        boolean isOverlapping = a1.overlaps(overlappingAppointment);
        assertThat(isOverlapping).isTrue();
    }

    @Test
    void testNotOverlapping() {
        // Create an appointment that does not overlap with the original appointment
        Appointment nonOverlappingAppointment = new Appointment(p1, d1, r1,
                LocalDateTime.parse("17:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("18:00 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

        boolean isOverlapping = a1.overlaps(nonOverlappingAppointment);
        assertThat(isOverlapping).isFalse();
    }

    @Test
    void testDeleteAppointment() {
        // Creamos las entidades relacionadas
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Patient patient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Room room = new Room("Dermatology");

        // Persistimos las entidades relacionadas para asegurarnos de que estén en estado "managed"
        entityManager.persistAndFlush(doctor);
        entityManager.persistAndFlush(patient);
        entityManager.persistAndFlush(room);

        // Creamos la entidad Appointment con las entidades relacionadas ya persistidas
        Appointment appointment = new Appointment(patient, doctor, room,
                LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

        // Persistimos la entidad Appointment
        entityManager.persistAndFlush(appointment);
        long appointmentId = appointment.getId();

        // Removemos la entidad usando su ID
        entityManager.remove(entityManager.find(Appointment.class, appointmentId));
        entityManager.flush();

        // Verificamos que la entidad fue eliminada
        Appointment deletedAppointment = entityManager.find(Appointment.class, appointmentId);
        assertThat(deletedAppointment).isNull();
    }
}
