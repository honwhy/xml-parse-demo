package com.honey.app;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String[] location = new String[]{
    			"spring/applicationContext.xml"	
    		};
		
		AbstractApplicationContext context =
				new ClassPathXmlApplicationContext(location);
    		
    }
}
