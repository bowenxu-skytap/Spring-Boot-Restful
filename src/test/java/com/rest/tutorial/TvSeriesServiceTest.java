package com.rest.tutorial;

import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rest.tutorial.dao.TvCharacterDao;
import com.rest.tutorial.dao.TvSeriesDao;
import com.rest.tutorial.pojo.TvCharacter;
import com.rest.tutorial.pojo.TvSeries;
import com.rest.tutorial.service.TvSeriesService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TvSeriesServiceTest {

	@MockBean TvSeriesDao seriesDao;
	@MockBean TvCharacterDao characterDao;
	
	@Autowired TvSeriesService tvSeriesService;
	
	@Test
	public void testGetAll() {
		//设置一个TvSeries List
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
		
		//下面这个语句是告诉mock出来tvSeriesDao当执行getAll方法时，返回上面创建的那个list
		Mockito.when(seriesDao.getAll()).thenReturn(list);
		Mockito.when(characterDao.getAllByTvSeriesId(ts.getId())).thenReturn(characters);
		//测试tvSeriesService的getAllTvSeries()方法，获得返回值
		List<TvSeries> res = tvSeriesService.getAllTvSeries();
		//判断返回值是否和最初做的那个list相同，应该是相同的。
		Assert.assertEquals(list.size(), res.size());
		Assert.assertEquals("POI", res.get(0).getName());
	}
	
	@Test
	public void testUpdateOne() {
		//根据不同的传入参数，被mock的bean返回不同的数据的例子
		BitSet mockExecuted = new BitSet();
		String newName = "prison break";
		Mockito.doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				TvSeries bean = (TvSeries) args[0];
				//传入的值，应该和下面调用tvSeriesService.updateTvSeries()方法时的参数中的值相同
				Assert.assertEquals(newName, bean.getName());
				mockExecuted.set(0);
				return 1;
			}
			
        }).when(seriesDao).update(any(TvSeries.class));
		
		TvSeries ts = new TvSeries();
		ts.setId(2);
		ts.setName(newName);
		
		tvSeriesService.updateTvSeries(ts);
		Assert.assertTrue(mockExecuted.get(0));
	}
	
    @Test
    public void testGetAllWithoutMockit() { // this is a bad test case
        List<TvSeries> list = tvSeriesService.getAllTvSeries();
        //这里的测试结果依赖连接数据库内的记录，很难写一个判断是否成功的条件，甚至无法执行
        //上面的testGetAll()方法，使用了mock出来的dao作为桩模块，避免了这一情形
        Assert.assertTrue(list.size() >= 0);
    }
}
