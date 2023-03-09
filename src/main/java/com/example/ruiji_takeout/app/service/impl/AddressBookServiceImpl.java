package com.example.ruiji_takeout.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.app.mapper.AddressBookMapper;
import com.example.ruiji_takeout.app.pojo.AddressBook;
import com.example.ruiji_takeout.app.service.AddressBookService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-20:25
 * @Description：
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    @Transactional
    public boolean updateDefaultAddressBook(AddressBook addressBook, Long userId) {
        // 将原本的默认地址设为非默认
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,userId);
        updateWrapper.eq(AddressBook::getIsDefault,1);
        updateWrapper.set(AddressBook::getIsDefault,0);
        int update1 = baseMapper.update(null, updateWrapper);

        // 将新的默认地址设为默认
        if (update1 == 1){
            addressBook.setIsDefault(1);
            int update2 = baseMapper.updateById(addressBook);
            if (update2 == 1) return true;
        }

        return false;
    }
}
