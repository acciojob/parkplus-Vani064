package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Reservation reservation = new Reservation();
        Optional<ParkingLot> optionalParkingLot=parkingLotRepository3.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = optionalParkingLot.get();
        Optional<User> optionalUser = userRepository3.findById(userId);
        if(!optionalUser.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        User user = optionalUser.get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot finalspot=null;
        int min=Integer.MAX_VALUE;
        for(Spot spot:spotList)
        {
            if(spot.getOccupied())
            {
                continue;
            }
            if(numberOfWheels>4 && spot.getSpotType() == SpotType.OTHERS)
            {

                if(min > spot.getPricePerHour()*timeInHours)
               {
                   min = spot.getPricePerHour()*timeInHours;
                   finalspot = spot;
               }
            }
            else if(numberOfWheels>2 && spot.getSpotType()==SpotType.FOUR_WHEELER)
            {

                if(min > spot.getPricePerHour()*timeInHours)
                {
                    min = spot.getPricePerHour()*timeInHours;
                    finalspot = spot;
                }
            }
            else{

                if(min > spot.getPricePerHour()*timeInHours)
                {
                    min = spot.getPricePerHour()*timeInHours;
                    finalspot = spot;
                }
            }

        }
        if(finalspot == null)
        {
            throw new Exception("Cannot make reservation");
        }
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(finalspot);
        reservation.setUser(user);

        user.getReservationList().add(reservation);;
        finalspot.getReservationList().add(reservation);;
        finalspot.setOccupied(true);

        userRepository3.save(user);
        spotRepository3.save(finalspot);

        return reservation;
    }
}
