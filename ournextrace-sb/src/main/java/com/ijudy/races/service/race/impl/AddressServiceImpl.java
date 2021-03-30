package com.ijudy.races.service.race.impl;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.entity.AddressEntity;
import com.ijudy.races.exception.NotFoundException;
import com.ijudy.races.repository.AddressRepository;
import com.ijudy.races.service.race.AddressService;
import com.ijudy.races.util.RaceConverterUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO save(AddressDTO dto) {
        final LocalDateTime now = LocalDateTime.now();

        AddressEntity entity = RaceConverterUtil.toEntity(dto);
        // H2 does not default to CURRENT TIME DATE
        entity.setModDate(now);

        AddressEntity savedEntity = addressRepository.save(entity);
        dto.setId(savedEntity.getId());
        return dto;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<AddressDTO> addresses = new ArrayList<>();
        addressRepository.findByOrderByLocationAsc().forEach(
                    entity -> addresses.add(RaceConverterUtil.toDTO(entity))
                );
        return addresses;

    }

    @Override
    public AddressDTO getAddress(long addressId) {

        Optional<AddressEntity> addressEntityOptional = addressRepository.findById(addressId);

        AddressEntity entity = addressEntityOptional.get();
        if (entity == null){
            throw new NotFoundException("Address with id " + addressId + " was not found");
        }

        return RaceConverterUtil.toDTO(entity);
    }
}
