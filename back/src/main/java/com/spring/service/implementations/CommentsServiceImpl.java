package com.spring.service.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.spring.component.UserContext;
import com.spring.entity.Comment;
import com.spring.entity.User;
import com.spring.exception.RestException;
import com.spring.repository.CommentsRepository;
import com.spring.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {

	private final CommentsRepository commentsRepository;
	private final UserContext userContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentsServiceImpl.class);


	private static final Integer PAGE_SIZE = 5;

	@Autowired
	public CommentsServiceImpl(CommentsRepository commentsRepository, UserContext userContext) {
		this.commentsRepository = commentsRepository;
		this.userContext = userContext;
	}

	@Override
	public Page<Comment> getByProject(Long id, Integer page) {
		LOGGER.debug("in getByProject(id: {}, page: {})", id, page);
		return commentsRepository.findByProjectId(id, PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("dateTime"))));
	}

	@Override
	public void addComment(Comment comment) {
		LOGGER.debug("in addComment(comment: {})", comment);

		commentsRepository.save(fillData(comment));
	}

	private Comment fillData(Comment comment) {
		LOGGER.debug("in fillData(comment: {})", comment);

		if(comment.getText() == null || comment.getText().isEmpty() || comment.getText().length() > 256) {
			throw new RestException("Comment's text must be between 0 and 256 chars", HttpStatus.BAD_REQUEST.value());
		}

		User user = userContext.getCurrentUser();

		comment.setDateTime(LocalDateTime.now());
		comment.setUserId(user.getId());

		return comment;
	}

	@Override
	public void replyComment(Comment comment, String replyId) {
		LOGGER.debug("in replyComment(comment: {}, replyId: {})", comment, replyId);

		Comment currentComment = fillData(comment);
		currentComment.setId(new ObjectId().toString());

		Optional<Comment> replyCommentOptional = commentsRepository.findById(replyId);

		if(!replyCommentOptional.isPresent()) {
			throw new RestException("Can not find such comment to reply", HttpStatus.BAD_REQUEST.value());
		}

		Comment replyComment = replyCommentOptional.get();
		List<Comment> oldReplies = replyComment.getReplies();
		oldReplies.add(currentComment);
		replyComment.setReplies(oldReplies);

		commentsRepository.save(replyComment);
	}

	@Override
	public void deleteCommentById(String id) {
		LOGGER.debug("in deleteCommentById(id: {})", id);


		String[] ids = id.split(":");

		if(ids != null && ids.length > 1) {

			Optional<Comment> commentOptional = commentsRepository.findById(ids[0]);

			if(commentOptional.isPresent()) {
				Comment currentComment = commentOptional.get();
				List<Comment> comments = currentComment.getReplies();

				Optional<Comment> comment = comments.stream().filter(x -> x.getId().equals(ids[1])).findFirst();

				if(comment.isPresent()) {

					Comment innerComment = comment.get();

					checkPermission(innerComment);
					comments.remove(innerComment);

					currentComment.setReplies(comments);
					commentsRepository.save(currentComment);
					return;
				}

			}

			throw new RestException("Invalid reply reference", HttpStatus.BAD_REQUEST.value());

		}

		Optional<Comment> commentOptional = commentsRepository.findById(id);

		if(commentOptional.isPresent()) {
			Comment comment = commentOptional.get();
			checkPermission(comment);
			commentsRepository.delete(comment);
			return;
		}

		throw new RestException("Invalid request", HttpStatus.BAD_REQUEST.value());
	}

	private void checkPermission(Comment comment) {
		LOGGER.debug("in checkPermission(comment: {})", comment);
		User current = userContext.getCurrentUser();
		if(!comment.getUserId().equals(current.getId()) && !current.isAdmin()) {
			throw new RestException("Invalid permission");
		}

	}

}
