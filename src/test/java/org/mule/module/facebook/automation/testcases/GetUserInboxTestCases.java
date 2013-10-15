/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.facebook.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

public class GetUserInboxTestCases extends FacebookTestParent {
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
    	initializeTestRunMessage("getUserInboxTestData");
			
    	String profileId = getProfileId();
    	upsertOnTestRunMessage("user", profileId);
	}
	
    @SuppressWarnings("unchecked")
	@Category({RegressionTests.class})
	@Test
	public void testGetUserInbox() {
		try {
			List<org.mule.module.facebook.types.Thread> result = runFlowAndGetPayload("get-user-inbox");
			assertNotNull(result);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
    
}