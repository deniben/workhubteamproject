package com.spring.test.objects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class MultipartTestFile implements MultipartFile {

	private String name;
	private Long size;
	private InputStream inputStream;

	public MultipartTestFile(String name, Long size) {
		this.name = name;
		this.size = size;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return name;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return new byte[0];
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {

	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
