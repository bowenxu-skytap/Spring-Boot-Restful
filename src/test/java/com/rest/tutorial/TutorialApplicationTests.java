package com.rest.tutorial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.rest.tutorial.controller.TvSeriesController;
import com.rest.tutorial.dao.TvCharacterDao;
import com.rest.tutorial.dao.TvSeriesDao;
import com.rest.tutorial.pojo.TvCharacter;
import com.rest.tutorial.pojo.TvSeries;

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

	@Test
	public void testGetAll() throws Exception {
		List<TvSeries> list = new ArrayList<>();
		TvSeries ts = new TvSeries();
		ts.setId(1);
		ts.setName("POI");
		list.add(ts);
		
		List<TvCharacter> characters = new ArrayList<>();
		TvCharacter tc = new TvCharacter();
		tc.setId(1);
		tc.setName("John");
		tc.setTvSeriesId(1);
		characters.add(tc);
		
		//下面这个语句是告诉mock出来tvSeriesDao当执行getAll方法时，返回上面创建的那个list
		Mockito.when(seriesDao.getAll()).thenReturn(list);
		Mockito.when(characterDao.getAllByTvSeriesId(ts.getId())).thenReturn(characters);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/tvseries")).andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("POI")));
		//上面这几句和字面意思一致，期望状态是200，返回值包含POI三个字面，桩模块返回的一个电视剧名字是POI，如果测试正确是包含这三个字母的。
	}
	
	@Test
    public void testAddSeries() throws Exception{
        BitSet bitSet = new BitSet(1);
        bitSet.set(0, false);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                TvSeries ts = (TvSeries) args[0];
                Assert.assertEquals(ts.getName(), "westlife");
                ts.setId(5432);
                bitSet.set(0, true);
                return 1;
            }
        }).when(seriesDao).insert(Mockito.any(TvSeries.class));
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                TvCharacter tc = (TvCharacter) args[0];
                //应该是json中传递过来的剧中角色名字
                Assert.assertEquals(tc.getName(), "jack");
                Assert.assertTrue(tc.getTvSeriesId() == 5432);
                bitSet.set(0, true);
                return 1;
            }
        }).when(characterDao).insert(Mockito.any(TvCharacter.class));
        
        String jsonData = "{\"name\":\"westlife\",\"seasonCount\":5,\"originRelease\":\"2011-09-22\",\"tvCharacters\":[{\"name\":\"jack\"}, {\"name\":\"jack\"}]}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/tvseries").contentType(MediaType.APPLICATION_JSON).content(jsonData))
                     .andDo(MockMvcResultHandlers.print())
                     .andExpect(MockMvcResultMatchers.status().isOk());
        
        //确保tvCharacterDao.insert被访问过
        Assert.assertTrue(bitSet.get(0));
    }
}
