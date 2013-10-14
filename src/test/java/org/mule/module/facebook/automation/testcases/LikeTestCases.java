package org.mule.module.facebook.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.tests.ConnectorTestUtils;

public class LikeTestCases extends FacebookTestParent {
	
	@Before
	public void setUp() throws Exception {
		testObjects = (Map<String, Object>) getBeanFromContext("likeTestData");
		
		String profileId = getProfileId();
		testObjects.put("profileId", profileId);
		
		String msg = (String) testObjects.get("msg");
		String messageId = publishMessage(profileId, msg);
		testObjects.put("messageId", messageId);
	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testLike() {
		try {
			String messageId = (String) testObjects.get("messageId");
			testObjects.put("postId", messageId);
			
			MessageProcessor flow = lookupFlowConstruct("like");
			MuleEvent response = flow.process(getTestEvent(testObjects));
						
			Boolean result = (Boolean) response.getMessage().getPayload();
			assertTrue(result);
		}
		catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
	
	@After
	public void tearDown() throws Exception {
		String messageId = (String) testObjects.get("messageId");
		deleteObject(messageId);
	}


}
