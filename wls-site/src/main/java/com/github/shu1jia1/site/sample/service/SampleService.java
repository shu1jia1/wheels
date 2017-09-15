package com.github.shu1jia1.site.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.sample.dao.SampleDao;
import com.github.shu1jia1.site.sample.entity.SampleUser;


@Service("sampleService")
public class SampleService implements ISampleService {
    @Autowired
    private SampleDao sampleDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void tranRequire() {
        sampleDao.insertTestData("a_stu", "insert", 1);
        //regionDao.insertTestData("a_tx", "tx1", 1);
        throw new IllegalArgumentException("insertData");
    }

    @Override
    public void tranDefault() {
        sampleDao.insertTestData("a_stu", "testdata", 1);
        throw new IllegalArgumentException("testData");
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void tranNerver() {
        sampleDao.insertTestData("a_stu", "nerver", 1);
        //regionDao.insertTestData("a_tx", "tx1", 1);
        throw new IllegalArgumentException("addData");

    }

    @Override
    @Transactional(readOnly = true)
    public void tranReadOnly() {
        sampleDao.insertTestData("a_stu", "delete", 1);
        sampleDao.insertTestData("a_tx", "tx1", 1);
        throw new IllegalArgumentException("deleteData");
    }

    @Override
    public ResponseData modify(SampleUser user) {
        
         return new ResponseData(true);
    }

    @Override
    public void create(SampleUser user) {
        sampleDao.insertTestData("cm_user", "", 2);
    }

}
