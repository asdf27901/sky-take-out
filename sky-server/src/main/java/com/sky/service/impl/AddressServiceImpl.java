package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.BusinessException;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public boolean saveAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        return addressMapper.saveAddress(addressBook) > 0;
    }

    @Override
    public List<AddressBook> getAllAddress() {
        return addressMapper.getAllAddress(BaseContext.getCurrentId());
    }

    @Override
    public AddressBook getDefaultAddress() {
        return addressMapper.getDefaultAddress(BaseContext.getCurrentId());
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long id) {
        AddressBook addressBook = getAddressById(id);
        AddressBook defaultAddress = getDefaultAddress();
        if (defaultAddress != null && !defaultAddress.getId().equals(id)) {
            // 如果本来就存在默认地址，那么就要修改原来默认地址的默认状态
            defaultAddress.setIsDefault(0);
            updateAddress(defaultAddress);
        }
        addressBook.setIsDefault(1);
        return updateAddress(addressBook);
    }

    @Override
    public AddressBook getAddressById(Long id) {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = addressMapper.getAddressById(userId, id);
        if (addressBook == null) {
            throw new BusinessException("地址不存在");
        }
        return addressBook;
    }

    @Override
    public boolean deleteAddressById(Long id) {
        Long userId = BaseContext.getCurrentId();
        int affectRow = addressMapper.deleteAddressById(userId, id);
        return affectRow > 0;
    }

    @Override
    public boolean updateAddress(AddressBook addressBook) {
        AddressBook address = getAddressById(addressBook.getId());
        BeanUtils.copyProperties(addressBook, address);
        address.setUserId(BaseContext.getCurrentId());
        int affectRow = addressMapper.updateAddress(address);
        return affectRow > 0;
    }


}
