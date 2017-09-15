package com.github.shu1jia1.site.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
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

//@Controller
public class SampleController {
	private Logger logger = LoggerFactory.getLogger(SampleController.class);

	public @ResponseBody String api(@PathVariable("path") String path, HttpServletRequest request)
			throws ErrorCodeException {
		return "request " + path + " from tomcat";
	}

	@RequestMapping(value = "/item", method = RequestMethod.GET)
	public @ResponseBody String item(HttpSession session, HttpServletRequest request) {
		logger.info(request.getParameter("status"));
		return request.getParameter("status");
	}

	@RequestMapping("/home")
	public String homev3(HttpServletRequest request) {
		String p = request.getParameter("p");
		if (!StringUtils.isEmpty(p)) {
			return "pad/index";
		}
		return "home";
	}

	@RequestMapping(value = "testput/test", method = RequestMethod.PUT)
	public @ResponseBody String sampleput(Model model) {
		return "receivePutMessage";
	}

	@RequestMapping(value = "testput/test", method = RequestMethod.GET)
	public @ResponseBody String samplegut(Model model) {
		return "receiveGutMessage";
	}

	@RequestMapping("sample/home")
	public String home(Model model) {
		return "sample/home";
	}

	@RequestMapping("sample/menu")
	public String listuser(Model model) {
		return "sample/menu";
	}

	@RequestMapping("sample/menuhtml")
	public String listmenu(Model model) {
		return "sample/menu";
	}

	@RequestMapping("sample/{url}")
	public String listurl(Model model, @PathVariable String url) {
		return "sample/" + url;
	}

	@RequestMapping("/listBean")
	public String showUser(HttpServletRequest request, Model model) {
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getServletContext());
		model.addAttribute("bean", context.getEnvironment());
		return "listBean";
	}

	@RequestMapping(value = "sample/nodes", method = RequestMethod.GET)
	public @ResponseBody String listSubNodes() {
		return "noparam";
	}

	@RequestMapping(value = "sample/nodes", params = "nodeid", method = RequestMethod.GET)
	public @ResponseBody String listbyId(@RequestParam String nodeid) {
		return "params: node id:" + nodeid;
	}

	@RequestMapping(value = "sample/nodes", params = "nodename", method = RequestMethod.GET)
	public @ResponseBody String listbyName(@RequestParam String nodename) {
		return "params: node name:" + nodename;
	}

	@RequestMapping(value = "sample/nodes", params = { "parentid", "parenttype" }, method = RequestMethod.GET)
	public @ResponseBody String listsubnodes(@RequestParam String parenttype, @RequestParam String parentid) {
		return "params: parenttype:" + parenttype + ", String parentid:" + parentid;
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseData modifyRegion(@PathVariable("id") String id, @RequestBody SampleUser data) throws ErrorCodeException {
        data.setId(id);
        return sampleService.modify(data);
    }
	@Autowired
	private ISampleService sampleService;

	@RequestMapping(value = "/tranr")
	public void insertData() {
		sampleService.tranRequire();
	}

	@RequestMapping(value = "/trand")
	public void testData() {
		sampleService.tranDefault();
	}

	@RequestMapping(value = "/trann")
	public void tranDefault() {
		sampleService.tranDefault();
	}

}
