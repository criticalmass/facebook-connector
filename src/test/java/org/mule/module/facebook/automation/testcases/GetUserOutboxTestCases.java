package org.mule.module.facebook.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.facebook.types.OutboxThread;
import org.mule.modules.tests.ConnectorTestUtils;

public class GetUserOutboxTestCases extends FacebookTestParent {

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		testObjects = (Map<String, Object>) getBeanFromContext("getUserOutboxTestData");
			
		String profileId = getProfileId();
		testObjects.put("user", profileId);
	}
	
	@SuppressWarnings("unchecked")
	@Category({RegressionTests.class})
	@Test
	public void testGetUserOutbox() {
		try {
			MessageProcessor flow = lookupFlowConstruct("get-user-outbox");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			
			List<OutboxThread> result = (List<OutboxThread>) response.getMessage().getPayload();
			assertNotNull(result);
		}
		catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
	
}
