package org.example.memoryauthentication.repository;

import org.example.memoryauthentication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

}
