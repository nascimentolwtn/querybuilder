package net.sf.esfinge.querybuilder.domainterms;

import static net.sf.junit.Assert.assertEquals;

import java.util.List;

import net.sf.esfinge.querybuilder.mongodb.testresources.Person;
import net.sf.esfinge.querybuilder.mongodb.testresources.QueryBuilderDatabaseTest;
import net.sf.junit.Before;
import net.sf.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;

public class QueryBuilderMongoDBDomainTermsTest extends QueryBuilderDatabaseTest{
	
	private TestDomainQuery tq;

	@Before
	public void setupDatabase() throws Exception {
		initializeDatabase("/initial_db_domain.xml");
		tq = QueryBuilder.create(TestDomainQuery.class);
	}
	
	@Test
	public void domainQuery(){
		List<Person> list = tq.getPersonTeenager();
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0).getName());
		assertEquals("The list should have Maria", "Maria", list.get(1).getName());
	}
	
	@Test
	public void onDomainTermsQuery(){
		List<Person> list = tq.getPersonPaulista();
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have Maria", "Maria", list.get(0).getName());
		assertEquals("The list should have Marcos", "Marcos", list.get(1).getName());
	}
	
	@Test
	public void twoDomainTerms(){
		List<Person> list = tq.getPersonTeenagerPaulista();
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Maria", "Maria", list.get(0).getName());
	}
	
	@Test
	public void domainTermWithTwoWords(){
		List<Person> list = tq.getPersonOldGuys();
		assertEquals("The list should have 2 person", 2, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
		assertEquals("The list should have Silvia", "Silvia", list.get(1).getName());
	}
	
	@Test
	public void domainTermWithParameter(){
		List<Person> list = tq.getPersonPaulistaByAge(20);
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
	}

}
