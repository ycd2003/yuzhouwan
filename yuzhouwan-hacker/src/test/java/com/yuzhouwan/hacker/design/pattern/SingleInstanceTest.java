package com.yuzhouwan.hacker.design.pattern;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.hacker.algorithms.thread.lock.ReadWriteLockExample;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: SingleInstance Tester
 *
 * @author Benedict Jin
 * @since 2016/8/3
 */
public class SingleInstanceTest {

    private static final String SERIALIZE_STORE = DirUtils.TEST_RESOURCES_PATH.concat("serialize/serialize.out");

    @Test
    public void serialize() throws Exception {

        SingleInstance instance = SingleInstance.INSTANCE;
        ReadWriteLockExample.Business business = ReadWriteLockExample.Business.getInstance();

        try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(SERIALIZE_STORE));
             ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream(SERIALIZE_STORE))) {
            oos1.writeObject(instance);
            oos1.writeObject(business);

            SingleInstance instanceSerialized = ((SingleInstance) ois1.readObject());
            ReadWriteLockExample.Business businessSerialized = ((ReadWriteLockExample.Business) ois1.readObject());

            //Enum版的 single instance 保证了安全性
            assertTrue(3 == instanceSerialized.getPoint());
            assertTrue(0 == instance.compareTo(instanceSerialized));
            assertTrue(instance.equals(instanceSerialized));

            //序列化、反序列化 破坏了 "single instance + double check"的唯一性
            assertFalse(business.equals(businessSerialized));
        }
    }
}
