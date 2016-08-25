package org.esfige.querybuilder.mongodb.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.esfinge.querybuilder.mongodb.MongoDBVisitorFactory;
import org.esfinge.querybuilder.mongodb.TestQuery;
import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.methodparser.DSLMethodParser;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.methodparser.MethodParser;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@SuppressWarnings("rawtypes")
public class TestDynamicQueriesMongoDBQueryVisitor {
	
	QueryVisitor visitor;
	MethodParser mp = new DSLMethodParser();
	
	@Before
	public void init(){
		mp.setInterface(TestQuery.class);
		mp.setEntityClassProvider(ServiceLocator.getServiceImplementation(EntityClassProvider.class));
	}
	
	@Test
	public void notDynamicQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("nome");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertFalse("Query should not be dynamic", qr.isDynamic());
		String query = qr.getQuery().toString();
		assertEquals("{ \"name\" : \"nome\"}", query);
	}
	
	@Test
	public void ignoreWhenNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAge", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ }", qr.getQuery().toString());
		
		args[0] = new Integer(15);
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"age\" : 15}", qr.getQuery().toString());
		
	}
	
	@Test
	public void compareToNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"lastName\" :  null }", qr.getQuery().toString());
		
		args[0] = "Fonseca";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"lastName\" : \"Fonseca\"}", qr.getQuery().toString());
		
	}
	
	@Test
	public void twoCompareToNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameAndLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = null;
		args[1] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" :  null } , { \"lastName\" :  null }]}", qr.getQuery().toString());
	
		args[0] = "Eduardo";
		args[1] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : \"Eduardo\"} , { \"lastName\" :  null }]}", qr.getQuery().toString());
		
		args[0] = null;
		args[1] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" :  null } , { \"lastName\" : \"Guerra\"}]}", qr.getQuery().toString());
		
		args[0] = "Eduardo";
		args[1] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : \"Eduardo\"} , { \"lastName\" : \"Guerra\"}]}", qr.getQuery().toString());
	
	}
	
	@Test
	public void startsAndCompareToNull(){
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByNameAndAge", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = "M";
		args[1] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : { \"$regex\" : \"M.*\"}} , { \"age\" :  null }]}", qr.getQuery().toString());
	
		args[0] = "Eduardo";
		args[1] = 15;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : { \"$regex\" : \"Eduardo.*\"}} , { \"age\" : 15}]}", qr.getQuery().toString());
	
	}
	
	@Test
	public void twoIgnoreWhenNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameOrLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = null;
		args[1] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ }", qr.getQuery().toString());
	
		args[0] = "Eduardo";
		args[1] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		assertEquals("{ \"name\" : \"Eduardo\"}", qr.getQuery().toString());
		
		args[0] = null;
		args[1] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"lastName\" : \"Guerra\"}", qr.getQuery().toString());
		
		args[0] = "Eduardo";
		args[1] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$or\" : [ { \"name\" : \"Eduardo\"} , { \"lastName\" : \"Guerra\"}]}", qr.getQuery().toString());
	
	}
	
	@Test
	public void twoIgnoreWhenNullQueryPlusOther(){
		
		Method m = null;
		try {
			Class[] args = new Class[3];
			args[0] = String.class;
			args[1] = Integer.class;
			args[2] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameAndAgeAndLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[3];
		args[0] = null;
		args[1] = new Integer(15);
		args[2] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"age\" : 15}", qr.getQuery().toString());
		
		args[0] = "Eduardo";
		args[1] = new Integer(15);
		args[2] = null;
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : \"Eduardo\"} , { \"age\" : 15}]}", qr.getQuery().toString());
		
		args[0] = null;
		args[1] = new Integer(15);
		args[2] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"age\" : 15} , { \"lastName\" : \"Guerra\"}]}", qr.getQuery().toString());
		
		args[0] = "Eduardo";
		args[1] = new Integer(15);
		args[2] = "Guerra";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("{ \"$and\" : [ { \"name\" : \"Eduardo\"} , { \"age\" : 15} , { \"lastName\" : \"Guerra\"}]}", qr.getQuery().toString());
		
	}

}
