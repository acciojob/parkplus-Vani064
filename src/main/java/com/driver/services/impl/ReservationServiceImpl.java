package com.driver.services.impl;

//import com.driver.model.*;
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

        Reservation reservation= new Reservation();
        //when we take value like this
        //User user = userRepository3.findById(userId).get();
        //user will not be null because get() is annotated with NOTNULL parameter

        if(userRepository3.findById(userId) == null){
            throw new Exception("Cannot make reservation");
        }
        User user= userRepository3.findById(userId).get();

        if(parkingLotRepository3.findById(parkingLotId)==null){
            throw new Exception("Cannot make reservation");
        }

        ParkingLot parkingLot= parkingLotRepository3.findById(parkingLotId).get();

        SpotType spotType;

        List<Spot> spotList = parkingLot.getSpotList();

        int minprice = Integer.MAX_VALUE;
        Spot reservedSpot = null;

        for(Spot spot : spotList){
            if(spot.getOccupied() == false){
                if(spot.getSpotType().equals(SpotType.TWO_WHEELER)){
                    if(numberOfWheels <= 2){
                        if(minprice > spot.getPricePerHour()){
                            minprice = spot.getPricePerHour();
                            reservedSpot = spot;
                        }
                    }
                }
                else if(spot.getSpotType().equals(SpotType.FOUR_WHEELER)){
                    if(numberOfWheels <= 4){
                        if(minprice > spot.getPricePerHour()){
                            minprice = spot.getPricePerHour();
                            reservedSpot = spot;
                        }
                    }
                }

                else{
                    if(minprice > spot.getPricePerHour()){
                        minprice = spot.getPricePerHour();
                        reservedSpot = spot;
                    }
                }
            }
        }

        if(reservedSpot == null) {
            throw new Exception("Cannot make reservation");
        }


        reservedSpot.setOccupied(true);
        reservation.setUser(user);
        reservation.setSpot(reservedSpot);
        reservation.setNumberOfHours(timeInHours);

        List<Reservation> reservationList  = user.getReservationList();

        List<Reservation>reservations = user.getReservationList();

        reservationList.add(reservation);
        reservations.add(reservation);

        user.setReservationList(reservationList);
        reservedSpot.setReservationList(reservationList);


        userRepository3.save(user);

        spotRepository3.save(reservedSpot);

//        parkingLotRepository3.save(parkingLot);
//        Payment newPayment = new Payment();


        return reservation;

    }
}