package com.electonic.store;

import com.electonic.store.entities.Role;
import com.electonic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
@Autowired
private RoleRepository roleRepository;
	@Value("${normal.user.id}")
	private String normalId;
	@Value("${admin.user.id}")
	private String adminId;
	@Override
	public void run(String... args) throws Exception {
		try {
			Role roleNormal = Role.builder().roleId(normalId).roleName("ROLE_NORMAL").build();
			Role roleAdmin = Role.builder().roleId(adminId).roleName("ROLE_ADMIN").build();
			roleRepository.save(roleAdmin);
			roleRepository.save(roleNormal);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
