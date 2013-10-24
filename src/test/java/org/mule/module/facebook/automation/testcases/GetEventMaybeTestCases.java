/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.facebook.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import com.restfb.types.User;

public class GetEventMaybeTestCases extends FacebookTestParent {
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
    	initializeTestRunMessage("getEventMaybeTestData");

    	String profileId = getProfileId();
    	
    	String auxProfileId = getProfileIdAux();
    	
    	String eventName = getTestRunMessageValue("eventName");
    	String startTime = getTestRunMessageValue("startTime");
    	
    	String eventId = publishEventAux(auxProfileId, eventName, startTime);
		tentativeEvent(eventId);

		upsertOnTestRunMessage("eventId", eventId);
    	upsertOnTestRunMessage("profileId", profileId);
    	upsertOnTestRunMessage("auxProfileId", auxProfileId);
	}
	
	@SuppressWarnings("unchecked")
	@Category({RegressionTests.class})
	@Test
	public void testGetEventMaybe() {
		try {
			String profileId = getTestRunMessageValue("profileId");
			
			List<User> users = runFlowAndGetPayload("get-event-maybe");
			assertTrue(users.size() == 1);

			User user = users.get(0);
			assertEquals(user.getId(), profileId);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
    
	@After
	public void tearDown() throws Exception {
		String eventId = getTestRunMessageValue("eventId");
		deleteObjectAux(eventId);
	}
	
}