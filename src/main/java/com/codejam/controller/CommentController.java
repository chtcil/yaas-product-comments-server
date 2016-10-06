package com.codejam.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(value = "{tenant}/products/{productId}/comments")
public class CommentController {
	private final Logger LOG = LoggerFactory.getLogger(CommentController.class);

	// GET all comments for product
	@CrossOrigin()
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Comment>> get(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId) {

		List<Comment> list = new ArrayList<Comment>();
		list.add(new Comment(tenant, productId));
		list.add(new Comment(tenantFromHeader, scopesFromHeader));

		return new ResponseEntity<List<Comment>>(list, HttpStatus.OK);
	}

	// POST create new comment in product
	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Comment> post(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId, @RequestBody Comment comment) {

		Comment newComment = new Comment(comment.author, comment.text);

		return new ResponseEntity<Comment>(newComment, HttpStatus.CREATED);
	}

	// DELETE used to delete comment in product
	@CrossOrigin()
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Comment> delete(
			@RequestHeader(value = "hybris-tenant", defaultValue = "") String tenantFromHeader,
			@RequestHeader(value = "hybris-scopes", defaultValue = "") String scopesFromHeader,
			@PathVariable String tenant, @PathVariable String productId, @PathVariable String id) {

		LOG.info("Comment id: " + id);

		return new ResponseEntity<Comment>(HttpStatus.NO_CONTENT);
	}

}
