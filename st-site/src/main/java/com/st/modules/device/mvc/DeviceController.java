package com.st.modules.device.mvc;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.github.shu1jia1.common.exception.ErrorCodeException;
import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.sample.entity.SampleUser;
import com.github.shu1jia1.site.sample.service.ISampleService;
import com.st.modules.device.entity.DeviceInfo;
import com.st.modules.device.service.IDeviceService;

@Controller
@RequestMapping("/device")
public class deviceController {
    private Logger logger = LoggerFactory.getLogger(deviceController.class);

    @Resource(name = "")
    private IDeviceService deviceService;

    @RequestMapping("")
    public @ResponseBody ResponseData<List<DeviceInfo>> list(@RequestParam("devId") String devId)
            throws ErrorCodeException {
        return deviceService.listByGenomativeId(devId);
    }

}
