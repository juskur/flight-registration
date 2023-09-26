package edu.flightregistration.core.jpa.repository;

import edu.flightregistration.core.jpa.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String>,
        JpaSpecificationExecutor<Flight> {

    List<Flight> findByOriginContainingOrDestinationContaining(String origin, String destination);
    List<Flight> findByOriginContainingAndDestinationContaining(String origin, String destination);

}
