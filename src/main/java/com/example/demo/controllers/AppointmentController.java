package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        if (!isValidAppointment(appointment)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!isDoctorScheduleAvailable(appointment, existingAppointments)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        if (isAppointmentConflict(appointment, existingAppointments) && !isRoomScheduleAvailable(appointment, existingAppointments)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        appointmentRepository.save(appointment);
        List<Appointment> updatedAppointments = appointmentRepository.findAll();
        return ResponseEntity.ok(updatedAppointments);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isValidAppointment(Appointment appointment) {
        return appointment.getStartsAt().isBefore(appointment.getFinishesAt());
    }

    private boolean isAppointmentConflict(Appointment appointment, List<Appointment> existingAppointments) {
        for (Appointment existingAppointment : existingAppointments) {
            if (!existingAppointment.equals(appointment) &&
                    existingAppointment.getRoom().equals(appointment.getRoom()) &&
                    existingAppointment.getStartsAt().isBefore(appointment.getFinishesAt()) &&
                    existingAppointment.getFinishesAt().isAfter(appointment.getStartsAt())) {
                return true;
            }
            if (existingAppointment.getRoom().getRoomName().equals(appointment.getRoom().getRoomName()) &&
                    existingAppointment.getStartsAt().isEqual(appointment.getStartsAt()) &&
                    existingAppointment.getFinishesAt().isEqual(appointment.getFinishesAt())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDoctorScheduleAvailable(Appointment proposedAppointment, List<Appointment> existingAppointments) {
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getDoctor().equals(proposedAppointment.getDoctor()) &&
                    existingAppointment.getStartsAt().isBefore(proposedAppointment.getFinishesAt()) &&
                    existingAppointment.getFinishesAt().isAfter(proposedAppointment.getStartsAt())) {
                return false;
            }
            if (existingAppointment.getDoctor().equals(proposedAppointment.getDoctor()) &&
                    existingAppointment.getStartsAt().isEqual(proposedAppointment.getStartsAt()) &&
                    existingAppointment.getFinishesAt().isEqual(proposedAppointment.getFinishesAt())) {
                return false;
            }
        }
        return true;
    }

    private boolean isRoomScheduleAvailable(Appointment proposedAppointment, List<Appointment> existingAppointments) {
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getRoom().equals(proposedAppointment.getRoom()) &&
                    existingAppointment.getStartsAt().isBefore(proposedAppointment.getFinishesAt()) &&
                    existingAppointment.getFinishesAt().isAfter(proposedAppointment.getStartsAt())) {
                return false;
            }
            if (existingAppointment.getRoom().getRoomName().equals(proposedAppointment.getRoom().getRoomName()) &&
                    existingAppointment.getStartsAt().isEqual(proposedAppointment.getStartsAt()) &&
                    existingAppointment.getFinishesAt().isEqual(proposedAppointment.getFinishesAt())) {
                return false;
            }
        }
        return true;
    }

}
