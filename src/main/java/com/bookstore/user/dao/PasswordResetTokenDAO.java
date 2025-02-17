/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.dao;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.user.domain.User;
import com.bookstore.user.domain.security.PasswordResetToken;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {
	
	PasswordResetToken findByToken(String token);

	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
	
	@Modifying
	@Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
	
}
