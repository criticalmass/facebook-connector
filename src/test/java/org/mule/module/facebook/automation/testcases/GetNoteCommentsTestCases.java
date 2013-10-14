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

import com.restfb.types.Comment;

public class GetNoteCommentsTestCases extends FacebookTestParent {
	// create note.
	// add comment to note

	@Before
	public void setUp() throws Exception {
		testObjects = (HashMap<String, Object>) getBeanFromContext("getNoteCommentsTestData");
		String msg = testObjects.get("msg").toString();
		String subject = testObjects.get("subject").toString();
		String profileId = getProfileId();
		testObjects.put("profileId", profileId);
		
		String noteid = publishNote(profileId, msg, subject);
		testObjects.put("note", noteid);
		
		String commentId = publishComment(noteid, msg);
		testObjects.put("commentId", commentId);
	}

	@Category({RegressionTests.class})
	@Test
	public void testGetNoteComments() {
		try {
			String commentId = testObjects.get("commentId").toString();
			String msg = testObjects.get("msg").toString();

			MessageProcessor flow = lookupFlowConstruct("get-note-comments");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			List<Comment> comments = (List<Comment>) response.getMessage().getPayload();

			Boolean found = false;

			for (Comment comment : comments) {
				if (comment.getId().toString().equals(commentId)) {
					assertEquals(msg, comment.getMessage());
					found = true;
					break;
				}
			}
			assertTrue(found);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
	
	//note cannot be deleted with delete object
	@After
	public void tearDown() throws Exception {
		String note = (String) testObjects.get("note");
		deleteObject(note);
	}
}
