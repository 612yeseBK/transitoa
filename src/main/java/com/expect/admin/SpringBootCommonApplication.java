package com.expect.admin;

import com.expect.admin.service.TransPerRecordService;
import com.expect.admin.service.WFPointService;
import com.expect.admin.service.WorkFlowService;
import com.expect.admin.utils.SpringContextUtils;
import com.expect.admin.web.WorkFlowController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableCaching
public class SpringBootCommonApplication{

	//启动版
//	public static void main(String[] args) {
//		SpringApplication.run(SpringBootCommonApplication.class, args);
//	}

	//测试版
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SpringBootCommonApplication.class, args);
		SpringContextUtils springContextUtils = new SpringContextUtils();
		springContextUtils.setApplicationContext(applicationContext);
		TransPerRecordService tpr = SpringContextUtils.getBean(TransPerRecordService.class);
		WorkFlowController wfc = SpringContextUtils.getBean(WorkFlowController.class);
		WFPointService wfs = SpringContextUtils.getBean(WFPointService.class);
		WorkFlowService workFlowService = SpringContextUtils.getBean(WorkFlowService.class);
//		workFlowService.delete("2c9302ab635880510163588065920000");
//		wfs.deleteOne("2c9302ab635880510163588065b70001");
//		wfs.getBeforeAndNext("2c9302ab635880510163588065c20002");
//		wfc.testadd();
	}

	/**
	 * 文件上传配置
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//文件最大
		factory.setMaxFileSize("21504KB"); //KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("215040KB");
		return factory.createMultipartConfig();
	}

}
