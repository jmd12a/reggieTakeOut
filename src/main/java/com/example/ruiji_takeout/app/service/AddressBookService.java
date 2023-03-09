package com.example.ruiji_takeout.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruiji_takeout.app.pojo.AddressBook;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-20:24
 * @Description：
 */
public interface AddressBookService extends IService<AddressBook> {
    boolean updateDefaultAddressBook(AddressBook addressBook, Long id);
}
