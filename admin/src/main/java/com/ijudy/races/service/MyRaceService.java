package com.ijudy.races.service;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.entity.MyRaceCompKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MyRaceService {

    /**
     * Retrieve a list of all the users registered races. Will take the information from the
     * Principal's object.
     *
     * @param userId Long the User's ID
     * @return List of MyRaces
     */
    List<MyRaceDTO> getMyRaces(final Long userId);

    /**
     * Get all the public and MyRaces in one combined list of
     * MyRaceDTO objects
     *
     * @param userId The User Id for the user. The ID is taken from the
     *               Principal context and not accepted by the Client
     *
     *
     * @return List of MyRaceDTO
     */
    List<MyRaceDTO> getPublicAndMyRacesRaces(final Long userId);

    /**
     * @param beginDate Default to Last Week
     * @param endDate Default for one year
     * @param userId The User Id for the user. The ID is taken from the
     *               Principal context and not accepted by the Client
     * @return List of races meeting the criteria
     */
    List<MyRaceDTO> getPublicAndMyRacesRaces(LocalDate beginDate, LocalDate endDate, final Long userId);

    /**
     * Return one MyRace object given the Primary Key
     *
     * @param pk UserId and RaceId
     * @return MyRaceDTO The single Race Registration information
     *
     */
    Optional<MyRaceDTO> getMyRace(final MyRaceCompKey pk);
}