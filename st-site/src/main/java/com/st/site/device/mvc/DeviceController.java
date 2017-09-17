package com.st.site.device.mvc;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.shu1jia1.common.exception.ErrorCodeException;
import com.github.shu1jia1.site.base.entity.ResponseData;
import com.st.site.device.entity.DeviceInfo;
import com.st.site.device.service.DeviceService;

@Controller
@RequestMapping("/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
    public @ResponseBody ResponseData<List<DeviceInfo>> list(
            @RequestParam(value = "devId", required = false) String devId) throws ErrorCodeException {
        logger.info("receive listDeviceInfo request ,{}", devId);
        return deviceService.listByGenomativeId(devId);
    }

}
