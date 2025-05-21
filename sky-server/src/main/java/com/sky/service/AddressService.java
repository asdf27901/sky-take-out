package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressService {
    boolean saveAddress(AddressBook addressBook);

    List<AddressBook> getAllAddress();

    AddressBook getDefaultAddress();

    boolean setDefaultAddress(Long id);

    AddressBook getAddressById(Long id);

    boolean deleteAddressById(Long id);

    boolean updateAddress(AddressBook addressBook);
}
