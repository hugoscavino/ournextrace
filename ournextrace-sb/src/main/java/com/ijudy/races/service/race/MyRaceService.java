package com.ijudy.races.service.race;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.entity.MyRaceCompKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MyRaceService {

    /**
     * Save and Update MyRace
     *
     * @param dto MyRaceDTO has registration information for the User's race
     * @return The saved or updated MyRace
     */
    MyRaceDTO saveMyRace(MyRaceDTO dto);

    /**
     * Delete the MyRace from the list of registered races
     * @param dto
     */
    void deleteMyRace(MyRaceDTO dto);

    /**
     * Retrieve a list of all the users registered races. Will take the information from the
     * Principal's object.
     *
     * @see java.security.Principal
     *
     * @Param userId Long the User's ID
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
