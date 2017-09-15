package com.github.shu1jia1.site.sample.service;

import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.sample.entity.SampleUser;

public interface ISampleService {
    ResponseData modify(SampleUser user);
    
    void create(SampleUser user);
    
    void tranRequire();
    
    void tranDefault();
    
    void tranNerver();
    
    void tranReadOnly();
}
