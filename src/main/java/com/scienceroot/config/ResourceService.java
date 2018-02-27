package com.scienceroot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ResourceService {

	private Logger LOG = Logger.getLogger(ResourceService.class.getName());
	private Environment environment;

	@Autowired
	public ResourceService(Environment environment) {
		this.environment = environment;
	}

	public Resource loadFromResourcesFolder(String filename) {

		LOG.info("loading file '" + filename + "'");

		if (environment.acceptsProfiles("staging")) {
			LOG.info("loading file as FileSystemResource '/" + filename + "'");
			return new FileSystemResource("/" + filename);
		} else {
			LOG.info("loading file as ClassPathResource '" + filename + "'");
			return new ClassPathResource(filename);
		}
	}
}
