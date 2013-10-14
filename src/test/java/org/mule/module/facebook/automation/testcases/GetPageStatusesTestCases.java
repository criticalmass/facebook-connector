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

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.tests.ConnectorTestUtils;

import com.restfb.types.StatusMessage;

public class GetPageStatusesTestCases extends FacebookTestParent {
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		testObjects = (HashMap<String,Object>) getBeanFromContext("getPageStatusesTestData");

		String page = (String) testObjects.get("page");
		String msg = (String) testObjects.get("msg");
		
		String messageId = publishMessage(page, msg);
		testObjects.put("messageId", messageId);
	}
	
    @SuppressWarnings("unchecked")
	@Category({RegressionTests.class})
	@Test
	public void testGetPageStatuses() {
		try {
			String pageId = (String) testObjects.get("page");
			String messageId = (String) testObjects.get("messageId");
			
			MessageProcessor flow = lookupFlowConstruct("get-page-statuses");
			MuleEvent response = flow.process(getTestEvent(testObjects));

			List<StatusMessage> result = (List<StatusMessage>) response.getMessage().getPayload();
			assertTrue(result.size() == 1);
			
			StatusMessage message = result.get(0);
			assertEquals(pageId + "_" + message.getId(), messageId);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}

    @After
    public void tearDown() throws Exception {
    	String messageId = (String) testObjects.get("messageId");
    	deleteObject(messageId);
    }
    
}