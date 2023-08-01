
package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllDoctorsTest() throws Exception {
        List<Doctor> doctorList = new ArrayList<>();
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctorList.add(doctor);
        when(doctorRepository.findAll()).thenReturn(doctorList);
        mockMvc.perform(get("/api/doctors").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorList)))
                .andExpect(status().isOk());

    }

    @Test
    void errorGetAllDoctorsTest() throws Exception {
        List<Doctor> doctorList = new ArrayList<>();
        when(doctorRepository.findAll()).thenReturn(doctorList);
        mockMvc.perform(get("/api/doctors").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorList)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDoctorByIdOkTest() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(1);
        Optional<Doctor> opt = Optional.of(doctor);

        when(doctorRepository.findById(Mockito.any())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetDoctorById() throws Exception {
        long doctorId = 1;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/" + doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        when(doctorRepository.save(Mockito.any())).thenReturn(doctor);
        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldDeleteDoctorById() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(1);
        Optional<Doctor> opt = Optional.of(doctor);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);
        when(doctorRepository.findById(Mockito.any())).thenReturn(opt);
        mockMvc.perform(delete("/api/doctors/"+doctor.getId()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldNopDeleteDoctorById() throws Exception {
        long doctorId = 1;
        mockMvc.perform(delete("/api/doctors/"+doctorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllDoctors () throws Exception{
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllPatientsTest() throws Exception {
        List<Patient> patients = new ArrayList<>();
        Patient patient1 = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        patients.add(patient1);
        Patient patient2 = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        patients.add(patient2);
        Patient patient3 = new Patient("Clarisa","Julia", 29, "c.julia@hospital.accwe");
        patients.add(patient3);

        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patients)))
                .andExpect(status().isOk());

    }

    @Test
    void errorGetAllPatientsTest() throws Exception {
        List<Patient> patientList = new ArrayList<>();
        when(patientRepository.findAll()).thenReturn(patientList);
        mockMvc.perform(get("/api/patients").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientList)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPatientByIdOkTest() throws Exception {
        Patient patient = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        patient.setId(1);
        Optional<Patient> opt = Optional.of(patient);

        when(patientRepository.findById(Mockito.any())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetPatientById() throws Exception {
        long doctorId = 1;
        when(patientRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/patients/" + doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePatient() throws Exception {
        Patient patient = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        when(patientRepository.save(Mockito.any())).thenReturn(patient);
        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldDeletePatientById() throws Exception {
        Patient patient = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        patient.setId(1);
        Optional<Patient> opt = Optional.of(patient);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);
        when(patientRepository.findById(Mockito.any())).thenReturn(opt);
        mockMvc.perform(delete("/api/patients/"+patient.getId()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldNopDeletePatientById() throws Exception {
        long doctorId = 1;
        mockMvc.perform(delete("/api/patients/"+doctorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllDoctors () throws Exception{
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllRoomsTest() throws Exception {
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room("Dermatology");
        rooms.add(room1);
        Room room2 = new Room("Operations");
        rooms.add(room2);
        Room room3 = new Room("Emergencies");
        rooms.add(room3);

        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rooms)))
                .andExpect(status().isOk());

    }

    @Test
    void errorGetAllRoomsTest() throws Exception {
        List<Room> roomList = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(roomList);
        mockMvc.perform(get("/api/rooms").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomList)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRoomByIdOkTest() throws Exception {
        Room room = new Room("Dermatology");
        Optional<Room> opt = Optional.of(room);

        when(roomRepository.findByRoomName(Mockito.any())).thenReturn(opt);
        mockMvc.perform(get("/api/rooms/" + room.getRoomName()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetRoomById() throws Exception {
        long doctorId = 1;
        when(roomRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/rooms/" + doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRoom() throws Exception {
        Room room = new Room("Dermatology");
        when(roomRepository.save(Mockito.any())).thenReturn(room);
        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldDeleteRoomById() throws Exception {
        Room room = new Room("Dermatology");
        Optional<Room> opt = Optional.of(room);
        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        when(roomRepository.findByRoomName(Mockito.any())).thenReturn(opt);
        mockMvc.perform(delete("/api/rooms/"+room.getRoomName()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldNopDeleteRoomById() throws Exception {
        String roomsName = "Test";
        mockMvc.perform(delete("/api/rooms/"+roomsName))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllRooms () throws Exception{
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }


}
