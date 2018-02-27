package com.scienceroot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

	private Environment environment;

	@Autowired
	public ResourceService(Environment environment) {
		this.environment = environment;
	}

	public Resource loadFromResourcesFolder(String filename) {
		if (environment.acceptsProfiles("staging")) {
			return new FileSystemResource("/" + filename);
		} else {
			return new ClassPathResource(filename);
		}
	}
}
