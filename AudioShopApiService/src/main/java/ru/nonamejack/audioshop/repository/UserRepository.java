package ru.nonamejack.audioshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nonamejack.audioshop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
