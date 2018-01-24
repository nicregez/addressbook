package ch.ctiv.addressbook;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nicolas Regez
 * @since 23 Jan 2018
 */
public interface PersonRepository extends JpaRepository<Person, String> {}
