package com.github.shu1jia1.site.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/servlet-context.xml", "classpath:spring/spring-radis-context.xml",
        "classpath:spring/spring-beans.xml", /* "classpath:spring/spring-security.xml", */
        "classpath:spring/springmvc-mybatis.xml", })
public class SampleControllerTest extends SampleController {

    @Autowired
    private SampleController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        //绑定需要测试的Controller到MockMvcshang  
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testSample() throws Exception {
        mockMvc.perform(get("/regions")).andExpect(status().isOk()).andExpect(content().contentType("text/plain;charset=ISO-8859-1"));
        //.andExpect(content().string("Hello world!"));
    }

}
