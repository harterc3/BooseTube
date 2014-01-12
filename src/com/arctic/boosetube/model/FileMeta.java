package com.arctic.boosetube.model;

public class FileMeta {
	private String fileName;
	private String fileSize;
	private String fileType;

	private byte[] bytes;
	
	public FileMeta() {
		fileName = "";
		fileSize = "";
		fileType = "";
		bytes = null;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
		this.fileSize = String.format("%.2fkb",(bytes.length / 1000.0));
	}
}
