package coreGuiRavage;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.system.Time;

import ravage.IBaseRavage;


public class Test implements IBaseRavage
{
	private static Model m_model = new Model();
	
	public Test()
	{
		m_model.m_tests.add(this);
	}
	
	public static Model getModel()
	{
		return m_model;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		System.out.println("Mon nom: " + this);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public static class Model implements IBaseRavage
	{
		private  List<Test> m_tests = new ArrayList<Test>();
		
		public List<Test> getTests()
		{
			return m_tests;
		}

		@Override
		public void init() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(Time deltaTime) 
		{
			for(Test test : this.m_tests)
				test.update(deltaTime);
				
			
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	
}
