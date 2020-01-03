package com.spring.utils.mapper;

import com.spring.dto.DocumentDto;
import com.spring.entity.FileDocument;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper implements DTOMapper<FileDocument, DocumentDto> {

	private ModelMapper modelMapper;

	@Autowired
	public DocumentMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public FileDocument toEntity(DocumentDto dto) {
		return modelMapper.map(dto, FileDocument.class);
	}

	public DocumentDto toDto(FileDocument fileDocument) {
		if (fileDocument == null) {
			return null;
		}
		TypeMap<FileDocument, DocumentDto> toDtoTypeMap = modelMapper.getTypeMap(FileDocument.class, DocumentDto.class);
		if (toDtoTypeMap == null) {
			toDtoTypeMap = modelMapper.createTypeMap(FileDocument.class, DocumentDto.class);
		}
		toDtoTypeMap.addMapping(FileDocument::getFileName, DocumentDto::setName)
				.addMappings(x -> x.skip(DocumentDto::setOwner));

		return toDtoTypeMap.map(fileDocument);
	}

}
