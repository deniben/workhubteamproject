package com.spring.controller;

import com.spring.dto.CommentCreationDto;
import com.spring.dto.CommentDto;
import com.spring.dto.PageableResponse;
import com.spring.dto.ProfileDto;
import com.spring.entity.Comment;
import com.spring.entity.Profile;
import com.spring.service.CommentsService;
import com.spring.service.ProfileService;
import com.spring.utils.mapper.CommentsMapper;
import com.spring.utils.mapper.ProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

	private final CommentsService commentsService;
	private final ProfileService profileService;
	private final CommentsMapper commentsMapper;
	private final ProfileMapper profileMapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentsController.class);

	@Autowired
	public CommentsController(CommentsService commentsService, ProfileService profileService, CommentsMapper commentsMapper, ProfileMapper profileMapper) {
		this.commentsService = commentsService;
		this.profileService = profileService;
		this.commentsMapper = commentsMapper;
		this.profileMapper = profileMapper;
	}

    @GetMapping("/{id}/pages/{page}")
    public ResponseEntity<PageableResponse> getCommentsByProject(@PathVariable Long id, @PathVariable(required = false) Integer page) {
	    LOGGER.debug("in getCommentsByProject(), id: {}, page: {}", id, page);

	    Page<Comment> comments = commentsService.getByProject(id, page == null ? 0 : page);
        List<CommentDto> commentDtos = mapCommentToDto(comments.getContent());

        return ResponseEntity.ok(new PageableResponse(comments.getTotalPages(), commentDtos));
    }

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody CommentCreationDto commentCreationDto) {
	    LOGGER.debug("in addComment(), commentCreationDto: {}", commentCreationDto);

	    Comment comment = commentsMapper.toEntityFromCommentCreationDto(commentCreationDto);

        if (commentCreationDto.getReplyId() != null) {
            commentsService.replyComment(comment, commentCreationDto.getReplyId());
        } else {
            commentsService.addComment(comment);
        }

        return ResponseEntity.ok("Successfully added your comment");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
	    LOGGER.debug("in deleteComment(), id: {}", id);

	    commentsService.deleteCommentById(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    private List<CommentDto> mapCommentToDto(List<Comment> commentsList) {
	    LOGGER.debug("in mapCommentToDto(), commentsList: {}", commentsList);

	    List<CommentDto> commentDtos = new ArrayList<>();
        if (commentsList != null && !commentsList.isEmpty()) {
            for (Comment comment : commentsList) {
                CommentDto commentDto = commentsMapper.toDto(comment);
                Profile authorProfile = profileService.findById(comment.getUserId());
                ProfileDto authorDto = profileMapper.toDto(authorProfile);
                commentDto.setAuthor(authorDto);
                commentDto.setReplies(mapCommentToDto(comment.getReplies()));
                commentDtos.add(commentDto);
            }
        }
        return commentDtos;
    }

}
