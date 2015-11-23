package test.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import interprete.AddNode;
import interprete.AndNode;
import interprete.AssignmentNode;
import interprete.AtomNode;
import interprete.IInterpreter;
import interprete.InterpreterController;
import interprete.PSNode;
import interprete.PSValue;
import interprete.Variable;
import model.Accion;
import model.PaqueteAlgoritmico;
import model.PreguntaEspecifica;

public class InterpreterTest {

	private static String predefinedMessage = "Nothing to do here, there shouldnt be any errors";
	private static long idPaquete = 1;
	/*private static EntityManager em;
	
	@BeforeClass
	public static void init() {
		 em = Persistence.createEntityManagerFactory("Servidor").createEntityManager();
		List<PaqueteAlgoritmico> lst = em.createQuery("Select p from PaqueteAlgoritmico p ORDER BY p.fechaHistoria ASC",PaqueteAlgoritmico.class).getResultList();
		if (!lst.isEmpty()){
			idPaquete = lst.get(0).getId();
		}
		else{
			System.out.println("Lista de paquetes es vacia!");
		}
	}*/
	
	@Test
	public void test1(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 + 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test2(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x+y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test3(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x+4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test4(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 + true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 + true)", e.getMessage());
		}
	}
	
	@Test
	public void test5(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x + y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x + y)", e.getMessage());
		}
	}

	@Test
	public void test6(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x + y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x + y)", e.getMessage());
		}
	}
	
	@Test
	public void test7(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test8(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x/y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test9(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x/4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test10(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 / true)", e.getMessage());
		}
	}
	
	@Test
	public void test11(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x / y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x / y)", e.getMessage());
		}
	}

	@Test
	public void test12(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x / y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x / y)", e.getMessage());
		}
	}
	
	@Test
	public void test13(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / 0";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test14(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 0 x = 3.1 / x";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test15(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test16(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x mod y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test17(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x mod 4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test18(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 mod true)", e.getMessage());
		}
	}
	
	@Test
	public void test19(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x mod y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x mod y)", e.getMessage());
		}
	}

	@Test
	public void test20(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x mod y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x mod y)", e.getMessage());
		}
	}
	
	@Test
	public void test21(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod 0";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test22(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 0 x = 3.1 mod x";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test23(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 * 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test24(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x * y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test25(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x * 4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test26(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 * true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 * true)", e.getMessage());
		}
	}
	
	@Test
	public void test27(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x * y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x * y)", e.getMessage());
		}
	}

	@Test
	public void test28(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x * y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x * y)", e.getMessage());
		}
	}
	
	@Test
	public void test29(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 ^ 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test30(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x ^ y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test31(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x ^ 4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test32(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 ^ true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 ^ true)", e.getMessage());
		}
	}
	
	@Test
	public void test33(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x ^ y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x ^ y)", e.getMessage());
		}
	}

	@Test
	public void test34(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x ^ y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x ^ y)", e.getMessage());
		}
	}
	
	@Test
	public void test35(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 - 2.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test36(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test37(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x - 4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test38(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 - true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 - true)", e.getMessage());
		}
	}
	
	@Test
	public void test39(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x - y)", e.getMessage());
		}
	}

	@Test
	public void test40(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x - y)", e.getMessage());
		}
	}
	
	@Test
	public void test41(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1==1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test42(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x==z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test43(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1>=1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test44(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>=z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x >= z)", e.getMessage());
		}
	}
	
	@Test
	public void test45(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>=1.8 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x >= 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test46(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x>=z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test47(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1>1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test48(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x > z)", e.getMessage());
		}
	}
	
	@Test
	public void test49(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>1.8 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x > 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test50(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x>z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test51(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<=1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test52(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<=z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x <= z)", e.getMessage());
		}
	}
	
	@Test
	public void test53(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<=1.8 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x <= 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test54(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x<=z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test55(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test56(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x < z)", e.getMessage());
		}
	}
	
	@Test
	public void test57(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<1.8 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x < 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test58(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x<z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test59(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<>1 then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test60(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<>z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test61(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if true and true then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test62(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x and z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test63(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x and 1.8 then x=true endif";
		
		try{
			interprete.check(code);
			}catch(Exception e){
			assertEquals("illegal expression: (x and 1.8)", e.getMessage());
		}	
	}
		
	@Test
	public void test64(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x and z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x and z)", e.getMessage());
		}
	}
	
	@Test
	public void test65(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if true or true then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test66(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x or z then x=true endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test67(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x or 1.8 then x=true endif";
		
		try{
			interprete.check(code);
			}catch(Exception e){
			assertEquals("illegal expression: (x or 1.8)", e.getMessage());
		}	
	}
		
	@Test
	public void test68(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x or z then x=1.4 endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (x or z)", e.getMessage());
		}
	}
	
	@Test
	public void test69(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x=1.2";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test70(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x=true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Can't assign boolean to float type. Variable 'x'", e.getMessage());
		}
	}
	
	@Test
	public void test71(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR\n" + 
				       "x=3.5";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Can't assign number to boolean type. Variable 'x'", e.getMessage());
		}
	}
	
	@Test
	public void test72(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR\n" + 
				       "z=3.5";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'z' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test73(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR\n" + 
				       "y=true x=y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test74(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR\n" + 
				       "x=y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'y' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test75(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=HISTORY[-2]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test76(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=dev[-2,-1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test77(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=avg[-2, -1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test78(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test79(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, true]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test80(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, 1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Index out of bound in average expresion. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test81(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -3]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalide range in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test82(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test83(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test85(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test86(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, 3+5]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Index out of bound in average expresion. Greater than zero.", e.getMessage());
		}
	}
	@Test
	public void test87(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -4+1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalide range in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test88(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test89(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, true]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test90(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, 1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Index out of bound in deviation expresion. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test91(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -3]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid range in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test92(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test93(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test95(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test96(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, 3+5]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Index out of bound in deviation expresion. Greater than zero.", e.getMessage());
		}
	}
	@Test
	public void test97(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -4+1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid range in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test98(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=HISTORY[-1]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test99(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y=10 x=HISTORY[y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test100(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y=10 x=HISTORY[-y]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test101(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "z=true x=HISTORY[z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in History expression.", e.getMessage());
		}
	}
	
	@Test
	public void test102(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "z=true x=HISTORY[-z]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test103(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=HISTORY[10]";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Index out of bound in History expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test104(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "x=5.3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'x' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test105(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "if x then println(3) endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'x' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test106(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "if 4 then println(3) endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal boolean expression inside if-statement: 4.0", e.getMessage());
		}
	}
	
	@Test
	public void test107(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=4.0 if x then println(3) endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal boolean expression inside if-statement: x", e.getMessage());
		}
	}
	
	@Test
	//it proves that else statement is checked too even though it will never be executed
	public void test108(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "if true then\n" + 
				       "        println(3)\n" + 
				       "else\n" + 
				       "       println(4)\n" + 
				       "       println(z)\n" + 
				       "endif";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'z' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test109(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean z ENDVAR\n" + 
				       "z= not true";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test110(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean z ENDVAR\n" + 
				       "z= not 4";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (not 4.0)", e.getMessage());
		}
	}
	
	@Test
	public void test111(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=8 BUY(x) BUY(10)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test112(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "BUY(-10) BUY(10)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Error: negative Buy operation not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void test113(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=-8 BUY(x)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test114(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true BUY(x)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in Buy expression.", e.getMessage());
		}
	}
	
	@Test
	public void test115(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=8 SELL(x) SELL(10)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test116(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "SELL(-10) SELL(10)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Error: negative SELL operation not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void test117(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=-8 SELL(x)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test118(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true SELL(x)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Invalid expression in Sell expression.", e.getMessage());
		}
	}
	
	@Test
	public void test119(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or (true and false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test120(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and true) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test121(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and BUY(5)) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("line 1:42 no viable alternative at input 'BUY'", e.getMessage());
		}
	}
	
	@Test
	public void test122(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and 5) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("illegal expression: (true and 5.0)", e.getMessage());
		}
	}
	
	@Test
	public void test123(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR x=true or ((true and y) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("Variable 'y' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test124(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x=true or ((true and y) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test125(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x= not y or ((true and y) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test126(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x= not not (not y  and false) or ((true and y) or false)";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test127(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR x= 4 + (5*2) - 3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test128(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR x= (4 + 5)*2 - 3";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test129(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z y=4 z=5 ENDVAR x= (y + z)*z - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("line 1:16 missing 'ENDVAR' at 'y'", e.getMessage());
		}
	}
	
	@Test
	public void test130(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR y=4 z=5 ENDVAR x= (y + z)*z - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals("line 1:31 missing EOF at 'ENDVAR'", e.getMessage());
		}
	}
	
	@Test
	public void test131(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR y=4 z=5 x= (y + z)*z - y";
		
		try{
			interprete.check(code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	
	/**************************** EVALUATE TEST ****************************************************************************************/
	/***********************************************************************************************************************************/
	@Test
	public void test132(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 + 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test133(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x+y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test134(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x+4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test135(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 + true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 + true)", e.getMessage());
		}
	}
	
	@Test
	public void test136(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x + y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x + y)", e.getMessage());
		}
	}

	@Test
	public void test137(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x + y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x + y)", e.getMessage());
		}
	}
	
	@Test
	public void test138(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test139(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x/y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test140(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x/4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test141(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 / true)", e.getMessage());
		}
	}
	
	@Test
	public void test142(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x / y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x / y)", e.getMessage());
		}
	}

	@Test
	public void test143(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x / y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x / y)", e.getMessage());
		}
	}
	
	@Test
	public void test144(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 / 0";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test145(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 0 x = 3.1 / x";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test146(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test147(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x mod y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test148(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x mod 4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test149(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 mod true)", e.getMessage());
		}
	}
	
	@Test
	public void test150(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x mod y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x mod y)", e.getMessage());
		}
	}

	@Test
	public void test151(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x mod y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x mod y)", e.getMessage());
		}
	}
	
	@Test
	public void test152(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 mod 0";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test153(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 0 x = 3.1 mod x";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: division by 0.", e.getMessage());
		}
	}
	
	@Test
	public void test154(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 * 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test155(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x * y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test156(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x * 4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test157(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 * true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 * true)", e.getMessage());
		}
	}
	
	@Test
	public void test158(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x * y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x * y)", e.getMessage());
		}
	}

	@Test
	public void test159(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x * y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x * y)", e.getMessage());
		}
	}
	
	@Test
	public void test160(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 ^ 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test161(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x ^ y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test162(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x ^ 4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test163(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 ^ true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 ^ true)", e.getMessage());
		}
	}
	
	@Test
	public void test164(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x ^ y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x ^ y)", e.getMessage());
		}
	}

	@Test
	public void test165(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x ^ y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x ^ y)", e.getMessage());
		}
	}
	
	@Test
	public void test166(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 - 2.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test167(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test168(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR\n" + 
				       "x = 3.1 y = 2.3 z = x - 4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test169(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x = 3.1 - true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (3.1 - true)", e.getMessage());
		}
	}
	
	@Test
	public void test170(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x Boolean y ENDVAR\n" + 
				       "x=3.1 y=true x = x - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x - y)", e.getMessage());
		}
	}

	@Test
	public void test171(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y Float z ENDVAR\n" + 
				       "x=true y=true z = x - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x - y)", e.getMessage());
		}
	}
	
	@Test
	public void test172(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1==1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test173(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x==z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test174(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1>=1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test175(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>=z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x >= z)", e.getMessage());
		}
	}
	
	@Test
	public void test176(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>=1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x >= 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test177(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x>=z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test178(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1>1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test179(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x > z)", e.getMessage());
		}
	}
	
	@Test
	public void test180(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x>1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x > 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test181(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x>z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test182(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<=1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test183(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<=z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x <= z)", e.getMessage());
		}
	}
	
	@Test
	public void test184(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<=1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x <= 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test185(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x<=z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test186(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test187(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x < z)", e.getMessage());
		}
	}
	
	@Test
	public void test188(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x < 1.8)", e.getMessage());
		}
	}
	
	@Test
	public void test189(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x<z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test190(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if 1<>1 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test191(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x<>z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test192(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if true and true then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test193(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x and z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test194(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x and 1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
			}catch(Exception e){
			assertEquals("illegal expression: (x and 1.8)", e.getMessage());
		}	
	}
		
	@Test
	public void test195(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x and z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x and z)", e.getMessage());
		}
	}
	
	@Test
	public void test196(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x Float z ENDVAR\n" + 
				       "if true or true then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test197(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x or z then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test198(){
		
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,z ENDVAR\n" + 
				       "x=true z=false if x or 1.8 then x=true endif";
		
		try{
			interprete.evaluate(idPaquete, code);
			}catch(Exception e){
			assertEquals("illegal expression: (x or 1.8)", e.getMessage());
		}	
	}
		
	@Test
	public void test199(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,z ENDVAR\n" + 
				       "x=1.2 z=1.3 if x or z then x=1.4 endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (x or z)", e.getMessage());
		}
	}
	
	@Test
	public void test200(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x=1.2";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test201(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR\n" + 
				       "x=true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Can't assign boolean to float type. Variable 'x'", e.getMessage());
		}
	}
	
	@Test
	public void test202(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR\n" + 
				       "x=3.5";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Can't assign number to boolean type. Variable 'x'", e.getMessage());
		}
	}
	
	@Test
	public void test203(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR\n" + 
				       "z=3.5";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'z' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test204(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR\n" + 
				       "y=true x=y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test205(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR\n" + 
				       "x=y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'y' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test206(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=HISTORY[-2]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test207(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=dev[-2,-1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test208(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "x=avg[-2, -1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test209(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in average expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test210(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, true]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test211(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=avg[-2, 1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in average expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test212(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -3]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test213(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test214(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test215(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test216(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, 3+5]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in average expression. Greater than zero.", e.getMessage());
		}
	}
	@Test
	public void test217(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=avg[-2, -4+1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in average expression.", e.getMessage());
		}
	}
	
	@Test
	public void test218(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in deviation expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test219(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, true]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test220(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y ENDVAR\n" + 
				       "y = 3.9 x=dev[-2, 1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in deviation expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test221(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -3]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test222(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test223(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test224(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test225(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, 3+5]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in deviation expression. Greater than zero.", e.getMessage());
		}
	}
	@Test
	public void test226(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y = 3.9 z=true x=dev[-2, -4+1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid range in deviation expression.", e.getMessage());
		}
	}
	
	@Test
	public void test227(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=HISTORY[-1]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test228(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y=10 x=HISTORY[y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in History expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test229(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "y=10 x=HISTORY[-y]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test230(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "z=true x=HISTORY[z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in History expression.", e.getMessage());
		}
	}
	
	@Test
	public void test231(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "z=true x=HISTORY[-z]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (-z)", e.getMessage());
		}
	}
	
	@Test
	public void test232(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=HISTORY[10]";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Index out of bound in History expression. Greater than zero.", e.getMessage());
		}
	}
	
	@Test
	public void test233(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "x=5.3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'x' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test234(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "if x then println(3) endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'x' not definied.", e.getMessage());
		}
	}
	
	@Test
	public void test235(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "if 4 then println(3) endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal boolean expression inside if-statement: 4.0", e.getMessage());
		}
	}
	
	@Test
	public void test236(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "x=4.0 if x then println(3) endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal boolean expression inside if-statement: x", e.getMessage());
		}
	}
	
	@Test
	//it proves that else statement is evaluateed too even though it will never be executed
	public void test237(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y Boolean z ENDVAR\n" + 
				       "if true then\n" + 
				       "        println(3)\n" + 
				       "else\n" + 
				       "       println(4)\n" + 
				       "       println(z)\n" + 
				       "endif";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'z' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test238(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean z ENDVAR\n" + 
				       "z= not true";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test239(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean z ENDVAR\n" + 
				       "z= not 4";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (not 4.0)", e.getMessage());
		}
	}
	
	@Test
	public void test240(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=8 BUY(x) BUY(10)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test241(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "BUY(-10) BUY(10)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: negative Buy operation not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void test242(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=-8 BUY(x)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test243(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true BUY(x)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in Buy expression.", e.getMessage());
		}
	}
	
	@Test
	public void test244(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=8 SELL(x) SELL(10)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test245(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "SELL(-10) SELL(10)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Error: negative SELL operation not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void test246(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x ENDVAR x=-8 SELL(x)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test247(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true SELL(x)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Invalid expression in Sell expression.", e.getMessage());
		}
	}
	
	@Test
	public void test248(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or (true and false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test249(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and true) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test250(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and BUY(5)) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("line 1:42 no viable alternative at input 'BUY'", e.getMessage());
		}
	}
	
	@Test
	public void test251(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x ENDVAR x=true or ((true and 5) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("illegal expression: (true and 5.0)", e.getMessage());
		}
	}
	
	@Test
	public void test252(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR x=true or ((true and y) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("Variable 'y' not initialized.", e.getMessage());
		}
	}
	
	@Test
	public void test253(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x=true or ((true and y) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test254(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x= not y or ((true and y) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test255(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Boolean x,y ENDVAR y = false x= not not (not y  and false) or ((true and y) or false)";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test256(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR x= 4 + (5*2) - 3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test257(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR x= (4 + 5)*2 - 3";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	@Test
	public void test258(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z y=4 z=5 ENDVAR x= (y + z)*z - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("line 1:16 missing 'ENDVAR' at 'y'", e.getMessage());
		}
	}
	
	@Test
	public void test259(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR y=4 z=5 ENDVAR x= (y + z)*z - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals("line 1:31 missing EOF at 'ENDVAR'", e.getMessage());
		}
	}
	
	@Test
	public void test260(){
					
		IInterpreter interprete = new InterpreterController();
		String code = "VAR Float x,y,z ENDVAR y=4 z=5 x= (y + z)*z - y";
		
		try{
			interprete.evaluate(idPaquete, code);
		}catch(Exception e){
			assertEquals(predefinedMessage, e.getMessage());
		}
	}
	
	
	
	
//	/**************	************** TEST ADD NODE *********************************************************************/
//	@Test
//	//test AddNode success
//	public void test() {
//		System.out.println("/**************************** TEST ADD NODE *********************************************************************/");
//			
//		PSNode oper1 = new AtomNode(1.1);
//		PSNode	 oper2 = new AtomNode(2.1);
//		
//		PSNode result = new AddNode(oper1, oper2);
//		PSValue value = result.check();
//		System.out.println("Result of check " + value.toString());
//		assertEquals(3.2, value.asDouble().doubleValue(),0.00000000001);
//	
//		
//		PSValue value2 = result.evaluate();
//		System.out.println("Result of evaluate " + value2.toString());
//		assertEquals(3.2, value2.asDouble().doubleValue(),0.00000000001);
//		
//		System.out.println("Result of node to string ");
//		System.out.println(result.toString());
//		assertEquals("(1.1 + 2.1)", result.toString());
//	}
//	
//	@Test
//	//test AddNode error, operand1 is not a number
//	public void test2() {
//		PSNode oper1 = new AtomNode(true);
//		PSNode oper2 = new AtomNode(2.1);
//		
//		PSNode result = new AddNode(oper1, oper2);
//		try{
//			PSValue value = result.check();
//			System.out.println("Result of check " + value.toString());
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("illegal expression: (true + 2.1)", e.getMessage());
//		}
//		
//		try{
//			PSValue value2 = result.evaluate();
//			System.out.println("Result of evaluate " + value2.toString());
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("illegal expression: (true + 2.1)", e.getMessage());
//		}
//		
//		System.out.println("Result of node to string ");
//		System.out.println(result.toString());
//		assertEquals("(true + 2.1)", result.toString());
//		System.out.println("/**************************** END TEST ADD NODE *********************************************************************/");
//		
//	}
//	/**************************** END TEST ADD NODE *********************************************************************/
//	
//	/**************************** TEST AND NODE *********************************************************************/
//	@Test
//	//test AndNode success
//	public void test3() {
//		
//		System.out.println("/**************************** TEST AND NODE *********************************************************************/");
//		
//		PSNode oper1 = new AtomNode(true);
//		PSNode oper2 = new AtomNode(true);
//		PSNode oper3 = new AtomNode(false);
//		
//		PSNode result = new AndNode(oper1, oper2);
//		PSNode result2 = new AndNode(oper1, oper3);
//		
//		PSValue value = result.check();
//		System.out.println("Result of check " + value.toString());
//		assertEquals(true, value.asBoolean());
//	
//		
//		PSValue value2 = result.evaluate();
//		System.out.println("Result of evaluate " + value2.toString());
//		assertEquals(true, value2.asBoolean());
//		
//		System.out.println("Result of node to string ");
//		System.out.println(result.toString());
//		assertEquals("(true && true)", result.toString());
//		
//		PSValue value3 = result2.check();
//		System.out.println("Result of check " + value3.toString());
//		assertEquals(false, value3.asBoolean());
//	
//		
//		PSValue value4 = result2.evaluate();
//		System.out.println("Result of evaluate " + value4.toString());
//		assertEquals(false, value4.asBoolean());
//		
//		System.out.println("Result of node to string ");
//		System.out.println(result2.toString());
//		assertEquals("(true && false)", result2.toString());
//		
//	}
//	
//	@Test
//	//test AndNode error, operand2 is not a boolean
//	public void test4() {
//		PSNode oper1 = new AtomNode(true);
//		PSNode oper2 = new AtomNode(2.1);
//		
//		PSNode result = new AndNode(oper1, oper2);
//		try{
//			PSValue value = result.check();
//			System.out.println("Result of check " + value.toString());
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("illegal expression: (true && 2.1)", e.getMessage());
//		}
//		
//		try{
//			PSValue value2 = result.evaluate();
//			System.out.println("Result of evaluate " + value2.toString());
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("illegal expression: (true && 2.1)", e.getMessage());
//		}
//		
//		System.out.println("Result of node to string ");
//		System.out.println(result.toString());
//		assertEquals("(true && 2.1)", result.toString());
//		
//		System.out.println("/**************************** END TEST AND NODE *********************************************************************/");
//		
//	}
//	/**************************** END TEST AND NODE *********************************************************************/
//	
//	/**************************** TEST ASSIGNMENT NODE *********************************************************************/
//	@Test
//	public void test5() {
//		
//		System.out.println("/**************************** TEST ASSIGNMENT NODE *********************************************************************/");
//		IInterpreter interpreter = new InterpreterController();
//		String code = "VAR Float x ENDVAR\n" + 
//					   "x=8.2";
//		
//		interpreter.check(code);
//		interpreter.evaluate(idPaquete, code);
//		
//		code = "x=true";
//		try{
//			interpreter.check(code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Variable 'x' not definied.", e.getMessage());
//		}
//		
//		try{
//			interpreter.evaluate(idPaquete, code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Variable 'x' not definied.", e.getMessage());
//		}
//		
//		
//		code = "VAR Float x ENDVAR\n" + 
//				"x=true";
//		try{
//			interpreter.check(code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Can't assign boolean to float type. Variable 'x'", e.getMessage());
//		}
//		
//		try{
//			interpreter.evaluate(idPaquete, code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Can't assign boolean to float type. Variable 'x'", e.getMessage());
//		}
//		
//		code = "VAR Boolean x ENDVAR\n" + 
//				"x=1";
//		try{
//			interpreter.check(code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Can't assign number to boolean type. Variable 'x'", e.getMessage());
//		}
//		
//		try{
//			interpreter.evaluate(idPaquete, code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Can't assign number to boolean type. Variable 'x'", e.getMessage());
//		}
//		
//		PSNode exp = new AtomNode(3.9);
//		Map<String, Variable> v = new HashMap<String, Variable>();
//		PSNode node = new AssignmentNode("x", exp, v);
//		System.out.println(node.toString());
//		assertEquals("(x = 3.9)", node.toString());
//		
//		System.out.println("/**************************** END TEST ASSIGNMENT NODE *********************************************************************/");
//		
//	}
//	
//	/**************************** TEST DIV NODE *********************************************************************/
//	@Test
//	public void test6() {
//		
//		System.out.println("/**************************** TEST DIV NODE *********************************************************************/");
//		IInterpreter interpreter = new InterpreterController();
//		String code = "VAR Float x ENDVAR\n" + 
//					   "x=3.1/2.4 println(x)";
//		
//		interpreter.check(code);
//		interpreter.evaluate(idPaquete, code);
//		
//		code = "x=3/0";
//		try{
//			interpreter.check(code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Error: division by 0.", e.getMessage());
//		}
//		
//		try{
//			interpreter.evaluate(idPaquete, code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			assertEquals("Error: division by 0.", e.getMessage());
//		}
//		
//		code = "VAR Float x,y ENDVAR" + "\n" + 
//	           "x=3.1/y println(x)";
//		try{
//			interpreter.check(code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			//assertEquals("Variable 'y' not initialized.", e.getMessage());
//		}
//		
//		try{
//			interpreter.evaluate(idPaquete, code);
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			//assertEquals("Variable 'y' not initialized.", e.getMessage());
//		}
//		
//		code = "VAR Float x,y ENDVAR" + "\n" + 
//		           "y = 0 x=3.1/y println(x)";
//			try{
//				interpreter.check(code);
//			}catch(Exception e){
//				System.out.println(e.getMessage());
//				//assertEquals("Variable 'y' not initialized.", e.getMessage());
//			}
//			
//			try{
//				interpreter.evaluate(idPaquete, code);
//			}catch(Exception e){
//				System.out.println(e.getMessage());
//				//assertEquals("Variable 'y' not initialized.", e.getMessage());
//			}
//	
//		System.out.println("/**************************** END TEST DIV NODE *********************************************************************/");
//		
//	}
	
	
	
	
	
	
	
	
	
	
	
	
}
