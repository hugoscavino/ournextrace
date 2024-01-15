package com.ijudy.races.service.impl;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.entity.AddressEntity;
import com.ijudy.races.enums.RoleNames;
import com.ijudy.races.repository.AddressRepository;
import com.ijudy.races.service.AddressService;
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
        dto.setAuthorId((long) RoleNames.ADMIN.toId());
        AddressEntity entity = RaceConverterUtil.toEntity(dto);
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
        return RaceConverterUtil.toDTO(entity);
    }
}
