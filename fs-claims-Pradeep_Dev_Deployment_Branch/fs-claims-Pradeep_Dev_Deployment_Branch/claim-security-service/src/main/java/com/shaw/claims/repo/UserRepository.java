package com.shaw.claims.repo;

import com.shaw.claims.model.UserGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT u FROM Users u WHERE u.userId > 0 AND u.statusId != 3 ORDER BY u.createdDateTime DESC")
    Page<Users> findAllActiveUsers(Pageable pageable);

    @Query("SELECT u FROM Users u WHERE u.userId > 0 AND u.statusId != 3 ORDER BY u.createdDateTime DESC")
    List<Users> findAllActiveUsers();

    Optional<Users> findByActiveDirectoryId(String activeDirectoryId);

	Optional<Users> findByObjectId(String objectId);
    @Query("SELECT CONCAT(u.firstName, ' ', u.lastName) FROM Users u WHERE u.userId IN (:userIds)")
    List<String> findUserNames(List<Integer> userIds);
    @Query("SELECT CONCAT(u.firstName, ' ', u.lastName) FROM Users u WHERE u.userId IN (:userId)")
    String findUserName(int userId);
}
