package com.alten.altentest.repository;

import com.alten.altentest.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Room, Long> {
}
