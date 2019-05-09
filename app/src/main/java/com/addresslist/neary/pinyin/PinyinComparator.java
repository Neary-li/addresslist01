package com.addresslist.neary.pinyin;


import com.addresslist.neary.bean.User;

import java.util.Comparator;

/**
 * 对联系人排序
 */
public class PinyinComparator implements Comparator<User> {

    public int compare(User o1, User o2) {
        if (o1.getPrefix().equals("@")
                || o2.getPrefix().equals("#")) {
            return -1;
        } else if (o1.getPrefix().equals("#")
                || o2.getPrefix().equals("@")) {
            return 1;
        } else {
            return o1.getPrefix().compareTo(o2.getPrefix());
        }
    }

}
