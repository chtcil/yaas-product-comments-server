package com.codejam.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codejam.model.Comment;
import com.codejam.utils.DocumentRepo;
import com.codejam.utils.OAuth;

@RestController
@RequestMapping(value = "{tenant}/products/{productId}/comments")
public class CommentController {
	private final Logger LOG = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private OAuth oauth;
	@Autowired
	private DocumentRepo documentRepo;

	// GET all comments for product
	@CrossOrigin()
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Comment>> get(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId) {

		// Get access token
		String token = oauth.getAuthToken("nemanjadev");
		
		//Get items from document repo
		Comment[] objects = documentRepo.getAll(token, tenant, productId, Comment[].class);
		
		//Cast to comments
		List<Comment> comments = new ArrayList<Comment>();
		for	(Comment object : objects){
			comments.add(object);
		}
		
		//Return to client
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}

	// POST create new comment in product
	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> post(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId, @RequestBody Comment comment) {

		// Get access token
		String token = oauth.getAuthToken("nemanjadev");

		//Create comment object
		Comment newComment = new Comment(comment.author, comment.text);
		
		//Save in Document Repo
		String result = documentRepo.post(newComment, tenant, token, productId);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	// DELETE used to delete comment in product
	@CrossOrigin()
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Comment> delete(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId, @PathVariable String id) {

		// Get access token
		String token = oauth.getAuthToken("nemanjadev");

		LOG.info("Comment id: " + id);
		
		HttpStatus status = documentRepo.delete(id, tenant, token, productId);

		return new ResponseEntity<Comment>(status);
	}

}
