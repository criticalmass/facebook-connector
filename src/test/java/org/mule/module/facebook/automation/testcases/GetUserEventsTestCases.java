/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.facebook.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.tests.ConnectorTestUtils;

import com.restfb.types.Event;

public class GetUserEventsTestCases extends FacebookTestParent {
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
    	testObjects = (HashMap<String,Object>) context.getBean("getUserEventsTestData");
			
    	String profileId = getProfileId();
    	testObjects.put("user", profileId);
	    	
    	String eventId = publishEvent(profileId, (String) testObjects.get("eventName"), (String) testObjects.get("startTime"));
    	testObjects.put("objectId", eventId);
	}
	
    @SuppressWarnings("unchecked")
	@Category({RegressionTests.class})
	@Test
	public void testGetUserEvents() {
		try {
			MessageProcessor flow = lookupFlowConstruct("get-user-events");
			MuleEvent response = flow.process(getTestEvent(testObjects));

			List<Event> result = (List<Event>) response.getMessage().getPayload();
			
			assertTrue(result.size() != 0);
			
			String eventId = (String) testObjects.get("objectId");
			boolean containsPublishedEvent = false;
			for(Event event : result) {
				if(eventId.equals(event.getId())) {
					containsPublishedEvent = true;
					break;
				}
			}
			
			assertTrue(containsPublishedEvent);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}
    
    @After
	public void tearDown() throws Exception {
		deleteObject((String) testObjects.get("objectId"));
	}
    
}