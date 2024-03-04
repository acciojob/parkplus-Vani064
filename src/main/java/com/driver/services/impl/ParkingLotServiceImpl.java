package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        List<Spot> spotList = new ArrayList<>();
        parkingLot.setSpotList(spotList);

        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot newSpot = new Spot();
        newSpot.setPricePerHour(pricePerHour);
        newSpot.setOccupied(false);
        if(numberOfWheels > 4)
        {
            newSpot.setSpotType(SpotType.OTHERS);
        }else if(numberOfWheels >2)
        {
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        }else {
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        }
        List<Reservation> reservationList = new ArrayList<>();
        newSpot.setReservationList(reservationList);
        newSpot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(newSpot);
        parkingLotRepository1.save(parkingLot);
        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
//         Spot spot = spotRepository1.findById(spotId).get();
//         ParkingLot parkingLot = spot.getParkingLot();
//         parkingLot.getSpotList().remove(spot);
//          parkingLotRepository1.save(parkingLot);
//          spotRepository1.delete(spot);
        //as deleting child we break the link automatically therefore just delete the child
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot savedspot = new Spot();
        for(Spot spot : spotList)
        {
            if(spot.getId()==spotId)
            {
                spot.setPricePerHour(pricePerHour);
                savedspot = spotRepository1.save(spot);
                //as we are doing no change in parent no need to save it
            }
        }

        return savedspot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
       parkingLotRepository1.deleteById(parkingLotId);
    }
}
