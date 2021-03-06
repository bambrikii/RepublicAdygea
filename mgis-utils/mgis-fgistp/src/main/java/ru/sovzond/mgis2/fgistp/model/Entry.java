package ru.sovzond.mgis2.fgistp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class Entry {
	private String id;
	private String documentsLinks;
	private String usersLinks;
	private String childrenLinks;
	private String parentLink;
	private String typeLink;

	private String updated;
	private Map<String, String> properties = new HashMap<>();

	private List<Entry> entries = new ArrayList<>();

	private List<Entry> files = new ArrayList<>();

	private List<Entry> children = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocumentsLinks() {
		return documentsLinks;
	}

	public void setDocumentsLinks(String documentsLinks) {
		this.documentsLinks = documentsLinks;
	}

	public String getUsersLinks() {
		return usersLinks;
	}

	public void setUsersLinks(String usersLinks) {
		this.usersLinks = usersLinks;
	}

	public String getChildrenLinks() {
		return childrenLinks;
	}

	public void setChildrenLinks(String childrenLinks) {
		this.childrenLinks = childrenLinks;
	}

	public String getParentLink() {
		return parentLink;
	}

	public void setParentLink(String parentLink) {
		this.parentLink = parentLink;
	}

	public String getTypeLink() {
		return typeLink;
	}

	public void setTypeLink(String typeLink) {
		this.typeLink = typeLink;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@XmlElementWrapper(name = "entries")
	@XmlElement(name = "entry")
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file")
	public List<Entry> getFiles() {
		return files;
	}

	public void setFiles(List<Entry> files) {
		this.files = files;
	}

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "child")
	public List<Entry> getChildren() {
		return children;
	}

	public void setChildren(List<Entry> children) {
		this.children = children;
	}
}
