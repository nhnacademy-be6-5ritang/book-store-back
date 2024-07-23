package com.nhnacademy.bookstoreback.global.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

	private final byte[] content;
	private final String name;
	private final String contentType;

	public CustomMultipartFile(byte[] content, String name, String contentType) {
		this.content = content;
		this.name = name;
		this.contentType = contentType;
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
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return content.length == 0;
	}

	@Override
	public long getSize() {
		return content.length;
	}

	@Override
	public byte[] getBytes() {
		return content;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(content);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		throw new UnsupportedOperationException("transferTo is not supported in this implementation.");
	}
}