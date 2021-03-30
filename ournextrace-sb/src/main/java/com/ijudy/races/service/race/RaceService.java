package com.ijudy.races.service.race;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Operations regarding all Races
 */
public interface RaceService {

    /**
     * All public MyRaceDTO. The UserDTO is a public instance
     *
     * @return List of MyRaceDTO
     */
    List<MyRaceDTO> getPublicRaces();

    /**
     * Get Races in this range
     * @param startDate
     * @param endDate
     * @return
     */
    List<MyRaceDTO> getPublicRaces(LocalDate startDate, LocalDate endDate);
    
    /**
     * Return all the Races ordered by Date. There are no
     * filters just all the races
     *
     * @return List<RaceDTO> List of Races
     */
    List<RaceDTO> getAllRaces();

    /**
     * Search for a Race given the Race Id
     * @param raceId The Race Id
     * @return RaceDTO The found Race
     */
    Optional<RaceDTO> findById(Long raceId);

    /**
     * Save ( or update) one RaceDTO to the repository
     * @param raceDTO The Race to save or update
     * @return RaceDTO The saved/updated Race
     */
    RaceDTO save(RaceDTO raceDTO);

    /**
     * Clone a race for the next year
     * @param raceId
     * @return newly cloned and saved Race
     */
    RaceDTO clone(long raceId );

    /**
     * List of all the Race Types
     * @return List<RaceTypeDTO>
     */
    List<RaceTypeDTO> getAllRaceTypes();

    /**
     * Update the location of the Race
     * @param raceId The Race Id
     * @param addressId The Address Id
     * @return RaceDTO The updated RaceDTO
     */
    RaceDTO updateRaceLocation(Long raceId, Long addressId);


    RaceDTO delete(Long raceId);
}
