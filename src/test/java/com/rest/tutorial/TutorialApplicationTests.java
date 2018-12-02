package com.rest.tutorial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.rest.tutorial.controller.TvSeriesController;
import com.rest.tutorial.dao.TvCharacterDao;
import com.rest.tutorial.dao.TvSeriesDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TutorialApplicationTests {
    /**
     * @MockBean可给当前的spring context装载一个假的bean上去替代原有的同名bean；
     * mock了dao的所有bean后，数据访问层就别接管了，从而实现测试不受具体数据库内数据值影响的结果
     */
    @MockBean TvSeriesDao seriesDao;
    @MockBean TvCharacterDao characterDao;
    
	@Autowired private MockMvc mockMvc;
	@Autowired private TvSeriesController tvSeriesController;

	@Test
	public void contextLoads() throws Exception {
        //这个方法虽然啥内容都没有，但如果spring-boot的配置有问题，例如需要autowire的bean不存在等，是不能被执行到这步的。
        //所以，如果没有任何测试用例时，写这么个空的也是好过没有的。如果有了其他有具体内容的测试用例，这个空测试用例就没存在的必要了。
	}

	
}
