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

import java.io.File;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.facebook.types.Photo;
import org.mule.modules.tests.ConnectorTestUtils;

public class GetPhotoTestCases extends FacebookTestParent {
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		testObjects = (HashMap<String,Object>) context.getBean("getPhotoTestData");
		String profileId = getProfileId();
		testObjects.put("user", profileId);
		
		String caption = (String) testObjects.get("caption");
		File photoFile = new File(getClass().getClassLoader().getResource((String) testObjects.get("photoFilePath")).toURI());
		
		String photoId = publishPhoto(profileId, caption, photoFile);
		testObjects.put("photo", photoId);
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testGetPhoto() {
		try {
			MessageProcessor flow = lookupFlowConstruct("get-photo");
			MuleEvent response = flow.process(getTestEvent(testObjects));

			Photo result = (Photo) response.getMessage().getPayload();
			assertTrue(result.getId().equals((String) testObjects.get("photo")));
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
	
	@After
	public void tearDown() throws Exception {
		String photoId = (String) testObjects.get("photo");
		deleteObject(photoId);
	}
    
}