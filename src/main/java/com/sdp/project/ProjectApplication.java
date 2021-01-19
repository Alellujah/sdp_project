package com.sdp.project;

import com.sdp.project.model.ArmazemCentral;
import com.sdp.project.model.Item;
import com.sdp.project.model.ItemEntrega;
import com.sdp.project.repository.ArmazemCentralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
