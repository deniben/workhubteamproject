package com.spring.utils.mapper;

import com.spring.dto.CommentCreationDto;
import com.spring.dto.CommentDto;
import com.spring.entity.Comment;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentsMapper implements DTOMapper<Comment, CommentDto> {

	private ModelMapper modelMapper;

	@Autowired
	public CommentsMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	@Override
	public Comment toEntity(CommentDto dto) {
		return modelMapper.map(dto, Comment.class);
	}
	@Override
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        TypeMap<Comment, CommentDto> toDtoTypeMap = modelMapper.getTypeMap(Comment.class, CommentDto.class);
        if (toDtoTypeMap == null) {
            toDtoTypeMap = modelMapper.createTypeMap(Comment.class, CommentDto.class);
        }

		return toDtoTypeMap.map(comment);
	}

	public Comment toEntityFromCommentCreationDto(CommentCreationDto commentCreationDto) {
		if (commentCreationDto == null) {
			return null;
		}
		Comment comment = new Comment();
		comment.setText(commentCreationDto.getText());
		comment.setProjectId(commentCreationDto.getProjectId());

		return comment;
	}

}
