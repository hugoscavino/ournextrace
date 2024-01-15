package com.ijudy.races.service;

import com.ijudy.races.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    /**
     * Save on Address DTO to repository
     * @param dto The Address DTO to save
     * @return AddressDTO saved object with the new Primary Key
     */
    AddressDTO save(AddressDTO dto);

    /**
     * Return all the addresses in the repository
     *
     * @return List<AddressDTO> list of all addresses
     */
    List<AddressDTO> getAllAddresses();

    /**
     * Return one address given the primary key
     *
     * @param addressId
     * @return AddressDTO returned if not found will throw a RunTimeException
     */
    AddressDTO getAddress(long addressId);
}
